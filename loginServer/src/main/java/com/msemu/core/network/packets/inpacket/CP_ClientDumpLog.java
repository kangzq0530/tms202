package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.HexUtils;
import com.msemu.commons.utils.StringUtils;
import com.msemu.core.network.LoginClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_ClientDumpLog extends InPacket<LoginClient> {
    private static final Logger log = LoggerFactory.getLogger("ClientDump");
    private int logType;
    private int errorType;
    private int dataSize;
    private short opcode;
    private byte[] packet;
    private int packetLen;

    public CP_ClientDumpLog(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        if(getLength() < 8) {
            log.error("ClientDump", HexUtils.byteArraytoHex(getData()));
        }
        logType = decodeShort();
        errorType = decodeInt();
        dataSize =decodeShort();
        decodeInt();
        opcode = decodeShort();
        packet = decodeBytes(available());


    }

    @Override
    public void runImpl() {

        String typeStr = "AuthFail?!";
        switch (logType) {
            case 0x01:
                typeStr = "SendBackupPacket";
                break;
            case 0x02:
                typeStr = "Crash Report";
                break;
            case 0x03:
                typeStr = "Exception";
                break;
            default:
                break;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("帳號:").append(client.getAccount() != null ? client.getAccount().getUsername() : "null")
        .append("\n");
        sb.append("錯誤類型:").append(typeStr).append("(").append(errorType).append(")\n").append(")\n");
        sb.append("[Opcode]:").append(OutHeader.getOutHeaderByOp(opcode).name())
            .append("(0x").append(StringUtils.getLeftPaddedStr(Integer.toHexString(opcode), '0', 4)).append(")")
                .append("[").append(dataSize-4).append("字元]\n\n");
        sb.append(packet.length < 1 ? "" : HexUtils.byteArraytoHex(packet)).append("\n");
        sb.append(packet.length < 1 ? "" : HexUtils.toAscii(packet)).append("\n").append("\n");

        log.error(sb.toString());

    }
}
