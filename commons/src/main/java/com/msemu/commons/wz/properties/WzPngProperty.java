package com.msemu.commons.wz.properties;

import com.msemu.commons.utils.types.Size;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.WzObjectType;
import com.msemu.commons.wz.WzPropertyType;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzPngProperty extends WzImageProperty {


    @Getter
    @Setter
    private int width, height, format, format2;

    private BufferedImage value;

    public WzPngProperty() {
        super("PNG");
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof Size) {
            Size rect = (Size) object;
            width = rect.getWidth();
            height = rect.getHeight();
        } else if (object instanceof BufferedImage) {
            BufferedImage image = (BufferedImage) object;
            width = image.getWidth();
            height = image.getHeight();
            value = value;
        }
    }

    @Override
    public WzPropertyType propType() {
        return WzPropertyType.PNG;
    }

    @Override
    public WzObjectType type() {
        return WzObjectType.Property;
    }

}
