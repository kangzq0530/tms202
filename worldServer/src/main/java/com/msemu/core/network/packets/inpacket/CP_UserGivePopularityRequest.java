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

import com.msemu.commons.enums.FileTimeUnit;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_GivePopularityResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterStat;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_UserGivePopularityRequest extends InPacket<GameClient> {

    private int targetCharId;
    private boolean raise;

    public CP_UserGivePopularityRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        targetCharId = decodeInt();
        raise = decodeByte() > 0;
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Character target = chr.getField().getCharacterById(targetCharId);
        final CharacterStat cs = chr.getAvatarData().getCharacterStat();
        final CharacterStat targetCs = target.getAvatarData().getCharacterStat();
        if (chr == target) {
            chr.write(new LP_GivePopularityResult.LP_GivePopularitySlefAddError());
        } else if (cs.getLevel() < 15) {
            chr.write(new LP_GivePopularityResult.LP_GivePopularityLevelBelow15Error());
        } else if (cs.getLastMonthGivePopRecords()
                .stream()
                .filter(record -> record.getChrID() == target.getId())
                .count() > 0) {
            chr.write(new LP_GivePopularityResult.LP_GivePopularityNotThisMonthError());
        } else if (cs.getLastGivePopRecord()
                .getCreatedDate().plus(24, FileTimeUnit.HOUR).before(FileTime.now())) {
            chr.write(new LP_GivePopularityResult.LP_GivePopularityNotTodayError());
        } else {
            targetCs.setPop(targetCs.getPop() + 1);

            chr.write(new LP_GivePopularityResult.LP_GivePopularitySuccess(target.getName(), raise, targetCs.getPop()));
            target.write(new LP_GivePopularityResult.LP_GivePopularitySuccessForTarget(chr.getName(), raise));
        }

    }
}
