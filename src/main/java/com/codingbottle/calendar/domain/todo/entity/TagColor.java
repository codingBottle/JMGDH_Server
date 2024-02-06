package com.codingbottle.calendar.domain.todo.entity;


public enum TagColor {
    COLOR1("#FCE3E3"),
    COLOR2("#FCEFDA"),
    COLOR3("#FCFAD7"),
    COLOR4("#D8F1E2"),
    COLOR5("#E6F8D0"),
    COLOR6("#EEE8F8");

    private final String color;

    TagColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

