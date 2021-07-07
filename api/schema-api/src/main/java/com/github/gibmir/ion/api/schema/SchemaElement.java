package com.github.gibmir.ion.api.schema;

public interface SchemaElement {
  /**
   * @return element id
   */
  String getId();

  /**
   * @return element name
   */
  String getName();

  /**
   * @return element description
   */
  String getDescription();
}
