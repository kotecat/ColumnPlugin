package org.koteyka.columns.param;

import java.util.Arrays;
import java.util.List;

public enum Mode {

    FOUR (
            Arrays.asList(
                    new Cords(-13.5, 50, 0.5, -90),
                    new Cords(13.5, 50, 0.5, 90),
                    new Cords(0.5, 50, -13.5, 0),
                    new Cords(0.5, 50, 13.5, 180)
            ),
            new BorderConf(35, 60, 450),
            9,
            7
    ),
    EIGHT (
            Arrays.asList(
                    new Cords(-6.5, 50, 15.5, -160),
                    new Cords(7.5, 50, 15.5, 160),
                    new Cords(15.5, 50, 7.5, 113),
                    new Cords(15.5, 50, -6.5, 70),
                    new Cords(7.5, 50, -15.5, 30),
                    new Cords(-6.5, 50, -15.5, -30),
                    new Cords(-15.5, 50, -6.5, -62),
                    new Cords(-15.5, 50, 7.5, -113)
            ),
            new BorderConf(39, 60, 500),
            9,
            8
    );

    private final List<Cords> cords;
    private final BorderConf border;
    private final int countDownItem;
    private final int columnHeight;

    Mode(List<Cords> cords, BorderConf border, int countDownItem, int columnHeight) {
        this.cords = cords;
        this.border = border;
        this.countDownItem = countDownItem;
        this.columnHeight = columnHeight;
    }

    public List<Cords> getCords() {
        return cords;
    }

    public BorderConf getBorder() {
        return border;
    }

    public int getCountDownItem() {
        return countDownItem * 20;
    }

    public int getColumnHeight() {
        return columnHeight;
    }
}
