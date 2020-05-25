package com.lits.tovisitapp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ParseDataUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T prepareData(String path, TypeReference<T> valueTypeReference) throws IOException {
        InputStream inputStream = ParseDataUtils.class.getClassLoader().getResourceAsStream(path);
        return objectMapper.readValue(inputStream, valueTypeReference);
    }
}
