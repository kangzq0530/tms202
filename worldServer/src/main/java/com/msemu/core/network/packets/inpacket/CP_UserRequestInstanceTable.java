package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_ResultInstanceTable;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.InstanceTableInfo;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.constants.GameConstants;

/**
 * Created by Weber on 2018/4/28.
 */
public class CP_UserRequestInstanceTable extends InPacket<GameClient> {

    private String tableName;

    private int nCol;

    private int nRow;

    public CP_UserRequestInstanceTable(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        tableName = decodeString();
        nCol = decodeInt();
        nRow = decodeInt();
    }

    @Override
    public void runImpl() {

        InstanceTableInfo info = new InstanceTableInfo();

        switch (tableName) {
            // 超技能點數
            case "hyper":
            case "hyper_shaman": {
                if ((nRow == 0) && ((nCol == 28) || (nCol == 32) || (nCol == 36) || (nCol == 38) || (nCol == 40))) {
                    info.setValue(1);
                } else if ((nRow == 1) && ((nCol == 30) || (nCol == 34) || (nCol == 40))) {
                    info.setValue(1);
                }
                info.setRightResult(nCol < 41);
                break;
            }
            case "incHyperStat":
                info.setRightResult(true);
                info.setValue((nCol / 10) - 11);
                break;
            case "needHyperStatLv":
                int value = GameConstants.getHyperStatReqAp(nCol);
                info.setValue(value);
                break;
            default: {
                Character chr = getClient().getCharacter();
                if (tableName.startsWith("9200") || tableName.startsWith("9201")) {
                    info.setValue(100);
                } else {
                    int skillId = Integer.parseInt(tableName);
                    Skill skill = chr.getSkill(skillId);
                    int skillLevel = skill == null ? 0 : skill.getCurrentLevel();
                    if (skillId / 100000 == 920 && skill != null) {
                        info.setValue(Math.max(0, 100 - ((nCol + 1) - skillLevel) * 20));
                    }
                }
            }
        }
        getClient().write(new LP_ResultInstanceTable(tableName, nCol, nRow, info));
    }
}
