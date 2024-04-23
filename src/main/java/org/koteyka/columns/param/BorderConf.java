package org.koteyka.columns.param;

public class BorderConf {
    private final int startBorderSize;
    private final int timeToWait;
    private final int timeToEnd;
    private final double centerX = 0.5;
    private final double centerZ = 0.5;
    private final double endX = 1000.0;
    private final double endZ = 1000.0;
    private final double endDamageBuffer = 0.2;
    private final double endDamageAmount = 0.003;

    public BorderConf(int startBorderSize, int timeToWait, int timeToEnd) {
        this.startBorderSize = startBorderSize;
        this.timeToWait = timeToWait;
        this.timeToEnd = timeToEnd;
    }

    public int getStartBorderSize() {
        return startBorderSize;
    }

    public int getTimeToWait() {
        return timeToWait;
    }

    public int getTimeToEnd() {
        return timeToEnd;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndZ() {
        return endZ;
    }

    public double getEndDamageBuffer() {
        return endDamageBuffer;
    }

    public double getEndDamageAmount() {
        return endDamageAmount;
    }
}
