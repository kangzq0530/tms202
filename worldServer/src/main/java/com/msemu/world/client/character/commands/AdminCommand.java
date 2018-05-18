package com.msemu.world.client.character.commands;


import com.msemu.commons.utils.StringUtils;
import com.msemu.core.network.GameClient;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.constants.MapleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
public class AdminCommand {

    private static final Logger log = LoggerFactory.getLogger("AdminCommands");

    public static class warp extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            Character chr = client.getCharacter();
            Channel channel = client.getChannelInstance();
            Character victim = null;
            Field toField = null;
            if (args.size() < 2) {
                return false;
            }
            victim = World.getInstance().getCharacterByName(args.get(1));
            toField = StringUtils.isNumber(args.get(1)) ? channel.getField(Integer.parseInt(args.get(1))) : null;
            if (victim == null && toField == null)
                return false;

            if(victim != null) {
                toField = victim.getField();
                chr.warp(toField, toField.findClosestPortal(victim.getPosition()));
            } else {
                chr.warp(toField);
            }

            return true;
        }

        @Override
        public String getHelpMessage() {
            return "";
        }
    }

    public static class job extends CommandExecute {

        @Override
        public boolean execute(GameClient client, List<String> args) {
            if(args.size() < 2) {
                return false;
            }
            if(!StringUtils.isNumber(args.get(1))) {
                return false;
            }
            int jobId = Integer.parseInt(args.get(1));
            if(!MapleJob.isExist(jobId)) {
                return false;
            }
            final Character chr = client.getCharacter();
            chr.setJob(jobId);
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "";
        }
    }
}
