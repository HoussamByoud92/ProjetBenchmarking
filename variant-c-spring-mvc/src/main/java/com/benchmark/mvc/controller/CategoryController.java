package com.benchmark.mvc.controller;

import com.benchmark.dto.CategoryDTO;
import com.benchmark.model.Category;
import com.benchmark.mvc.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository repository;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = repository.findAll(pageable);
        return ResponseEntity.ok(categories.map(this::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        return repository.findById(id)
                .map(cat -> ResponseEntity.ok(toDTO(cat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto) {
        Category cat = new Category();
        cat.setCode(dto.getCode());
        cat.setName(dto.getName());
        Category saved = repository.save(cat);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO dto) {
        return repository.findById(id)
                .map(cat -> {
                    cat.setCode(dto.getCode());
                    cat.setName(dto.getName());
                    Category updated = repository.save(cat);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CategoryDTO toDTO(Category cat) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(cat.getId());
        dto.setCode(cat.getCode());
        dto.setName(cat.getName());
        dto.setUpdatedAt(cat.getUpdatedAt());
        return dto;
    }
}
