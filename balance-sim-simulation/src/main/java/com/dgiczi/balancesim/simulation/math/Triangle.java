package com.dgiczi.balancesim.simulation.math;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

public class Triangle {

    private final double a;
    private final double b;
    private final double c;
    private final double degA;
    private final double degB;
    private final double degC;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.degA = calculateDegreeA(a, b, c);
        this.degB = calculateDegreeB(a, b, c);
        this.degC = calculateDegreeC(a, b, c);
    }

    private double calculateDegreeA(double a, double b, double c) {
        double numerator = Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2);
        double denominator = 2 * b * c;
        double cos = numerator / denominator;
        return Math.toDegrees(Math.acos(cos));
    }

    private double calculateDegreeB(double a, double b, double c) {
        double numerator = Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2);
        double denominator = 2 * a * c;
        double cos = numerator / denominator;
        return Math.toDegrees(Math.acos(cos));
    }

    private double calculateDegreeC(double a, double b, double c) {
        double numerator = Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2);
        double denominator = 2 * a * b;
        double cos = numerator / denominator;
        return Math.toDegrees(Math.acos(cos));
    }

    public static Triangle fromSides(double a, double b, double c) {
        return new Triangle(a, b, c);
    }

    public static Triangle fromPointsAndDegree(double a, double b, double degAB) {
        double sqC = Math.pow(b, 2) + Math.pow(a, 2) - 2 * a * b * Math.cos(Math.toRadians(degAB));
        double c = Math.sqrt(sqC);
        return new Triangle(a, b, c);
    }

    public static Triangle rightFromAdjacentAndOppositeSide(double a, double b) {
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return new Triangle(a, b, c);
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getDegA() {
        return degA;
    }

    public double getDegB() {
        return degB;
    }

    public double getDegC() {
        return degC;
    }
}
