package com.busybees.little_bee.data.vos.Search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchVO {
    @SerializedName("search")
    @Expose
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
