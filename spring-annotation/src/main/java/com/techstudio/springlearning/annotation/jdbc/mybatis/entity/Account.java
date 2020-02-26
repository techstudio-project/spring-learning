package com.techstudio.springlearning.annotation.jdbc.mybatis.entity;

/**
 * @author lj
 * @date 2020/2/25
 */
public class Account {

    private Integer id;

    private String username;

    private Long money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}
