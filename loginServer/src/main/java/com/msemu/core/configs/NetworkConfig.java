/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigAfterLoad;
import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;
import com.msemu.commons.enums.NetIOExecMode;

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(
        name = "configs/network.properties"
)
public class NetworkConfig {
    @ConfigComments(
            comment = {"Rmi connection host."}
    )
    @ConfigProperty(
            name = "network.rmi.host",
            value = "0.0.0.0"
    )
    public static String RMI_HOST;
    @ConfigComments(
            comment = {"Rmi connection port."}
    )
    @ConfigProperty(
            name = "network.rmi.port",
            value = "9000"
    )
    public static int RMI_PORT;
    @ConfigComments(
            comment = {"Rmi connection password."}
    )
    @ConfigProperty(
            name = "network.rmi.password",
            value = "1234"
    )
    public static String RMI_PASSWORD;
    @ConfigComments(
            comment = {"Host for server binding.", "Default: 0.0.0.0"}
    )
    @ConfigProperty(
            name = "network.host",
            value = "0.0.0.0"
    )
    public static String HOST;
    @ConfigComments(
            comment = {"Port for server binding.", "Default: 8484"}
    )
    @ConfigProperty(
            name = "network.port",
            value = "8484"
    )
    public static int PORT;
    @ConfigComments(
            comment = {"Receive buffer size.", "Default: 32768"}
    )
    @ConfigProperty(
            name = "network.recv.buffer.size",
            value = "32768"
    )
    public static int RECV_BUFFER_SIZE;
    @ConfigComments(
            comment = {"Send buffer size.", "Default: 65536"}
    )
    @ConfigProperty(
            name = "network.send.buffer.size",
            value = "65536"
    )
    public static int SEND_BUFFER_SIZE;
    @ConfigComments(
            comment = {"NetworkThread socket backlog size.", "See: http://www.linuxjournal.com/files/linuxjournal.com/linuxjournal/articles/023/2333/2333s2.html", "Default: 50"}
    )
    @ConfigProperty(
            name = "network.server.socket.backlog",
            value = "50"
    )
    public static int SERVER_SOCKET_BACKLOG;
    @ConfigComments(
            comment = {"Client socket options.", "SO_SNDBUF - the size of the socket\'s send buffer. On most systems this the size of a kernel buffer so be careful! See RFC1323.", "SO_RCVBUF - the size of the socket\'s receive buffer. On most systems this the size of a kernel buffer so be careful! See RFC1323.", "TCP_NODELAY - The Nagle algorithm. Enabling it increases throughput but also increases latency. See RFC1122.", "Default: SO_SNDBUF(8192);SO_RCVBUF(8192);TCP_NODELAY(true)"}
    )
    @ConfigProperty(
            name = "network.client.socket.options",
            value = "SO_SNDBUF(8192);SO_RCVBUF(8192);TCP_NODELAY(true)"
    )
    public static String CLIENT_SOCKET_OPTIONS;
    @ConfigComments(
            comment = {"NetworkThread socket options.", "SO_REUSEADDR - if true, prevents socket from usage until all opened sockets are really closed. See RFC793.", "Default: SO_REUSEADDR(true);SO_RCVBUF(4096)"}
    )
    @ConfigProperty(
            name = "network.server.socket.options",
            value = "SO_REUSEADDR(false);SO_RCVBUF(4096)"
    )
    public static String SERVER_SOCKET_OPTIONS;

    @ConfigComments(
            comment = {"IO Network thread execution mode.", "POOLED - All IO response are executed in a special thread IO execution pool", "FIXED - All IO response execution is spread across fixed number of treads", "Default: POOLED"}
    )
    @ConfigProperty(
            name = "network.io.execution.mode",
            value = "POOLED"
    )
    public static NetIOExecMode IO_EXECUTION_MODE;
    @ConfigComments(
            comment = {"Number of IO Network threads.", "Default: -1 (Processor count)"}
    )
    @ConfigProperty(
            name = "network.io.execution.thread.num",
            value = "-1"
    )
    public static int IO_EXECUTION_THREAD_NUM;
    @ConfigComments(
            comment = {"Income opcodes header size.", "Default: 3"}
    )
    @ConfigProperty(
            name = "network.income.packet.header.size",
            value = "3"
    )
    public static byte INCOME_PACKET_HEADER_SIZE;
    @ConfigComments(
            comment = {"Max income opcodes size.", "Default: 16384"}
    )
    @ConfigProperty(
            name = "network.max.income.packet.size",
            value = "16384"
    )
    public static int MAX_INCOME_PACKET_SIZE;
    @ConfigComments(
            comment = {"Outcome opcodes header size.", "Default: 3"}
    )
    @ConfigProperty(
            name = "network.outcome.packet.header.size",
            value = "3"
    )
    public static byte OUTCOME_PACKET_HEADER_SIZE;
    @ConfigComments(
            comment = {"Max outcome opcodes size.", "Default: 16384"}
    )
    @ConfigProperty(
            name = "network.max.outcome.packet.size",
            value = "16384"
    )
    public static int MAX_OUTCOME_PACKET_SIZE;

    public NetworkConfig() {
    }

    @ConfigAfterLoad
    private void afterLoad() {
        if (IO_EXECUTION_THREAD_NUM < 0) {
            IO_EXECUTION_THREAD_NUM = Runtime.getRuntime().availableProcessors();
        }

    }
}
