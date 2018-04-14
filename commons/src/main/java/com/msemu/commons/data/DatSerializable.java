package com.msemu.commons.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public interface DatSerializable {

    void write(DataOutputStream dos) throws IOException;

    DatSerializable load(DataInputStream dis) throws IOException ;
}
