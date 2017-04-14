package com.dgiczi.balancesim.render.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum TickType {
    MINOR(Color.gray(0.8), 5), MAJOR(Color.gray(0.9), 10), ORIGIN(Color.gray(1), 20);

    private final Paint color;
    private final int height;

    TickType(Paint color, int height) {
        this.color = color;
        this.height = height;
    }

    public Paint getColor() {
        return color;
    }

    public int getHeight() {
        return height;
    }

}
