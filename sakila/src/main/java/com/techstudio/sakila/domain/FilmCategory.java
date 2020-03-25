package com.techstudio.sakila.domain;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class FilmCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer filmId;

    private Integer categoryId;

    private LocalDateTime lastUpdate;


    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "FilmCategory{" +
        "filmId=" + filmId +
        ", categoryId=" + categoryId +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
