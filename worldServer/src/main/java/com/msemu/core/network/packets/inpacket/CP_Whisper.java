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

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.field.LP_Whisper;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.whisper.WhisperReceivedResult;
import com.msemu.world.client.field.whisper.WhisperResult;
import com.msemu.world.client.field.whisper.location.*;
import com.msemu.world.enums.WhisperCommand;
import org.slf4j.LoggerFactory;

import static com.msemu.world.enums.WhisperCommand.Location_Request;

/**
 * Created by Weber on 2018/5/12.
 */
public class CP_Whisper extends InPacket<GameClient> {

    private byte command;
    private int updateTick;
    private String charName;
    private String text;

    public CP_Whisper(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        command = decodeByte();
        updateTick = decodeInt();


        switch (WhisperCommand.getByValue(command)) {
            case Location_Request:
            case Location_F_Request:
                charName = decodeString();
                break;
            case Whisper_Request:
                charName = decodeString();
                text = decodeString();
                break;
            default:
                LoggerFactory.getLogger(CP_Whisper.class).warn(String.format("Unknown WhisperCommand : %d", command));
                break;
        }
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Channel channel = getClient().getChannelInstance();
        final World world = getClient().getWorld();
        Character target;
        Channel targetChannel;

        WhisperCommand cmd = WhisperCommand.getByValue(command);

        // TODO GM 不給搜尋 & 密語
        // TODO 在商城的結果

        switch (cmd) {
            case Location_Request:
            case Location_F_Request:
                target = channel.getCharacterByName(charName);
                if (target != null) {
                    // find in channel
                    if (cmd == Location_Request)
                        chr.write(new LP_Whisper(new LocationFoundInFieldResult(target.getName(), target.getFieldID())));
                    else
                        chr.write(new LP_Whisper(new FriendLocationFoundInFieldResult(target.getName(), target.getFieldID())));
                } else {
                    target = world.getCharacterByName(charName);
                    targetChannel = world.getChannelByCharacterName(charName);
                    if ( target != null && targetChannel != null) {
                        if (cmd == Location_Request)
                            chr.write(new LP_Whisper(new LocationFoundInChannelResult(target.getName(), targetChannel.getChannelId())));
                        else
                            chr.write(new LP_Whisper(new FriendLocationFoundInChannelResult(target.getName(), targetChannel.getChannelId())));
                    } else {
                        // nothing found
                        chr.write(new LP_Whisper(new LocationNotFoundResult(charName)));
                    }
                }
                break;
            case Whisper_Request:
                target = world.getCharacterByName(charName);
                targetChannel = world.getChannelByCharacterName(charName);
                if( target != null && targetChannel != null) {
                    target.write(new LP_Whisper(new WhisperReceivedResult(chr.getName(), channel.getChannelId(), text)));
                    chr.write(new LP_Whisper(new WhisperResult(charName, true)));
                } else {
                    chr.write(new LP_Whisper(new WhisperResult(charName, false)));
                }
                break;
        }

    }
}
