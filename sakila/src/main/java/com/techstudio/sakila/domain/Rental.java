package com.techstudio.sakila.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lj
 * @since 2020-03-23
 */
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rental_id", type = IdType.AUTO)
    private Integer rentalId;

    private LocalDateTime rentalDate;

    private Integer inventoryId;

    private Integer customerId;

    private LocalDateTime returnDate;

    private Integer staffId;

    private LocalDateTime lastUpdate;


    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Rental{" +
        "rentalId=" + rentalId +
        ", rentalDate=" + rentalDate +
        ", inventoryId=" + inventoryId +
        ", customerId=" + customerId +
        ", returnDate=" + returnDate +
        ", staffId=" + staffId +
        ", lastUpdate=" + lastUpdate +
        "}";
    }
}
