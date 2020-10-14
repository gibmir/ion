package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class TypeDeclarationBean extends SchemaElementBean implements TypeDeclaration {
  private PropertyType[] propertyTypes;

  public TypeDeclarationBean() {
    super();
  }

  public TypeDeclarationBean(String id, String name, String description, PropertyType[] propertyTypes) {
    super(id, name, description);
    this.propertyTypes = propertyTypes;
  }

  public TypeDeclarationBean(String id, String name, String description, Collection<PropertyType> propertyTypes) {
    super(id, name, description);
    this.propertyTypes = propertyTypes.toArray(new PropertyType[0]);
  }

  @Override
  public PropertyType[] getPropertyTypes() {
    return propertyTypes;
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
