package com.msemu.commons.data.provider.streams;

import java.io.IOException;

/**
 * Created by Weber on 2018/3/16.
 */
interface IByteStream {

    long getPosition() throws IOException;

    void seek(final long offset) throws IOException;

    long getBytesRead();

    int readByte() throws IOException;

    void unReadByte() throws IOException;

    int readLastByte() throws IOException;

    int[] readLastBytes(int bytes) throws IOException;

    long available() throws IOException;

}
