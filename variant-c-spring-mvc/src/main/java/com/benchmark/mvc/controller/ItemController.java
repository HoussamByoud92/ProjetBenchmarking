package com.benchmark.mvc.controller;

import com.benchmark.dto.ItemDTO;
import com.benchmark.model.Item;
import com.benchmark.mvc.repository.ItemRepository;
import com.benchmark.mvc.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<Page<ItemDTO>> listItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findAll(pageable);
        return ResponseEntity.ok(items.map(this::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> ResponseEntity.ok(toDTO(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-category")
    public ResponseEntity<Page<ItemDTO>> getItemsByCategory(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(items.map(this::toDTO));
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO dto) {
        return categoryRepository.findById(dto.getCategoryId())
                .map(cat -> {
                    Item item = new Item();
                    item.setSku(dto.getSku());
                    item.setName(dto.getName());
                    item.setPrice(dto.getPrice());
                    item.setStock(dto.getStock());
                    item.setCategory(cat);
                    item.setDescription(dto.getDescription());
                    Item saved = itemRepository.save(item);
                    return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable Long id,
            @RequestBody ItemDTO dto) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setSku(dto.getSku());
                    item.setName(dto.getName());
                    item.setPrice(dto.getPrice());
                    item.setStock(dto.getStock());
                    item.setDescription(dto.getDescription());
                    Item updated = itemRepository.save(item);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        dto.setCategoryId(item.getCategory().getId());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setDescription(item.getDescription());
        return dto;
    }
}
