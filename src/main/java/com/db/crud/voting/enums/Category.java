package com.db.crud.voting.enums;

public enum Category {
    TECHNOLOGY("T"), OPINION("O"), SPORTS("S"), PROGRAMMING("P");

    private String code;

    private Category(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
