package com.codingbottle.calendar.domain.todo.entity;


import com.fasterxml.jackson.annotation.JsonValue;

public enum TagColor {
    BLUSH_PINK("#FCE3E3"),
    CREAMY_PEACH("#FCEFDA"),
    VANILLA_CREAM("#FCFAD7"),
    MINT_GREEN("#D8F1E2"),
    LIGHT_KHAKI("#E6F8D0"),
    BABY_PINK("#DBF0F5"),
    AQUA_GREEN("#D4F5F3"),
    LAVENDER_BLUSH("#E4EBF8"),
    SALMON_PINK("#FBE8F1"),
    LAVENDER_GRAY("#EEE8F8")
    ;

    @JsonValue
    private final String colorCode;

    TagColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}

