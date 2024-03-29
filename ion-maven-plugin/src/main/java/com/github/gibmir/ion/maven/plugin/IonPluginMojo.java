package com.github.gibmir.ion.maven.plugin;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.namespace.Namespace;
import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.SchemaBean;
import com.github.gibmir.ion.lib.schema.namespace.NamespaceBean;
import com.github.gibmir.ion.maven.plugin.exceptions.IonPluginException;
import com.github.gibmir.ion.maven.plugin.procedure.ProcedureGenerationUtils;
import com.github.gibmir.ion.maven.plugin.reader.IonSchemaReader;
import com.github.gibmir.ion.maven.plugin.type.TypeGenerationUtils;
import com.squareup.javapoet.JavaFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Mojo(name = "ionise", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
  requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
@Execute(goal = "ionise", phase = LifecyclePhase.GENERATE_SOURCES)
public final class IonPluginMojo extends AbstractMojo {
  private static final Logger LOGGER = LoggerFactory.getLogger(IonPluginMojo.class);
  private static final Jsonb JSONB = JsonbProvider.provider().create().build();
  public static final String JSON_FILE_EXTENSION = "json";
  public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
  public static final String PACKAGE_SEPARATOR = ".";
  @Parameter(property = "scan", defaultValue = "${project.build.resources[0].directory}")
  private String scanDirectoryPath;
  @Parameter(property = "codeGen", defaultValue = "${project.build.directory}")
  private String codeGenPath;
  @Parameter(property = "package")
  private String packageName;

  /**
   * Represents name as class name for code generation.
   *
   * @param name procedure or type name
   * @return class name
   */
  public static String asClassName(final String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  /**
   * Represents name as field name for code generation.
   *
   * @param name procedure or type name
   * @return field name
   */
  public static String asFieldName(final String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }

  /**
   * Represents name as annotation parameter for code generation.
   *
   * @param name procedure or type name
   * @return annotation parameter
   */
  public static String asAnnotationMember(final String name) {
    return '"' + name + '"';
  }

  @Override
  public void execute() throws MojoExecutionException {
    try {
      Path scanPath = Path.of(scanDirectoryPath);
      LOGGER.info("Scanning [{}]...", scanPath);
      List<JsonValue> schemas = Files.walk(scanPath)
        .peek(path -> LOGGER.info("Path [{}] was found", path))
        .filter(path -> path.toString().endsWith(JSON_FILE_EXTENSION))
        .map(IonPluginMojo::readSchemaAsString)
        .map(schemaJson -> JSONB.fromJson(schemaJson, JsonValue.class))
        .collect(Collectors.toList());
      List<Schema> schemaList = new ArrayList<>(schemas.size());
      String codeGenerationString = resolveCodeGenerationString();
      for (JsonValue schemaJson : schemas) {
        JsonObject schemaObject = schemaJson.asJsonObject();
        List<Namespace> namespaces = readNamespaces(codeGenerationString, schemaObject);
        Schema schema = new SchemaBean(namespaces);
        schemaList.add(schema);
      }
      LOGGER.info("Goal was executed. Schemas {}", schemaList);
    } catch (Exception e) {
      throw new MojoExecutionException("Exception occurred while executing \"ionise\" goal", e);
    }
  }

  private List<Namespace> readNamespaces(final String codeGenerationString, final JsonObject schemaObject) throws IOException {
    JsonValue namespacesJson = schemaObject.get(IonSchemaReader.SCHEMA_NAMESPACES_KEY);
    JsonObject namespacesObject = namespacesJson.asJsonObject();
    Set<String> namespaceNames = namespacesObject.keySet();
    List<Namespace> namespaces = new ArrayList<>();
    for (String namespaceName : namespaceNames) {
      JsonValue namespaceJson = namespacesObject.get(namespaceName);
      JsonObject namespaceObject = namespaceJson.asJsonObject();
      Map<String, TypeDeclaration> typeDeclarations = IonSchemaReader.readTypes(namespaceObject);
      Stack<TypeDeclaration> loadingStack = TypeGenerationUtils.buildLoadingStack(typeDeclarations);
      String namespacedPackageName = getNamespacedPackageName(packageName, namespaceName);
      LOGGER.info("Loading namespace [{}] into package [{}]", namespaceName, namespacedPackageName);
      TypeGenerationUtils.loadTypes(loadingStack, namespacedPackageName,
        Path.of(codeGenerationString));
      List<Procedure> procedures = IonSchemaReader.readProcedures(namespaceObject);
      for (Procedure procedure : procedures) {
        JavaFile.builder(namespacedPackageName,
            ProcedureGenerationUtils.asTypeSpecification(procedure)).build()
          .writeTo(Path.of(codeGenerationString));
      }
      String namespaceId = namespaceObject.getString(IonSchemaReader.ID_KEY, IonSchemaReader.DEFAULT_DESCRIPTION);
      String namespaceDescription = namespaceObject.getString(IonSchemaReader.DESCRIPTION_KEY, IonSchemaReader.DEFAULT_DESCRIPTION);
      namespaces.add(new NamespaceBean(namespaceId, namespaceName, namespaceDescription, procedures, typeDeclarations));
    }
    return namespaces;
  }

  private String getNamespacedPackageName(final String packageName, final String namespaceName) {
    if (packageName == null || packageName.isEmpty()) {
      return namespaceName;
    }
    return packageName + PACKAGE_SEPARATOR + namespaceName;
  }

  private String resolveCodeGenerationString() {
    if (this.packageName != null && !this.packageName.isEmpty()) {
      return this.codeGenPath + SEPARATOR + "generated-sources" + SEPARATOR + packageName;
    } else {
      return this.codeGenPath + SEPARATOR + "generated-sources";
    }
  }

  private static String readSchemaAsString(final Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      throw new IonPluginException("Exception occurred while reading [" + path + ']', e);
    }
  }
}
