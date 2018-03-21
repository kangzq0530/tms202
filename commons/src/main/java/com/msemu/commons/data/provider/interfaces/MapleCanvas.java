package com.msemu.commons.data.provider.interfaces;

/**
 * Created by Weber on 2018/3/16.
 */
import java.awt.image.BufferedImage;

public interface MapleCanvas {

    int getHeight();

    int getWidth();

    BufferedImage getImage();
}