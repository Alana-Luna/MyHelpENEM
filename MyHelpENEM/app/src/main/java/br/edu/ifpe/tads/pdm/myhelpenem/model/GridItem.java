package br.edu.ifpe.tads.pdm.myhelpenem.model;

import android.graphics.drawable.Drawable;

public class GridItem {
    private Drawable drawableId;
    private String name;

    public GridItem() {
    }

    public GridItem(Drawable drawableId, String name) {
        this.drawableId = drawableId;
        this.name = name;
    }

    public Drawable getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(Drawable drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
