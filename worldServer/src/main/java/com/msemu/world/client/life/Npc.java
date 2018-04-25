package com.msemu.world.client.life;

import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Npc extends Life {
    private boolean enabled = true;
    private int presentItemID;
    private byte presentItemState;
    private int presentItemTime = -1;
    private int noticeBoardType;
    private int noticeBoardValue;
    private int alpha; // if hideToLocalUser is true
    private String localRepeatEffect;
    private ScreenInfo screenInfo;
    private NpcTemplate template;

    public Npc(int objectId, NpcTemplate template) {
        super(objectId);
        this.template = template;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        // CNpc::Init
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getMoveAction() != 0);
        outPacket.encodeByte(getMoveAction());
        outPacket.encodeShort(getFh());
        outPacket.encodeShort(getRx0()); // rgHorz.low
        outPacket.encodeShort(getRx1()); // rgHorz.high
        outPacket.encodeByte(isEnabled());
        outPacket.encodeInt(getPresentItemID());
        outPacket.encodeByte(getPresentItemState());
        outPacket.encodeInt(getPresentItemTime());
        outPacket.encodeInt(getNoticeBoardType());
        if (getNoticeBoardType() == 1) {
            outPacket.encodeInt(getNoticeBoardValue());
        }
        outPacket.encodeInt(getAlpha());
        outPacket.encodeInt(0);
        outPacket.encodeString(getLocalRepeatEffect());
        ScreenInfo si = getScreenInfo();
        outPacket.encodeByte(si != null);
        if (si != null) {
            si.encode(outPacket);
        }
    }

    @Override
    public Npc deepCopy() {
        Npc copy = new Npc(getObjectId(), getTemplate());
        copy.setLifeType(getLifeType());
        copy.setTemplateId(getTemplateId());
        copy.setX(getX());
        copy.setY(getY());
        copy.setMobTime(getMobTime());
        copy.setF(getF());
        copy.setHide(isHide());
        copy.setFh(getFh());
        copy.setCy(getCy());
        copy.setRx0(getRx0());
        copy.setRx1(getRx1());
        copy.setLimitedName(getLimitedName());
        copy.setUseDay(isUseDay());
        copy.setUseNight(isUseNight());
        copy.setHold(isHold());
        copy.setNoFoothold(isNoFoothold());
        copy.setDummy(isDummy());
        copy.setSpine(isSpine());
        copy.setMobTimeOnDie(isMobTimeOnDie());
        copy.setRegenStart(getRegenStart());
        copy.setMobAliveReq(getMobAliveReq());
        copy.getScripts().addAll(getScripts());
        return copy;
    }

    public List<String> getScripts() {
        return getTemplate().getScripts();
    }
}