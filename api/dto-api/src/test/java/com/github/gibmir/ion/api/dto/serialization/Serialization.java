package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.spi.JsonbProvider;

public class Serialization {
  @Test
  void smoke() {
    JsonObject jsonObject;
    SuccessResponse successResponse = new SuccessResponse("id","some result");
    JsonbBuilder jsonbBuilder = JsonbProvider.provider().create();
    Jsonb ser = jsonbBuilder.build();
    String json = ser.toJson(successResponse);
    Object fromJson = ser.fromJson(json, JsonObject.class);
    System.out.println(fromJson);
  }
}
