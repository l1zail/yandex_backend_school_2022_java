package ru.yandex.yandex_backend_2022_java.deserializer;
import com.google.gson.*;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import ru.yandex.yandex_backend_2022_java.utils.GsonUtils;
import ru.yandex.yandex_backend_2022_java.utils.Imports;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImportsDeserializer implements JsonDeserializer<Imports>{

    @Override
    public Imports deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();
        Imports imports = new Imports();
        imports.setUpdateTime(LocalDateTime.parse(root.get("updateDate").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
        List<ElementEntity> entities = new ArrayList<>();
        for (JsonElement elementNode : root.get("items").getAsJsonArray()) {
            entities.add(GsonUtils.gson.fromJson(elementNode, ElementEntity.class));
        }
        imports.setItems(entities);
        return imports;
    }
}