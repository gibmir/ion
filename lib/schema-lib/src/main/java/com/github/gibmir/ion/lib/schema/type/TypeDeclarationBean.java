package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

import java.util.Arrays;
import java.util.Collection;

public class TypeDeclarationBean extends SchemaElementBean implements TypeDeclaration {
  private PropertyType[] propertyTypes;
  private TypeParameter[] typeParameters;

  public TypeDeclarationBean() {
    super();
  }


  public TypeDeclarationBean(String id, String name, String description, Collection<PropertyType> propertyTypes, Collection<TypeParameter> typeParameters) {
    super(id, name, description);
    this.propertyTypes = propertyTypes.toArray(new PropertyType[0]);
    this.typeParameters = typeParameters.toArray(new TypeParameter[0]);
  }

  @Override
  public PropertyType[] getPropertyTypes() {
    return propertyTypes;
  }

  @Override
  public TypeParameter[] getParameters() {
    return typeParameters;
  }

  public void setPropertyTypes(PropertyType... propertyTypes) {
    this.propertyTypes = propertyTypes;
  }

  @Override
  public String toString() {
    return "TypeDeclarationBean{" +
      "propertyTypes=" + Arrays.toString(propertyTypes) +
      '}';
  }
}
