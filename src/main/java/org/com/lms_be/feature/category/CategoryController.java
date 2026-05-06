package org.com.lms_be.feature.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = Arrays.stream(Category.values())
                .map(c -> new CategoryResponseDTO(c.name(), c.getDisplayName()))
                .toList();
        return ResponseEntity.ok(categories);
    }
}
