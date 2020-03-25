package com.techstudio.sakila.domain;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class FilmActor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer actorId;

    private Integer filmId;

    private LocalDateTime lastUpdate;


    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "FilmActor{" +
        "actorId=" + actorId +
        ", filmId=" + filmId +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
