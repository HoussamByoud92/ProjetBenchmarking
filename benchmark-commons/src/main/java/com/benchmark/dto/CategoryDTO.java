package com.benchmark.dto;

import java.time.LocalDateTime;

public class CategoryDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime updatedAt;

    public CategoryDTO() {}

    public CategoryDTO(Long id, String code, String name, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
