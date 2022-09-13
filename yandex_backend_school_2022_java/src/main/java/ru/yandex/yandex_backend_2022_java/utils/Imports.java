package ru.yandex.yandex_backend_2022_java.utils;

import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import java.time.LocalDateTime;
import java.util.List;

public class Imports {
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<ElementEntity> getItems() {
        return items;
    }

    public void setItems(List<ElementEntity> items) {
        this.items = items;
    }

    private LocalDateTime updateTime;

    private List<ElementEntity> items;

}
