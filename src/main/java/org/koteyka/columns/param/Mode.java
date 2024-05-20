package org.koteyka.columns.param;

import java.util.Arrays;
import java.util.List;

public enum Mode {

    FOUR (
            new BorderConf(36, 80, 360),
            8,
            5
    );

    private final BorderConf border;
    private final int countDownItem;
    private final int columnHeight;

    Mode(BorderConf border, int countDownItem, int columnHeight) {
        this.border = border;
        this.countDownItem = countDownItem;
        this.columnHeight = columnHeight;
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
