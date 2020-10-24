package com.example.childdraw.model;

public class ToolsItem {

    private int icone;
    private String name;

    public ToolsItem(int icone, String name) {
        this.icone = icone;
        this.name = name;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
