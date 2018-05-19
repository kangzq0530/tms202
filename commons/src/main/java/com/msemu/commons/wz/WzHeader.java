package com.msemu.commons.wz;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzHeader {
    @Getter
    @Setter
    private String ident;

    @Getter
    @Setter
    private String copyRight;

    @Getter
    @Setter
    private long fileSize;


    @Getter
    @Setter
    private long fileStart;

}
