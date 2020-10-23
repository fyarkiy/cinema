package com.cinema.model.dto;

public class MovieRequestDto {
    private String title;
    private String description;

    public MovieRequestDto() {
    }

    public MovieRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
