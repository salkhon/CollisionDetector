package com.circle.model;

import java.util.*;

public class CollisionDetector {
    private MyCircle[] myCircles;

    private List<MyCircle>[][] circlePool;

    // cached values
    private int div;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    @SuppressWarnings("unchecked")
    public void set(MyCircle[] myCircles) {
        this.myCircles = myCircles;
        this.div = (int) Math.sqrt(myCircles.length);

        this.circlePool = new List[this.div][this.div];
        for (int i = 0; i < circlePool.length; i++) {
            for (int j = 0; j < circlePool[i].length; j++) {
                this.circlePool[i][j] = new LinkedList<>();
            }
        }

        this.minX = getMinX();
        this.maxX = getMaxX();

        this.minY = getMinY();
        this.maxY = getMaxY();

        for (MyCircle myCircle : myCircles) {
            circlePool[poolXFor(myCircle)][poolYFor(myCircle)].add(myCircle);
        }
    }

    private int poolXFor(MyCircle myCircle) {
        return poolXFor(myCircle.getX());
    }

    private int poolXFor(double x) {
        int poolX = (int) (((x - this.minX) / (this.maxX - this.minX)) * (this.div));
        if (poolX >= this.div) {
            poolX = this.div - 1;
        } else if (poolX < 0) {
            poolX = 0;
        }
        return poolX;
    }

    private int poolYFor(MyCircle myCircle) {
        return poolYFor(myCircle.getY());
    }

    private int poolYFor(double y) {
        int poolY = (int) (((y - this.minY) / (this.maxY - this.minY)) * (this.div));
        if (poolY >= this.div) {
            poolY = this.div - 1;
        } else if (poolY < 0) {
            poolY = 0;
        }
        return poolY;
    }

    private double getMinX() {
        double minX = -1;
        Optional<MyCircle> minXop = Arrays.stream(this.myCircles).min(Comparator.comparingDouble(MyCircle::getX));
        if (minXop.isPresent()) {
            minX = minXop.get().getX();
        }
        return minX;
    }

    private double getMaxX() {
        double maxX = -1;
        Optional<MyCircle> maxXop = Arrays.stream(this.myCircles).max(Comparator.comparingDouble(MyCircle::getX));
        if (maxXop.isPresent()) {
            maxX = maxXop.get().getX();
        }
        return maxX;
    }

    private double getMinY() {
        double minY = -1;
        Optional<MyCircle> minYop = Arrays.stream(this.myCircles).min(Comparator.comparingDouble(MyCircle::getY));
        if (minYop.isPresent()) {
            minY = minYop.get().getY();
        }
        return minY;
    }

    private double getMaxY() {
        double maxY = -1;
        Optional<MyCircle> maxYop = Arrays.stream(this.myCircles).max(Comparator.comparingDouble(MyCircle::getY));
        if (maxYop.isPresent()) {
            maxY = maxYop.get().getY();
        }
        return maxY;
    }

    public boolean collides() {
        boolean collides = false;
        for (MyCircle myCircle : this.myCircles) {
            if (collidesWithNeighbors(myCircle)) {
                collides = true;
                break;
            }
        }
        return collides;
    }

    private boolean collidesWithNeighbors(MyCircle myCircle) {
        boolean collides = false;
        int poolX = poolXFor(myCircle);
        int poolY = poolYFor(myCircle);

        if (checkCollision(myCircle, this.circlePool[poolX][poolY])) {
            collides = true;
        } else {
            int loX = poolXFor(myCircle.getX() - myCircle.getRadius() * 2);
            int hiX = poolXFor(myCircle.getX() + myCircle.getRadius() * 2);

            int loY = poolYFor(myCircle.getY() - myCircle.getRadius() * 2);
            int hiY = poolYFor(myCircle.getY() + myCircle.getRadius() * 2);

            for (int x = loX; x <= hiX; x++) {
                for (int y = loY; y <= hiY; y++) {
                    if ((x != poolX || y != poolY) &&
                            checkCollision(myCircle, this.circlePool[x][y])) {
                        collides = true;
                        break;
                    }
                }
            }
        }

        return collides;
    }

    private boolean checkCollision(MyCircle target, List<MyCircle> myCircles) {
        boolean collides = false;
        for (MyCircle myCircle : myCircles) {
            if (myCircle != target && target.intersects(myCircle)) {
                collides = true;
                break;
            }
        }
        return collides;
    }
}
