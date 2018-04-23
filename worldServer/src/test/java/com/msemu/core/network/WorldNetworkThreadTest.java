package com.msemu.core.network;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Weber on 2018/4/23.
 */
public class WorldNetworkThreadTest {
    @Test
    public void testStartup() throws IOException, InterruptedException {
        WorldNetworkThread.getInstance().startup();
    }
}