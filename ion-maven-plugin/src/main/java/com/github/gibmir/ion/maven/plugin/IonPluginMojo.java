package com.github.gibmir.ion.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
  name = "ionise",
  defaultPhase = LifecyclePhase.GENERATE_SOURCES,
  requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
@Execute(goal = "ionise", phase = LifecyclePhase.GENERATE_SOURCES)
public class IonPluginMojo extends AbstractMojo {
  @Parameter(property = "dir", defaultValue = "${project.build.resourceDire}")
  private String scanDir;
  private String packageName;

  public static String asClassName(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  public static String asFieldName(String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }

  public static String asAnnotationMember(String name) {
    return '"' + name + '"';
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
//      ServiceSchema serviceSchema;
//      TypeSpec.Builder serviceTypeBuilder = TypeSpec.classBuilder(serviceSchema.getName())
//        .addModifiers(Modifier.PUBLIC);
//      Procedure[] serviceProcedures = serviceSchema.getServiceProcedures();
//      Procedure serviceProcedure = serviceProcedures[0];
//      ParameterType[] parameterTypes = serviceProcedure.getArgumentTypes();
//      MethodSpec.Builder serviceMethodBuilder = MethodSpec.methodBuilder(serviceProcedure.getName());
//      for (ParameterType argumentType : parameterTypes) {
//        TypeSpec parameterType = TypeSpec.classBuilder().addModifiers(Modifier.PUBLIC).build();
//        serviceMethodBuilder.addParameter(ParameterSpec.builder(parameterType).build());
//      }
//      serviceMethodBuilder.addParameter(ParameterSpec.builder(TypeSpec.classBuilder()).build());
//      JavaFile.builder(packageName, serviceTypeBuilder.build()).build().writeTo(Path.of(""));
    } catch (Exception e) {
      throw new MojoExecutionException("Exception occurred while executing \"ionise\" goal", e);
    }
  }
}
