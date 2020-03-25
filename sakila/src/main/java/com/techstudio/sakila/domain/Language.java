package com.techstudio.sakila.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "language_id", type = IdType.AUTO)
    private Integer languageId;

    private String name;

    private LocalDateTime lastUpdate;


    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Language{" +
        "languageId=" + languageId +
        ", name=" + name +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
