package com.synergy.Search;

import androidx.annotation.NonNull;

public class StatusItem {
   private  String statusName;

    public StatusItem() {
    }

    public StatusItem(String statusName ) {
        this.statusName = statusName;

    }

    public String getStatus() {
        return statusName;
    }
    @NonNull
    @Override
    public String toString() {
        return statusName;
    }
}
