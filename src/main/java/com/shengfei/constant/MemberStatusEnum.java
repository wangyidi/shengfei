package com.shengfei.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberStatusEnum {

    WAITE_CHECK(1,"待初审"),
    FINAL_CHECK(2,"待终审"),
    SUCCESS(3,"已通过"),
    RETURN_INIT_CHECK(4,"退回"),
    REJECT(5,"拒绝");


    private Integer id;
    private String value;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
