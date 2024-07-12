package com.db.crud.voting.enums;

public enum UserType {
    ADMIN("A"), COMMON("C");

    private String code;

    private UserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
