package com.dgiczi.balancesim.render;

import com.dgiczi.balancesim.render.model.RenderParams;
import com.dgiczi.balancesim.render.model.RenderState;
import com.dgiczi.balancesim.render.model.DistanceGridTickType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class SceneRenderer {
    //Colors
    private static final Paint BACKGROUND_COLOR = Color.gray(0.3);
    private static final Paint FONT_COLOR = Color.WHITE;
    private static final Paint BODY_COLOR = Color.GOLD;
    private static final Paint OUTER_WHEEL_COLOR = Color.gray(0.15);
    private static final Paint INNER_WHEEL_COLOR = Color.BLACK;
    private static final Paint TYRE_WHEEL_COLOR = Color.BLACK;
    private static final Paint ROAD_LINE_COLOR = Color.gray(0.65);
    private static final Paint GRID_COLOR = Color.gray(0.45);
    private static final Paint GRID_ORIGIN_COLOR = Color.gray(0.6);
    //Positions
    private static final int DISTANCE_TEXT_OFFSET = 10;
    private static final int SCALE_OFFSET = 40;
    private static final int ROAD_OFFSET = 45;
    //Sizes
    private static final double GRID_LINE_WIDTH = 0.5;
    private static final double SCALE_LINE_WIDTH = 0.5;
    private static final double ROAD_LINE_WIDTH = 0.5;
    private static final int GRID_STEP = 10; //mm
    //Formatting
    private static final NumberFormat numberFormat = createNumberFormat();

    private static NumberFormat createNumberFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat;
    }

    public void render(GraphicsContext gc, RenderParams params, RenderState state) {
        drawBackGround(gc);
        drawGrid(gc, params, state);
        drawRoad(gc, params, state);
        drawDistanceScale(gc, params, state);
        drawBody(gc, params, state);
        drawWheel(gc, params, state);
        drawStatusText(gc, state);
    }

    private void drawBackGround(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, width, height);
        gc.setLineWidth(SCALE_LINE_WIDTH);
        gc.setStroke(Color.BLACK);
    }

    private void drawGrid(GraphicsContext gc, RenderParams params, RenderState state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        double mmPerPx = params.getMmPerPx();
        double stepPx = GRID_STEP / mmPerPx;

        drawVerticalGridLines(gc, state, width, height, mmPerPx, stepPx);
        drawHorizontalGridLines(gc, width, height, stepPx);
    }

    private void drawHorizontalGridLines(GraphicsContext gc, double width, double height, double step) {
        int countToDraw = (int) ((height - ROAD_OFFSET) / step);
        for (int i = 0; i < countToDraw; i++) {
            double y = height - ROAD_OFFSET - (i + 1) * step;
            gc.strokeLine(0, y, width, y);
        }
    }

    private void drawVerticalGridLines(GraphicsContext gc, RenderState state, double width, double height, double mmPerPx, double stepPx) {
        int gridStartInPx = (int) Math.round(state.getPosX() / mmPerPx - width / 2);
        int offset = (Math.abs((int) Math.abs(stepPx - gridStartInPx))) % (int) stepPx;
        int startTickCount = (int) (gridStartInPx / stepPx);
        int countToDraw = (int) ((width + offset) / stepPx);

        gc.setLineWidth(GRID_LINE_WIDTH);

        for (int i = 0; i < countToDraw; i++) {
            if (startTickCount + i == 0) {
                gc.setStroke(GRID_ORIGIN_COLOR);
            } else {
                gc.setStroke(GRID_COLOR);
            }
            double currentX = offset + i * stepPx;
            gc.strokeLine(currentX, 0, currentX, height - ROAD_OFFSET);
        }
    }

    private void drawRoad(GraphicsContext gc, RenderParams params, RenderState state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.setFill(ROAD_LINE_COLOR);
        gc.setStroke(ROAD_LINE_COLOR);
        gc.setLineWidth(ROAD_LINE_WIDTH);
        gc.strokeLine(0, height - ROAD_OFFSET, width, height - ROAD_OFFSET);
    }

    private void drawWheel(GraphicsContext gc, RenderParams params, RenderState state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        int radius = (int) Math.round(params.getWheelRadius() / params.getMmPerPx());
        int innerRadius = radius / 10;
        gc.setFill(OUTER_WHEEL_COLOR);
        double wheelPosYPx = state.getPosY() / params.getMmPerPx();
        gc.fillArc(width / 2 - radius, height - ROAD_OFFSET - 2 * radius - wheelPosYPx,
                2 * radius, 2 * radius, 0, 360, ArcType.OPEN);
        gc.setFill(INNER_WHEEL_COLOR);
        gc.fillArc(width / 2 - innerRadius, height - ROAD_OFFSET - innerRadius - radius - wheelPosYPx,
                2 * innerRadius, 2 * innerRadius, 0, 360, ArcType.ROUND);
        gc.setStroke(TYRE_WHEEL_COLOR);
        gc.setLineWidth(5);
        double wheelDegree = getWheelDegree(params, state);
        gc.strokeArc(width / 2 - radius, height - ROAD_OFFSET - 2 * radius - wheelPosYPx,
                2 * radius, 2 * radius, 360 + wheelDegree, 720 + wheelDegree, ArcType.ROUND);
    }

    private double getWheelDegree(RenderParams params, RenderState state) {
        double perimeter = 2 * params.getWheelRadius() * Math.PI;
        return (state.getPosX() % perimeter) / perimeter * -360;
    }

    private void drawBody(GraphicsContext gc, RenderParams params, RenderState state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        int wheelRadiusPx = (int) Math.round(params.getWheelRadius() / params.getMmPerPx());
        double bodyWidthPx = params.getBodyWidth() / params.getMmPerPx();
        double bodyHeightPx = params.getBodyHeight() / params.getMmPerPx();

        Affine affine = new Affine();
        double wheelPosYPx = state.getPosY() / params.getMmPerPx();
        affine.appendRotation(-state.getTilt(), width / 2, height - ROAD_OFFSET - wheelRadiusPx - wheelPosYPx);
        gc.setFill(BODY_COLOR);
        gc.setTransform(affine);
        gc.fillRect(width / 2 - bodyWidthPx / 2, height - ROAD_OFFSET - wheelRadiusPx - bodyHeightPx - wheelPosYPx,
                bodyWidthPx, bodyHeightPx);
        gc.setTransform(new Affine());
    }

    private void drawDistanceScale(GraphicsContext gc, RenderParams params, RenderState state) {
        double mmPerPx = params.getMmPerPx();
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        double tickStepPx = GRID_STEP / mmPerPx;
        int scaleStartInPx = (int) Math.round(state.getPosX() / mmPerPx - width / 2);
        int minorTickOffset = (Math.abs((int) tickStepPx - scaleStartInPx)) % (int) tickStepPx;
        int startTickCount = (int) (scaleStartInPx / tickStepPx);
        int tickCountToDraw = (int) ((width + minorTickOffset) / tickStepPx);
        for (int i = 0; i < tickCountToDraw; i++) {
            double tickPosition = minorTickOffset + i * tickStepPx;
            DistanceGridTickType tickType = getTickType(startTickCount, i);
            gc.setStroke(tickType.getColor());
            gc.strokeLine(tickPosition, height - SCALE_OFFSET, tickPosition, height - SCALE_OFFSET + tickType.getHeight());
        }
    }

    private void drawStatusText(GraphicsContext gc, RenderState state) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(FONT_COLOR);
        gc.fillText(buildStatusText(state), width / 2, height - DISTANCE_TEXT_OFFSET);
    }

    private String buildStatusText(RenderState state) {
        StringBuilder sb = new StringBuilder();
        sb.append(numberFormat.format(state.getPosX()));
        sb.append(" mm");
        sb.append(", ");
        sb.append(numberFormat.format(state.getTilt()));
        sb.append(" deg");
        return sb.toString();
    }

    private DistanceGridTickType getTickType(int startTickCount, int i) {
        DistanceGridTickType tickType;
        if (startTickCount + i == 0) {
            tickType = DistanceGridTickType.ORIGIN;
        } else if ((startTickCount + i) % 5 == 0) {
            tickType = DistanceGridTickType.MAJOR;
        } else {
            tickType = DistanceGridTickType.MINOR;
        }
        return tickType;
    }

}
