package ru.yandex.yandex_backend_2022_java.repository;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;

import java.time.LocalDateTime;

public interface ElementRepository extends CrudRepository<ElementEntity, String> {
    Iterable<ElementEntity> findAllByParentId(String parentId);
    Iterable<ElementEntity> findFileByTypeAndDateBetween(ElementEntity.ElementType type, LocalDateTime start, LocalDateTime stop);
}
