package com.dgiczi.balancesim.render.com;

import com.dgiczi.balancesim.render.model.SceneParams;
import com.dgiczi.balancesim.render.model.State;
import com.dgiczi.balancesim.render.model.TickType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public class SceneRenderer {

    private static final Paint BACKGROUND_COLOR = Color.gray(0.3);
    private static final Paint FONT_COLOR = Color.WHITE;
    private static final Paint BODY_COLOR = Color.GOLD;
    private static final Paint OUTER_WHEEL_COLOR = Color.gray(0.15);
    private static final Paint INNER_WHEEL_COLOR = Color.BLACK;
    private static final Paint TYRE_WHEEL_COLOR = Color.BLACK;
    private static final Paint ROAD_LINE_COLOR = Color.gray(0.45);
    private static final int TICK_GAP = 10;
    private static final int DISTANCE_TEXT_OFFSET = 10;
    private static final int SCALE_OFFSET = 40;
    private static final int ROAD_OFFSET = 45;
    private static final double SCALE_LINE_WIDTH = 0.5;
    private static final double ROAD_LINE_WIDTH = 0.5;

    public void render(GraphicsContext gc, SceneParams params, State state) {
        drawBackGround(gc);

        drawRoad(gc, params, state);
        drawDistanceScale(gc, params, state);
        drawBody(gc, params, state);
        drawWheel(gc, params, state);
    }

    private void drawBackGround(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, width, height);
        gc.setLineWidth(SCALE_LINE_WIDTH);
        gc.setStroke(Color.BLACK);
    }

    private void drawRoad(GraphicsContext gc, SceneParams params, State state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.setFill(ROAD_LINE_COLOR);
        gc.setStroke(ROAD_LINE_COLOR);
        gc.setLineWidth(ROAD_LINE_WIDTH);
        gc.strokeLine(0, height - ROAD_OFFSET, width, height - ROAD_OFFSET);
    }

    private void drawWheel(GraphicsContext gc, SceneParams params, State state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        int radius = (int) Math.round(params.getWheelRadius() / params.getMmPerPx());
        int innerRadius = radius / 10;
        gc.setFill(OUTER_WHEEL_COLOR);
        gc.fillArc(width / 2 - radius, height - ROAD_OFFSET - 2 * radius,
                2 * radius, 2 * radius, 0, 360, ArcType.OPEN);
        gc.setFill(INNER_WHEEL_COLOR);
        gc.fillArc(width / 2 - innerRadius, height - ROAD_OFFSET - innerRadius - radius,
                2 * innerRadius, 2 * innerRadius, 0, 360, ArcType.ROUND);
        gc.setStroke(TYRE_WHEEL_COLOR);
        gc.setLineWidth(5);
        double wheelDegree = getWheelDegree(params, state);
        gc.strokeArc(width / 2 - radius, height - ROAD_OFFSET - 2 * radius,
                2 * radius, 2 * radius, 360 + wheelDegree, 720 + wheelDegree, ArcType.ROUND);
    }

    private double getWheelDegree(SceneParams params, State state) {
        double perimeter = 2 * params.getWheelRadius() * Math.PI;
        return (state.getDisatance() % perimeter) / perimeter * -360;
    }

    private void drawBody(GraphicsContext gc, SceneParams params, State state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        int wheelRadius = (int) Math.round(params.getWheelRadius() / params.getMmPerPx());
        double bodyWidth = params.getWidth() / params.getMmPerPx();
        double bodyHeight = params.getHeight() / params.getMmPerPx();

        Affine affine = new Affine();
        affine.appendRotation(state.getTilt(), width / 2, height - ROAD_OFFSET - wheelRadius);
        gc.setFill(BODY_COLOR);
        gc.setTransform(affine);
        gc.fillRect(width / 2 - bodyWidth / 2, height - ROAD_OFFSET - wheelRadius - bodyHeight, bodyWidth, bodyHeight);
        gc.setTransform(new Affine());
    }

    private void drawDistanceScale(GraphicsContext gc, SceneParams params, State state) {
        double mmPerPx = params.getMmPerPx();
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        int scaleStartInPx = (int) Math.round(state.getDisatance() / mmPerPx - width / 2);
        int minorTickOffset = scaleStartInPx % TICK_GAP;
        int startTickCount = scaleStartInPx / TICK_GAP;
        int tickCountToDraw = (int) (width - minorTickOffset) / TICK_GAP;
        for (int i = 0; i < tickCountToDraw; i++) {
            int tickPosition = minorTickOffset + i * TICK_GAP;
            TickType tickType = getTickType(startTickCount, i);
            gc.setStroke(tickType.getColor());
            gc.strokeLine(tickPosition, height - SCALE_OFFSET, tickPosition, height - SCALE_OFFSET + tickType.getHeight());
        }
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(FONT_COLOR);
        gc.fillText(state.getDisatance() + " mm", width / 2, height - DISTANCE_TEXT_OFFSET);
    }

    private TickType getTickType(int startTickCount, int i) {
        TickType tickType;
        if (startTickCount + i == 0) {
            tickType = TickType.ORIGIN;
        } else if ((startTickCount + i) % 5 == 0) {
            tickType = TickType.MAJOR;
        } else {
            tickType = TickType.MINOR;
        }
        return tickType;
    }

}
