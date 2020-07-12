package com.github.gibmir.ion.api.server.serialization;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import org.junit.jupiter.api.Test;

class SerializationUtilsTest {

    @Test
    void testExtractRequestFrom() {
      RequestDto requestDto = RequestDto.positional("id", "methodName", new Object[]{1, 2, 3});
    }
}
