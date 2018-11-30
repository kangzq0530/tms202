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

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_GuildResult;
import com.msemu.world.Channel;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.client.guild.GuildMember;
import com.msemu.world.client.guild.operations.*;
import com.msemu.world.enums.GuildResultType;
import com.msemu.world.service.GuildService;

public class CP_GuildRequest extends InPacket<GameClient> {

    private GuildResultType opcode = GuildResultType.NONE;
    private String guildName;
    private String charName;
    private String guildNotice;
    private int guildID;
    private int characterID;
    private String gradeName[];
    private int newRank;
    private int newBG;
    private int newBGColor;
    private int newLogo;
    private int newLogoColor;
    private int guildSkillID;
    private boolean createAgreeReply = false;

    public CP_GuildRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        gradeName = new String[5];
        opcode = GuildResultType.getByValue(decodeByte());
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
                for (int i = 0; i < 5; i++)
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
                createAgreeReply = decodeByte() > 0;
                break;
        }
    }

    @Override
    public void runImpl() {

        final Channel channel = getClient().getChannelInstance();
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final GuildService service = GuildService.getInstance();

        switch (opcode) {
            case ReqLoadGuild: {
                final Guild guild = chr.getGuild();
                chr.write(new LP_GuildResult(new LoadGuildDoneResponse(guild)));
                break;
            }
            case ReqFindGuildByCid:
                break;
            case ReqCheckGuildName: {
                final boolean nameExists = service.isGuildNameExists(guildName);
                final boolean nameLegal = service.isGuildNameLegal(guildName);
                if (nameExists) {
                    chr.write(new LP_GuildResult(new CheckGuildNameExistsResponse()));
                } else if (!nameLegal) {
                    chr.write(new LP_GuildResult(new CheckGuildNameUnknownResponse()));
                } else {
                    service.addReservedGuildName(chr, guildName);
                    chr.write(new LP_GuildResult(new CreateGuildAgreeRequest(guildName)));
                }
                break;
            }
            case ResCreateGuildAgree_Reply: {

                final boolean hasReservedName = !service.hasReservedGuildName(chr);
                if (!hasReservedName) {
                    return;
                }
                final String guildName = service.getReservedGuildName(chr);
                service.removeRerservedGuildName(chr);
                if (!createAgreeReply) {
                    chr.write(new LP_GuildResult(new CreateNewGuildDisagreeResponse()));
                    return;
                }
                final boolean hasGuild = chr.getGuild() != null;
                final boolean inField = field.getFieldId() == 200000301;
                final boolean isLevel100 = chr.getLevel() >= 100;
                final boolean hasMoney = chr.getMoney() >= 5000000;
                if (!hasGuild || !inField) {
                    chr.write(new LP_GuildResult(new CreateNewGuildAlreadyJoinedResponse()));
                } else if (!isLevel100) {
                    chr.write(new LP_GuildResult(new CreateNewGuildBeginnerResponse()));
                } else if (!hasMoney) {
                    // chr.write(new LP_GuildResult(new ));
                } else {
                    final Guild guild = service.createNewGuild(chr, guildName);
                    chr.addMoney(-5000000, true);
                    chr.setGuild(guild);
                    chr.write(new LP_GuildResult(new CreateNewGuildDoneResponse(chr.getGuild())));
                    DatabaseFactory.getInstance().saveToDB(guild);
                    DatabaseFactory.getInstance().saveToDB(chr);
                }
                break;
            }
            case ReqInviteGuild: {
                final Guild guild = chr.getGuild();
                if (guild == null)
                    return;
                final GuildMember guildMember = guild.getMemberByID(chr.getId());
                final boolean canInvite = guildMember.getGrade() > 2;
                if (canInvite) {
                    return;
                }
                final Character target = channel.getCharacterByName(charName);
                if (target == null) {
                    chr.write(new LP_GuildResult(new JoinGuildUnknownUserResponse()));
                } else if (guild.getMembers().size() >= guild.getMaxMembers()) {
                    chr.write(new LP_GuildResult(new JoinGuildAlreadyFullResponse()));
                } else if (target.getGuild() != null) {
                    guild.invite(target);
                }
                break;
            }
            case ReqSetGradeName: {
                final Guild guild = chr.getGuild();
                final boolean isLeader = guild.getLeaderID() == chr.getId();
                if (isLeader) {
                    guild.changeGradeNames(gradeName);
                }
                break;
            }
            case ReqSetMark: {
                final Guild guild = chr.getGuild();
                final boolean isLeader = guild.getLeaderID() == chr.getId();
                if (isLeader) {
                    guild.chaneMark(newBG, newBGColor, newLogo, newLogoColor);
                }
                break;
            }
            case ReqSearch:
                // [1] [1] [1] [1] [string]
                break;
        }
    }
}
