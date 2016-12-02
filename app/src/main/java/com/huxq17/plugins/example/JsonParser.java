package com.huxq17.plugins.example;

import com.google.gson.Gson;

public class JsonParser {
    private static Gson gson;

    private JsonParser() {
    }

    private static class JsonParserHolder {
        private static JsonParser jsonParser = new JsonParser();
        private static Gson gson = new Gson();
    }

    public static JsonParser instance() {
        gson = JsonParserHolder.gson;
        return JsonParserHolder.jsonParser;
    }

    public <T> T fromJson(String json, Class<T> beanClass) {
        T result = null;
        try {
            result = gson.fromJson(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}