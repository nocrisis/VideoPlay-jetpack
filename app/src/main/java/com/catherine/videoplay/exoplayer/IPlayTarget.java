package com.catherine.videoplay.exoplayer;

import android.view.ViewGroup;

public interface IPlayTarget {
    ViewGroup getOwner();

    void focusOnActive();

    void lossActive();

    boolean isPlaying();
}
