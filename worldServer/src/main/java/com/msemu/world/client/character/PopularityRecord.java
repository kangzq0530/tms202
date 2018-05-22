package com.msemu.world.client.character;

import com.msemu.commons.utils.types.FileTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/22.
 */
@Getter
@Setter
public class PopularityRecord {

    private int chrID;

    private FileTime createdDate;

    public PopularityRecord() {
        this.createdDate = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    }

}
