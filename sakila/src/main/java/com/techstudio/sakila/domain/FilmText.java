package com.techstudio.sakila.domain;

import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class FilmText implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer filmId;

    private String title;

    private String description;


    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FilmText{" +
        "filmId=" + filmId +
        ", title=" + title +
        ", description=" + description +
        "}";
    }
}
