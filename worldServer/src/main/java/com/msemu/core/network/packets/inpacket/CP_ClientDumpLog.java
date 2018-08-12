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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.enums.ClientDumpType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CP_ClientDumpLog extends InPacket<GameClient> {

    private static final Logger log = LoggerFactory.getLogger(CP_ClientDumpLog.class);

    private ClientDumpType type;
    private int errorCode;
    private OutHeader header;
    private ByteBuf buffer;

    public CP_ClientDumpLog(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        if (available() < 8)
            return;

        type = ClientDumpType.getByValue(decodeShort());
        errorCode = decodeInt();

        short dataSize = decodeShort();
        skip(4);
        short headerValue = decodeShort();
        header = OutHeader.getOutHeaderByOp(headerValue);
        buffer = Unpooled.copiedBuffer(decodeBytes(available()));

        final GameClient client = getClient();
        final Account account = client.getAccount();
        final String userName = account.getUsername() != null ? account.getUsername() : "<null>";
        final Character chr = client.getCharacter();
        final String charName = chr != null ? chr.getName() : "<null>";
        final int charLevel = chr != null ? chr.getLevel() : 0;
        final MapleJob job = chr != null ? MapleJob.getById(chr.getJob()) : null;
        final String jobName = job != null ? job.name() : "<null>";


        final Field field = chr != null ? chr.getField() : null;
        final String fieldName = field != null ? field.getFieldData().getName() : "<null>";


        StringBuilder builder = new StringBuilder();

        builder.append("帳號:").append(userName).append(System.lineSeparator())
                .append("角色:").append(charName).append("(等級:").append(charLevel).append(")").append(System.lineSeparator())
                .append("職業:").append(jobName).append(System.lineSeparator())
                .append("地圖:").append(fieldName).append(System.lineSeparator())
                .append("錯誤類型:").append(type.name()).append("(").append(errorCode).append(")").append(System.lineSeparator())
                .append("封包頭:").append(header).append(System.lineSeparator())
                .append("封包內容:").append(HexUtils.byteArraytoHex(buffer.array()));
        log.debug(builder.toString());
    }

    @Override
    public void runImpl() {

    }
}
