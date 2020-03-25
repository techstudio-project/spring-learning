package com.techstudio.sakila.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.sql.Blob;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "staff_id", type = IdType.AUTO)
    private Integer staffId;

    private String firstName;

    private String lastName;

    private Integer addressId;

    private Blob picture;

    private String email;

    private Integer storeId;

    private Boolean active;

    private String username;

    private String password;

    private LocalDateTime lastUpdate;


    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Staff{" +
        "staffId=" + staffId +
        ", firstName=" + firstName +
        ", lastName=" + lastName +
        ", addressId=" + addressId +
        ", picture=" + picture +
        ", email=" + email +
        ", storeId=" + storeId +
        ", active=" + active +
        ", username=" + username +
        ", password=" + password +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
