package ru.yandex.yandex_backend_2022_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import ru.yandex.yandex_backend_2022_java.repository.ElementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("singleton")
public class ElementService {
    private ElementRepository elementRepository;

    private ElementService(@Autowired ElementRepository elementRepository){
        this.elementRepository = elementRepository;
    }
    public ElementEntity findById(String id) {
        return elementRepository.findById(id).orElse(null);
    }

    public boolean existsById(String id) {
        return elementRepository.existsById(id);
    }

    public ElementEntity findByIdAllEntity(String id) {
        ElementEntity elementEntity = findById(id);
        if (elementEntity != null && elementEntity.getType() == ElementEntity.ElementType.FOLDER) {
            elementEntity.setChildren(new ArrayList<>());
            for (var child : findAllByParentId(id)) {
                elementEntity.getChildren().add(findByIdAllEntity(child.getId()));
            }
        }
        return elementEntity;
    }

    public List<ElementEntity> findAllByParentId(String parentId) {
        List<ElementEntity> children = new ArrayList<>();
        for (var file : elementRepository.findAllByParentId(parentId)) {
            children.add(file);
        }
        return children;
    }

    public List<ElementEntity> findFileByTypeAndDateBetween(ElementEntity.ElementType type, LocalDateTime start, LocalDateTime end) {
        List<ElementEntity> result = new ArrayList<>();
        for (var file : elementRepository.findFileByTypeAndDateBetween(type, start, end)) {
            result.add(file);
        }
        return result;
    }

    private void save(ElementEntity elementEntity) {
        elementRepository.save(elementEntity);
    }

    private void saveNewFolder(ElementEntity folder) {
        List<ElementEntity> childrenThatMayExist = findAllByParentId(folder.getId());
        for (var child : childrenThatMayExist) {
            folder.setSize(folder.getSize() + child.getSize());
        }
        save(folder);
        if (folder.getParentId() == null || existsById(folder.getParentId()) != true) {
            return;
        }
        ElementEntity parent = findById(folder.getParentId());
        while (true) {
            parent.setSize(folder.getSize() + parent.getSize());
            parent.setDate(folder.getDate());
            save(parent);
            if (parent.getParentId() == null || existsById(parent.getParentId())!= true) {
                break;
            }
            parent = findById(parent.getParentId());
        }
    }

    private void saveNewFile(ElementEntity file) {
        save(file);
        if (file.getParentId() == null || existsById(file.getParentId())!= true) {
            return;
        }
        ElementEntity parent = findById(file.getParentId());
        while (true) {
            parent.setSize(parent.getSize() + 1);
            parent.setDate(file.getDate());
            save(parent);
            if (parent.getParentId() == null || existsById(parent.getParentId()) != true) {
                break;
            }
            parent = findById(parent.getParentId());
        }
    }

    public void saveWithUpdateTree(ElementEntity elementEntity) {
        if (elementEntity.getType() == ElementEntity.ElementType.FOLDER) {
            if (existsById(elementEntity.getId())) {
                updateFolder(elementEntity);
            } else {
                saveNewFolder(elementEntity);
            }
        } else {
            if (existsById(elementEntity.getId())) {
                updateFile(elementEntity);
            } else {
                saveNewFile(elementEntity);
            }
        }
    }

    private void updateFile(ElementEntity file) {
        ElementEntity oldFile = findById(file.getId());
        if (file.getParentId() != oldFile.getParentId()) {
            if (oldFile.getParentId() == null) {
                save(file);
                if (existsById(file.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(file.getParentId()), 1, file.getDate());
                }
            } else {
                save(file);
                if (existsById(oldFile.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(oldFile.getParentId()), -1, file.getDate());
                }
                if (file.getParentId() != null && existsById(file.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(file.getParentId()), 1, file.getDate());
                }
            }
        }
        else {
            save(file);
        }
    }
    private void updateSizeDateUpToTheRootFrom(ElementEntity start, int sizeDelta, LocalDateTime newDate) {
        while (true) {
            if (newDate != null) {
                start.setDate(newDate);
            }
            start.setSize(start.getSize() + sizeDelta);
            save(start);
            if (start.getParentId() == null || !existsById(start.getParentId())) {
                break;
            }
            start = findById(start.getParentId());
        }
    }


    private void updateFolder(ElementEntity folder) {
        ElementEntity oldFolder = findById(folder.getId());
        folder.setSize(oldFolder.getSize());
        if (folder.getParentId() != oldFolder.getParentId()) {
            if (oldFolder.getParentId() == null) {
                save(folder);
                if (existsById(folder.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(folder.getParentId()), folder.getSize(), folder.getDate());
                }
            } else {
                save(folder);
                if (existsById(oldFolder.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(oldFolder.getParentId()), -folder.getSize(), folder.getDate());
                }
                if (folder.getParentId() != null && existsById(folder.getParentId())) {
                    updateSizeDateUpToTheRootFrom(findById(folder.getParentId()), folder.getSize(), folder.getDate());
                }
            }
        } else {
            save(folder);
        }
    }


    public void deleteById(String id) {
        if (findById(id).getType() == ElementEntity.ElementType.FILE) {
            deleteFile(id);
        } else {
            deleteFolder(id);
        }
    }

    private void deleteFolder(String folderId) {
        ElementEntity folder = findById(folderId);
        deleteSubTree(folder);
        if (folder.getParentId() == null || existsById(folder.getParentId()) != true) {
            return;
        }
        updateSizeDateUpToTheRootFrom(findById(folder.getParentId()), -folder.getSize(), null);
    }

    private void deleteFile(String fileId) {
        ElementEntity file = findById(fileId);
        elementRepository.deleteById(fileId);
        if (file.getParentId() == null || existsById(file.getParentId()) != true) {
            return;
        }
        updateSizeDateUpToTheRootFrom(findById(file.getParentId()), -1, null);
    }

    private void deleteSubTree(ElementEntity elementEntity) {
        if (elementEntity.getType() != ElementEntity.ElementType.FILE) {
            for (var child : findAllByParentId(elementEntity.getId())) {
                deleteSubTree(child);
            }
        }
        elementRepository.deleteById(elementEntity.getId());
    }

}
