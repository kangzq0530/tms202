package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class CP_GuildRequest extends InPacket<GameClient> {

    private GuildResultType opcode = GuildResultType.NONE;
    private String guildName;
    private String charName;
    private String guildNotice;
    private int guildID;
    private int characterID;
    private String gradeName[] = new String[5];
    private int newRank;
    private int newBG;
    private int newBGColor;
    private int newLogo;
    private int newLogoColor;
    private int guildSkillID;

    public CP_GuildRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        opcode = GuildResultType.getByBalue(decodeByte());
        switch (opcode) {
            case ReqLoadGuild:
                break;
            case ReqFindGuildByCid:
                break;
            case ReqFindGuildByGID:
                guildID = decodeInt();
                break;
            case ReqCheckGuildName:
                guildName = decodeString();
                break;
            case ReqInviteGuild:
                charName = decodeString();
                break;
            case ReqWithdrawGuild:
                characterID = decodeInt();
                charName = decodeString();
                break;
            case ReqKickGuild:
                characterID = decodeInt();
                charName = decodeString();
                break;
            case ReqSetGuildName:
                guildNotice = decodeString();
                break;
            case ReqSetGradeName:
                for(int i = 0; i < 5; i++)
                    gradeName[i] = decodeString();
                break;
            case ReqSetMemberGrade:
                characterID = decodeInt();
                newRank = decodeByte();
                break;
            case ReqSetMark:
                newBG = decodeByte();
                newBGColor = decodeByte();
                newLogo = decodeShort();
                newLogoColor = decodeByte();
                break;
            case ReqSkillLevelSetUp:
                guildSkillID = decodeInt();
                break;
            case ReqUseActiveSkill:
                guildSkillID = decodeInt();
                break;
            case ReqChangeGuildMaster:
                characterID = decodeInt();

                // TODO
                break;
            case ReqSearch:
                break;
            case ResCreateGuildAgree_Reply:
                break;
        }
    }

    @Override
    public void runImpl() {

    }
}
