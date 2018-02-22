package com.example.helpme.mvpandroid.widget;

import com.xiaopo.flying.puzzle.Line;
import com.xiaopo.flying.puzzle.straight.StraightPuzzleLayout;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
public class ImagePuzzleLayout extends StraightPuzzleLayout {
    
    private static final int mThemeCount = 7;
    protected int theme;
    private float[] scales;
    
    public ImagePuzzleLayout(int theme, float... scale) {
        this.theme = theme < mThemeCount ? theme : mThemeCount - 1;
        scales = scale;
    }
    
    public int getTheme() {
        return theme;
    }
    
    @Override
    public void layout() {
        switch (theme) {
            case 0:// 两张图片竖直方向  
                if (scales == null)
                    addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                else
                    addLine(0, Line.Direction.HORIZONTAL, scales[0]);
                break;
            case 1:// 三张1
                if (scales == null) {
                    addLine(0, Line.Direction.HORIZONTAL, 1f / 3);
                    addLine(1, Line.Direction.HORIZONTAL, 0.5f);
                } else {
                    addLine(0, Line.Direction.HORIZONTAL, scales[0]);
                    addLine(1, Line.Direction.HORIZONTAL, scales[1]);
                }
                break;
            case 2:// 三张2
                if (scales == null) {
                    addLine(0, Line.Direction.HORIZONTAL, 0.4f);
                    addLine(1, Line.Direction.VERTICAL, 0.5f);
                } else {
                    addLine(0, Line.Direction.HORIZONTAL, scales[0]);
                    addLine(1, Line.Direction.VERTICAL, scales[1]);
                }
                break;
            case 3:// 三张3
                if (scales == null) {
                    addLine(0, Line.Direction.VERTICAL, 0.4f);
                    addLine(1, Line.Direction.HORIZONTAL, 0.5f);
                } else {
                    addLine(0, Line.Direction.VERTICAL, scales[0]);
                    addLine(1, Line.Direction.HORIZONTAL, scales[1]);
                }
                break;
            case 4: // 四张1
                if (scales == null) {
                    addCross(0, 0.5f, 0.5f);
                } else {
                    addCross(0, scales[0], scales[1]);
                }
                break;
            case 5: // 四张2
                if (scales == null) {
                    addLine(0, Line.Direction.HORIZONTAL, 0.4f);
                    addLine(0, Line.Direction.VERTICAL, 0.5f);
                    addLine(2, Line.Direction.VERTICAL, 0.5f);
                } else {
                    addLine(0, Line.Direction.HORIZONTAL, scales[0]);
                    addLine(0, Line.Direction.VERTICAL, scales[1]);
                    addLine(2, Line.Direction.VERTICAL, scales[2]);
                }
                break;
            case 6: // 四张3
                if (scales == null) {
                    addLine(0, Line.Direction.VERTICAL, 0.4f);
                    addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                    addLine(1, Line.Direction.HORIZONTAL, 0.5f);
                } else {
                    addLine(0, Line.Direction.VERTICAL, scales[0]);
                    addLine(0, Line.Direction.HORIZONTAL, scales[1]);
                    addLine(1, Line.Direction.HORIZONTAL, scales[2]);
                }
                break;
        }
    }
}