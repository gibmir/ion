package com.github.gibmir.ion.api.configuration.provider;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationProviderTest {

  @Test
  void testLoadException() {
    //there is no impl in API module
    assertThrows(NoSuchElementException.class, ConfigurationProvider::load);
  }
}
