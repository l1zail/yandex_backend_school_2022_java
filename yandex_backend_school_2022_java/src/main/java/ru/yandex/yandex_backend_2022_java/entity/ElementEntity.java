package ru.yandex.yandex_backend_2022_java.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "elements")
public class ElementEntity {

    public enum ElementType {
        FILE,
        FOLDER
    }

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parentId", nullable = true)
    private String parentId;

    @Column(name = "type")
    private ElementType type;

    @Column(name = "size", nullable = true)
    private Integer size;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "updateDate")
    private LocalDateTime date;

    @Transient
    private List<ElementEntity> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ElementEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ElementEntity> children) {
        this.children = children;
    }
}
