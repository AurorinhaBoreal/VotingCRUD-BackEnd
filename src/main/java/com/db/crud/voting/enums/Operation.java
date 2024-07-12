package com.db.crud.voting.enums;

public enum Operation {
    CREATE("C");

    private String code;

    private Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
