package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.SchemaElement;

import java.util.Objects;

public class SchemaElementBean implements SchemaElement {
  private String id;
  private String name;
  private String description;

  public SchemaElementBean() {
  }

  public SchemaElementBean(String id, String name, String description) {
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

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SchemaElementBean that = (SchemaElementBean) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
