package com.msemu.commons.data.templates.field;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
@Setter
public class LifeData {
    private String type = "";
    private int id;
    private int x, y, f, fh, cy, rx0, rx1;
    private int mobTime, mobAliveReq, regenStart;
    private boolean hide, useDay, useNight, hold, nofoothold, dummy, spine
            , mobTimeOnDie;
    private String limitedname = "";
}
