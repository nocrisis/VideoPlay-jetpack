package com.catherine.libcommon;

import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;


public class ViewHelper {
    public static final int RADIUS_ALL = 0;
    public static final int RADIUS_LEFT = 1;
    public static final int RADIUS_TOP = 2;
    public static final int RADIUS_RIGHT = 3;
    public static final int RADIUS_BOTTOM = 4;

    public static void setViewOutline(View view, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        TypedArray array = view.getContext().obtainStyledAttributes(attributeSet, R.styleable.viewOutLineStrategy, defStyleAttr, defStyleRes);
        int radius = array.getDimensionPixelOffset(R.styleable.viewOutLineStrategy_radius, 0);
        int radiusSide = array.getIndex(R.styleable.viewOutLineStrategy_radiusSide);
        array.recycle();

        setViewOutline(view, radius, radiusSide);
    }

    public static void setViewOutline(View view, int radius, int radiusSide) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int width = view.getWidth();
                int height = view.getHeight();
                if (width == 0 || height == 0)
                    return;
                int left = 0, right = width, top = 0, bottom = height;
                if (radiusSide != RADIUS_ALL) {
                    if (radiusSide == RADIUS_LEFT) {
                        right += radius;
                    } else if (radiusSide == RADIUS_TOP) {
                        bottom += radius;
                    } else if (radiusSide == RADIUS_RIGHT) {
                        left -= radius;
                    } else if (radiusSide == RADIUS_BOTTOM) {
                        top -= radius;
                    }
                    outline.setRoundRect(left, top, right, bottom, radius);
                    return;
                }
                if (radius <= 0) {
                    outline.setRect(left, top, width, height);
                } else {
                    outline.setRoundRect(left, top, width, height, radius);
                }
            }
        });
    }
}
