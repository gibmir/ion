package com.github.gibmir.ion.maven.plugin.environment;

import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.service.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.Arrays;
import java.util.Objects;

public class IonMavenPluginTestEnvironment {
  public static final String TEST_PROCEDURE_NAME = "testProcedure";
  public static final String TEST_SERVICE_NAME = "testService";
  public static final String TEST_DESCRIPTION = "test description";
  public static final String TEST_ID = "someId";
  public static final String TEST_PROPERTY_NAME = "testProperty";
  public static final String TEST_TYPE_NAME = "string";

  public static class TestService implements Service {
    private final Procedure[] procedures;
    private final String id;
    private final String name;
    private final String description;

    public TestService(String id, String name, String description, Procedure... procedures) {
      this.procedures = procedures;
      this.id = id;
      this.name = name;
      this.description = description;
    }

    @Override
    public Procedure[] getServiceProcedures() {
      return procedures;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  public static class TestPropertyType implements PropertyType {
    private final String id;
    private final String name;
    private final String description;
    private final String typeName;

    public TestPropertyType(String id, String name, String description, String typeName) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.typeName = typeName;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return description;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestPropertyType that = (TestPropertyType) o;
      return Objects.equals(id, that.id) &&
        name.equals(that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, name);
    }

    @Override
    public String toString() {
      return "TestPropertyType{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
    }


    @Override
    public String getTypeName() {
      return typeName;
    }
  }

  public static class TestProcedure implements Procedure {

    private PropertyType returnArgumentType;
    private String description;
    private String name;
    private String id;
    private PropertyType[] propertyTypes;

    public TestProcedure(PropertyType returnArgumentType, String description, String name, String id,
                         PropertyType... propertyTypes) {
      this.returnArgumentType = returnArgumentType;
      this.description = description;
      this.name = name;
      this.id = id;
      this.propertyTypes = propertyTypes;
    }

    @Override
    public PropertyType[] getArgumentTypes() {
      return propertyTypes;
    }

    @Override
    public PropertyType getReturnArgumentType() {
      return returnArgumentType;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  public static class TestTypeParameter implements TypeParameter {
    private final String id;
    private final String name;
    private final String description;

    public TestTypeParameter(String id, String name, String description) {
      this.id = id;
      this.name = name;
      this.description = description;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  public static class TestTypeDeclaration implements TypeDeclaration {
    private final String id;
    private final String name;
    private final String description;
    private final PropertyType[] propertyTypes;
    private final TypeParameter[] typeParameters;

    public TestTypeDeclaration(String id, String name, String description, TypeParameter[] typeParameters, PropertyType... propertyTypes) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.typeParameters = typeParameters;
      this.propertyTypes = propertyTypes;
    }

    @Override
    public PropertyType[] getPropertyTypes() {
      return propertyTypes;
    }

    @Override
    public TypeParameter[] getParameters() {
      return typeParameters;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return description;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestTypeDeclaration that = (TestTypeDeclaration) o;
      return Objects.equals(id, that.id) &&
        name.equals(that.name) &&
        Arrays.equals(propertyTypes, that.propertyTypes);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(id, name);
      result = 31 * result + Arrays.hashCode(propertyTypes);
      return result;
    }

    @Override
    public String toString() {
      return "TestTypeDeclaration{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", propertyTypes=" + Arrays.toString(propertyTypes) +
        '}';
    }
  }
}
