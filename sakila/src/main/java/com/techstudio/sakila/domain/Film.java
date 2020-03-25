package com.techstudio.sakila.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "film_id", type = IdType.AUTO)
    private Integer filmId;

    private String title;

    private String description;

    private LocalDateTime releaseYear;

    private Integer languageId;

    private Integer originalLanguageId;

    private Integer rentalDuration;

    private BigDecimal rentalRate;

    private Integer length;

    private BigDecimal replacementCost;

    private String rating;

    private String specialFeatures;

    private LocalDateTime lastUpdate;


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

    public LocalDateTime getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(LocalDateTime releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getOriginalLanguageId() {
        return originalLanguageId;
    }

    public void setOriginalLanguageId(Integer originalLanguageId) {
        this.originalLanguageId = originalLanguageId;
    }

    public Integer getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(Integer rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Film{" +
        "filmId=" + filmId +
        ", title=" + title +
        ", description=" + description +
        ", releaseYear=" + releaseYear +
        ", languageId=" + languageId +
        ", originalLanguageId=" + originalLanguageId +
        ", rentalDuration=" + rentalDuration +
        ", rentalRate=" + rentalRate +
        ", length=" + length +
        ", replacementCost=" + replacementCost +
        ", rating=" + rating +
        ", specialFeatures=" + specialFeatures +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
