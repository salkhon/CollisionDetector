package com.circle.model;

public class MyCircle {
    private double x, y;
    private double radius;

    public MyCircle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public boolean intersects(MyCircle that) {
        double distSq = (this.x - that.x) * (this.x - that.x) + (this.y - that.y) * (this.y - that.y);
        boolean intersects = false;
        if (distSq < (this.radius + that.radius) * (this.radius + that.radius)) {
            intersects = true;
        }
        return intersects;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
