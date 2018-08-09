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

package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.configs.LoginConfig;
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;

import java.util.Set;

/**
 * Created by Weber on 2018/3/31.
 */
public class LP_SelectWorldResult extends OutPacket<LoginClient> {

    public LP_SelectWorldResult(Account account, WorldInfo worldInfo) {
        super(OutHeader.LP_SelectWorldResult);

        encodeByte(0); // loginResultType

        encodeString(worldInfo.getServerType().getValue());
        encodeInt(4);
        encodeByte(worldInfo.getServerType().equals(WorldServerType.Reboot));

        int reserved = 0;
        encodeInt(reserved); // TODO: 閃耀之星
        FileTime.now().encode(this);
        for (int i = 0; i < reserved; i++) {
            FileTime ft = FileTime.getFTFromLong(0);
            encodeInt(ft.getLowDateTime());
            ft.encode(this);
        }
        encodeByte(false);
        Set<Character> chars = account.getCharacters();
        encodeInt(chars.size());
        chars.forEach(c -> encodeInt(c.getId()));
        encodeByte(chars.size());
        chars.forEach(c -> {
            c.getAvatarData().encode(this);
            encodeByte(false);
            boolean hasRanking = c.getRanking() != null;
            encodeByte(hasRanking);
            if (hasRanking) {
                c.getRanking().encode(this);
            }
        });
        encodeByte(LoginConfig.CHECK_SECOND_PASSWORD ? 1 : 3); // 1 要第二組密碼 3 不用
        encodeByte(account.getPicStatus().getValue()); // bLoginOpt
        encodeInt(account.getCharacterSlots());
        encodeInt(0); // 50 等角色卡數量
        encodeInt(-1); // nEventNewCharJob
        encodeByte(0); // nRenameCount
        FileTime.now().encodeR(this);
        encodeByte(0);   // 變更角色名稱開關(在名字下方的, 不能單獨控制哪個角色能改)[0:關、1:開]
        encodeByte(0);  // 協議書開關[-1:開、0:關]
        encodeByte(0);
        encodeInt(0);
        encodeInt(0);
        FileTime.now().encodeR(this);
    }
}
