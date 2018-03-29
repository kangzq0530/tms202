package com.msemu.commons.network.crypt;

import java.nio.ByteBuffer;

/**
 * Created by Weber on 2018/3/28.
 */
public interface ICipher {

    byte[] getNewIv(byte[] iv);

    void crypt(byte[] data, byte[] iv);

    byte[] getSendHeader(int length, byte[] iv);

    int getLength(int header);

    boolean checkHeader(int header, byte[] iv);
}
