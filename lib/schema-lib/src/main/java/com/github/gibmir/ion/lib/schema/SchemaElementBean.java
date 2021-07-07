package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.SchemaElement;

import java.util.Objects;

public class SchemaElementBean implements SchemaElement {
  protected String id;
  protected String name;
  protected String description;

  public SchemaElementBean() {
  }

  public SchemaElementBean(final String id, final String name, final String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  @Override
  public final String getId() {
    return id;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final String getDescription() {
    return description;
  }

  /**
   * Sets id.
   *
   * @param id schema id
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * Sets schema name.
   *
   * @param name schema name
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Sets schema description.
   *
   * @param description schema description
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SchemaElementBean that = (SchemaElementBean) o;
    return Objects.equals(id, that.id)
      && Objects.equals(name, that.name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
