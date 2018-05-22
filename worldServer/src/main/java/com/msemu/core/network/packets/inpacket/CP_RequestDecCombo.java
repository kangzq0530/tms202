package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.jobs.legend.Aran;
import com.msemu.world.constants.MapleJob;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_RequestDecCombo extends InPacket<GameClient> {

    public CP_RequestDecCombo(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        if (MapleJob.is狂狼勇士(chr.getJob())) {
            JobHandler jobHandler = chr.getJobHandler();
            if (jobHandler instanceof Aran) {
                ((Aran) jobHandler).showCombo();
            }
        }
    }
}
