package ru.yandex.yandex_backend_2022_java.controller;

import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandex_backend_2022_java.entity.ElementEntity;
import ru.yandex.yandex_backend_2022_java.service.ElementService;
import ru.yandex.yandex_backend_2022_java.utils.GsonUtils;
import ru.yandex.yandex_backend_2022_java.utils.Imports;
import ru.yandex.yandex_backend_2022_java.utils.ImportsUtils;
import ru.yandex.yandex_backend_2022_java.utils.UpdateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
public class NodesController {

    @Autowired
    private ElementService fileService;

    /**
     * GET endpoint for path "nodes/{id}".
     * @return information about element by id
     */
    @GetMapping(value = "nodes/{id}")
    public ResponseEntity<String> getNode(@PathVariable("id") String id){
        try {
            if (fileService.existsById(id)) {
                ElementEntity information = fileService.findByIdAllEntity(id);
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(GsonUtils.gson.toJson(information));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"code\": 404, \"message\": \"Item not found\"}");
            }
        } catch (Exception e) {
            return  ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"code\": 400, \"message\": \"Validation Failed\"}");
        }
    }

    /**
     * DELETE endpoint for path "delete/{id}".
     * @return delete element by id
     */
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteNode(@PathVariable("id") String id) {
        try {
            if (fileService.existsById(id)) {
                fileService.deleteById(id);
                return ResponseEntity
                        .ok()
                        .build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"code\": 404, \"message\": \"Item not found\"}");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"code\": 400, \"message\": \"Validation Failed\"}");
        }
    }

    /**
     * POST endpoint for path "imports".
     * Import element
     */
    @PostMapping(value = "imports", consumes = "application/json")
    public ResponseEntity<String> imports(@RequestBody String element) {
        if (ImportsUtils.isValidImports(element)) {
            Imports imports = GsonUtils.gson.fromJson(element, Imports.class);
            for (var importElement : imports.getItems()) {
                importElement.setDate(imports.getUpdateTime());
                fileService.saveWithUpdateTree(importElement);
            }
            return ResponseEntity
                    .ok()
                    .build();
        } else {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"code\": 400, \"message\": \"Validation Failed\"}");
        }
    }

    /**
     * GET endpoint for path "updates".
     * Get a list of files that have been updated in the last 24 hours
     */
    @GetMapping("updates")
    public ResponseEntity<String> getUpdates(@RequestParam(name = "date") String end) {
        try {
            LocalDateTime date = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
            List<ElementEntity> listFiles = fileService.findFileByTypeAndDateBetween(
                    ElementEntity.ElementType.FILE,
                    date.minusHours(24),
                    date
            );
            JsonElement updates = UpdateUtils.serializeUpdates(listFiles);
            return ResponseEntity
                    .ok()
                    .body(GsonUtils.gson.toJson(updates));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 400, \"message\": \"Validation Failed\"}");
        }
    }
}
