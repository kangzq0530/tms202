package com.msemu.commons.network;

import java.nio.ByteBuffer;

/**
 * Created by Weber on 2018/3/14.
 */
public interface ICipher {

    byte[] getIV();

    byte[] encodeHeaderSize(int header);

    int decodeHeaderSize(int header);

    boolean checkHeader(int header);

    boolean crypt(ByteBuffer byteBuffer, int offset, int length);

}
