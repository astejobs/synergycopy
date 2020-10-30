package com.synergy.FaultReport;

import androidx.annotation.NonNull;

public class list {
    public String name;
    public int id;

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
