package com.techstudio.sakila.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "city_id", type = IdType.AUTO)
    private Integer cityId;

    private String city;

    private Integer countryId;

    private LocalDateTime lastUpdate;


    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "City{" +
        "cityId=" + cityId +
        ", city=" + city +
        ", countryId=" + countryId +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
