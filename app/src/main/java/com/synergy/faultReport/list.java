package com.synergy.faultReport;

import androidx.annotation.NonNull;

public class list {
    String name;
    int id;

    public list(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
