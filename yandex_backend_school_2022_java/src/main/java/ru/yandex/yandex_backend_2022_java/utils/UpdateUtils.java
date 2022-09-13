package ru.yandex.yandex_backend_2022_java.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;

import java.util.List;

public class UpdateUtils {
    public static JsonElement serializeUpdates(List<ElementEntity> files) {
        JsonObject jsonObject = new JsonObject();
        JsonArray items = new JsonArray();
        for (var file : files) {
            items.add(GsonUtils.gson.toJsonTree(file));
        }
        jsonObject.add("items", items);
        return jsonObject;
    }
}
