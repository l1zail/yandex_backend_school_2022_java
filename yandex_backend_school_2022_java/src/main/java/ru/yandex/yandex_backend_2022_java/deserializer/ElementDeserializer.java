package ru.yandex.yandex_backend_2022_java.deserializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;

public class ElementDeserializer implements JsonDeserializer<ElementEntity> {

    @Override
    public ElementEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject elementNode = jsonElement.getAsJsonObject();
        ElementEntity elementEntity = new ElementEntity();
        elementEntity.setId(elementNode.get("id").getAsString());
        elementEntity.setType(ElementEntity.ElementType.valueOf(elementNode.get("type").getAsString()));
        if (elementEntity.getType() == ElementEntity.ElementType.FILE) {
            elementEntity.setSize(elementNode.get("size").getAsInt());
        } else {
            elementEntity.setSize(0);
        }
        if (elementNode.has("url")) {
            if (elementNode.get("url").isJsonNull()) {
                elementEntity.setUrl(null);
            } else {
                elementEntity.setUrl(elementNode.get("url").getAsString());
            }
        } else {
            elementEntity.setUrl(null);
        }
        if (elementNode.has("parentId")) {
            if (elementNode.get("parentId").isJsonNull()) {
                elementEntity.setParentId(null);
            } else {
                elementEntity.setParentId(elementNode.get("parentId").getAsString());
            }
        } else {
            elementEntity.setParentId(null);
        }
        return elementEntity;
    }

}
