package ru.yandex.yandex_backend_2022_java.serializer;

import com.google.gson.*;
import org.springframework.stereotype.Component;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import ru.yandex.yandex_backend_2022_java.utils.GsonUtils;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

@Component
public class ElementSerializer implements JsonSerializer<ElementEntity> {

    @Override
    public JsonElement serialize(ElementEntity elementEntity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", elementEntity.getId());
        jsonObject.addProperty("url", elementEntity.getUrl());
        jsonObject.addProperty("size", elementEntity.getSize());
        jsonObject.addProperty("type", elementEntity.getType().toString());
        if (elementEntity.getParentId() == null) {
            jsonObject.add("parentId", JsonNull.INSTANCE);
        } else {
            jsonObject.addProperty("parentId", elementEntity.getParentId());
        }
        jsonObject.addProperty("date", elementEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        if (elementEntity.getType() == ElementEntity.ElementType.FILE) {
            jsonObject.add("children", JsonNull.INSTANCE);
        } else {
            JsonArray childrenArray = new JsonArray();
            for (var child : elementEntity.getChildren()) {
                childrenArray.add(GsonUtils.gson.toJsonTree(child));
            }
            jsonObject.add("children", childrenArray);
        }
        return jsonObject;
    }
}
