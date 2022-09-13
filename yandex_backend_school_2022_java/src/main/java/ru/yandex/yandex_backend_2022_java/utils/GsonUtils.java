package ru.yandex.yandex_backend_2022_java.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.yandex_backend_2022_java.deserializer.ElementDeserializer;
import ru.yandex.yandex_backend_2022_java.deserializer.ImportsDeserializer;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import ru.yandex.yandex_backend_2022_java.serializer.ElementSerializer;

public class GsonUtils {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ElementEntity.class, new ElementSerializer())
            .registerTypeAdapter(ElementEntity.class, new ElementDeserializer())
            .registerTypeAdapter(Imports.class, new ImportsDeserializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();
}