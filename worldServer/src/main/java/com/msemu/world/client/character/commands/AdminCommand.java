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

package com.msemu.world.client.character.commands;


import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.enums.FileTimeUnit;
import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.stats.CharacterLocalStat;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.MobData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.DropType;
import com.msemu.world.enums.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
public class AdminCommand {

    private static final Logger log = LoggerFactory.getLogger("AdminCommands");


    public static class spawnRuneStone extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {

            Character chr = client.getCharacter();

            chr.getField().getRuneStoneManager().spawnTask();

            return false;
        }

        @Override
        public String getHelpMessage() {
            return "!spawnRuneStone <TYPE> - 叫出符文輪";
        }
    }

    public static class warp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            Character chr = client.getCharacter();
            Channel channel = client.getChannelInstance();
            Character victim;
            Field toField;
            if (args.size() < 2) {
                return false;
            }
            victim = World.getInstance().getCharacterByName(args.get(1));
            toField = StringUtils.isNumber(args.get(1)) ? channel.getField(Integer.parseInt(args.get(1))) : null;
            if (victim == null && toField == null)
                return false;

            if (victim != null) {
                toField = victim.getField();
                chr.warp(toField, toField.findClosestPortal(victim.getPosition()));
            } else {
                chr.warp(toField);
            }

            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!warp <FieldID|CharacterName> : warp to specified field or character";
        }
    }

    public static class job extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2) {
                return false;
            }
            if (!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            int jobId = Integer.parseInt(args.get(1));
            if (!MapleJob.isExist(jobId)) {
                return false;
            }
            final Character chr = client.getCharacter();
            chr.setJob(jobId);
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!job <jobID> : change Job";
        }
    }

    public static class item extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2) {
                return false;
            }
            if (!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            final Character chr = client.getCharacter();
            final int itemID = Integer.parseInt(args.get(1));
            final int quantity = args.size() == 2 ? 1 : Integer.parseInt(args.get(2));
            final ItemData itemData = ItemData.getInstance();
            final Item item = itemData.createItem(itemID, true);
            if (item == null) {
                chr.chatMessage(ChatMsgType.SYSTEM, "道具不存在");
                return true;
            }
            item.setQuantity(quantity);
            chr.giveItem(item);
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!item <ItemID> : add an Item to inventory";
        }
    }

    public static class drop extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2) {
                return false;
            }
            if (!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            final Character chr = client.getCharacter();
            final Field field = chr.getField();
            final int itemID = Integer.parseInt(args.get(1));
            final ItemData itemData = ItemData.getInstance();
            final Item item = itemData.createItem(itemID);
            if (item == null) {
                // no item
                return true;
            }
            final Item dropItem = item;
            final Drop drop = new Drop(-1);
            drop.setOwnerID(chr.getId());
            drop.setDropType(DropType.ITEM);
            drop.setItem(dropItem);
            drop.setExpireTime(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
            field.drop(drop, chr.getPosition());
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!drop <ItemID> : drop an Item to field";
        }
    }

    public static class dropMoney extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2) {
                return false;
            }
            if (!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            final Character chr = client.getCharacter();
            final Field field = chr.getField();
            final int money = Integer.parseInt(args.get(1));
            final Drop drop = new Drop(-1);
            drop.setOwnerID(chr.getId());
            drop.setDropType(DropType.MONEY);
            drop.setMoney(money);
            drop.setExpireTime(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
            field.drop(drop, chr.getPosition());
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!drop <ItemID> : drop an Item to field";
        }
    }

    public static class levelUp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            final Character chr = client.getCharacter();
            chr.addExp(GameConstants.CHAR_EXP_TABLE[chr.getLevel()] - chr.getExp());
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!levelup - 角色升級";
        }
    }


    public static class level extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2) {
                return false;
            }
            if (!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            final Character chr = client.getCharacter();
            int level = Integer.parseInt(args.get(1));
            level = Math.min(250, level);
            level = Math.max(1, level);
            chr.setExp(0);
            chr.setStat(Stat.LEVEL, level);
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!level <level> : change level";
        }
    }

    public static class info extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            Character chr = client.getCharacter();
            CharacterStat cs = chr.getAvatarData().getCharacterStat();
            CharacterLocalStat localStat = chr.getCharacterLocalStat();
            chr.chatMessage(ChatMsgType.NOTICE, String.format("[Ability] Level: %d HP: (%d/%d) MP: (%d/%d)",
                    cs.getLevel(), cs.getHp(), localStat.getMaxHp(), cs.getMp(), localStat.getMaxMp()));
            chr.chatMessage(ChatMsgType.NOTICE, String.format("[Ability] STR: %d(+%d) DEX %d(+%d) INT: %d(+%d) LUK %d(+%d) ",
                    localStat.getStr(), (localStat.getStr() - cs.getStr()),
                    localStat.getDex(), (localStat.getDex() - cs.getDex()),
                    localStat.getInte(), (localStat.getInte() - cs.getInte()),
                    localStat.getLuk(), (localStat.getLuk() - cs.getLuk())));
            chr.chatMessage(ChatMsgType.NOTICE, String.format("[Ability] PAD: %d MAD: %d ",
                    localStat.getPad(), localStat.getMad()));
            chr.chatMessage(ChatMsgType.NOTICE, String.format("[Ability] Damage: %d ~ %d ",
                    localStat.getMinDamage(), localStat.getMaxDamage()));
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!info - Display character information";
        }
    }

    public static class Hp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            try {
                final int hp = Integer.parseInt(args.get(1));
                final Character chr = client.getCharacter();
                chr.setStat(Stat.HP, hp);
            } catch (NumberFormatException except) {
                return false;
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!hp <value> - set hp";
        }
    }

    public static class MaxHp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            try {
                final int mhp = Integer.parseInt(args.get(1));
                final Character chr = client.getCharacter();
                chr.setStat(Stat.MAX_HP, mhp);
            } catch (NumberFormatException except) {
                return false;
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!hp <value> - set max hp";
        }
    }

    public static class Mp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            try {
                final int mp = Integer.parseInt(args.get(1));
                final Character chr = client.getCharacter();
                chr.setStat(Stat.MP, mp);
            } catch (NumberFormatException except) {
                return false;
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!hp <value> - set mp";
        }
    }

    public static class MaxMp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            try {
                final int mmp = Integer.parseInt(args.get(1));
                final Character chr = client.getCharacter();
                chr.setStat(Stat.MAX_MP, mmp);
            } catch (NumberFormatException except) {
                return false;
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!hp <value> - set max mp";
        }
    }

    public static class Sp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            final Character chr = client.getCharacter();
            int jobGrade = 0;
            int addSp = 0;
            if (args.size() < 3) {
                addSp = Integer.parseInt(args.get(1));
            } else {
                jobGrade = Integer.parseInt(args.get(1));
                addSp = Integer.parseInt(args.get(2));
            }
            chr.addSp(jobGrade, addSp);
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!sp <jobGrade> <value>";
        }
    }

    public static class SpawnMob extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 2)
                return false;
            final int templateId = Integer.parseInt(args.get(1));
            final Character chr = client.getCharacter();
            final Mob mob = MobData.getInstance().getMobFromTemplate(templateId);

            if (mob != null) {
                mob.setPosition(chr.getPosition());
                mob.setFh(chr.getFh());
                mob.setOriginFh(chr.getFh());
                client.getCharacter().getField().spawnLife(mob);
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!spawnMob <templateId>";
        }
    }

    public static class LearnSkill extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if (args.size() < 3)
                return false;
            final Character chr = client.getCharacter();

            int skillId = Integer.parseInt(args.get(1));
            int level = Integer.parseInt(args.get(2));
            int masterLevel = 0;
            if (args.size() >= 4)
                masterLevel = Integer.parseInt(args.get(3));
            SkillInfo si = SkillData.getInstance().getSkillInfoById(skillId);
            if (si != null) {
                Skill skill = chr.getSkill(skillId);
                if (skill == null) {
                    skill = SkillData.getInstance().getSkillById(skillId);
                }
                masterLevel = Math.min(masterLevel, skill.getMaxLevel());
                if (masterLevel > 0)
                    skill.setMasterLevel(masterLevel);
                level = Math.max(0, level);
                level = Math.min(level, skill.getMasterLevel());
                skill.setCurrentLevel(level);
                chr.addSkill(skill);
            } else {
                chr.chatMessage(ChatMsgType.GAME_DESC, "找不到此技能");
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!skill <skillId> <level>\r\n"
                    + "!skill <skillId> <level> <master level>";
        }
    }

    public class ServerStatus extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            String[] lines = ServerInfoUtils.getMemoryUsageStatistics();
            Arrays.stream(lines).forEach(line -> client.getCharacter().chatMessage(ChatMsgType.NOTICE, line));
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "!serverstatus";
        }
    }

}
