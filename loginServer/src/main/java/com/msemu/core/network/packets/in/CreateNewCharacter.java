package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.CharCreateInfo;
import com.msemu.login.enums.CharCreateItemFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/19.
 */
public class CreateNewCharacter extends InPacket<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger(CreateNewCharacter.class);

    private String charName;
    private int keyMapType;
    private int jobType;
    private int jobSubCategory;
    private byte gender;
    private byte skin;
    private List<Integer> items;


    public CreateNewCharacter(short opcode) {
        super(opcode);
        items = new ArrayList<>();
    }

    @Override
    public void read() {
        charName = decodeString();
        keyMapType = decodeInt();
        decodeInt();
        jobType = decodeInt();
        jobSubCategory = decodeShort();
        gender = decodeByte();
        skin = decodeByte();

        int size = decodeByte();

        for(int i = 0 ; i < size;i++) {
            items.add(decodeInt());
        }

    }

    @Override
    public void runImpl() {

        CharCreateInfo info = CharCreateInfo.getByJobType(jobType);

        if(info == null || !info.enableCreate() || info.getSubJob() != jobSubCategory) {
            if(info == null) {
                log.error("Found new Job jobType:{}", jobType);
            }
            return;
        }

        boolean checkSkin = skin == 0;

        switch (info) {
            case 皇家騎士團:
            case 米哈逸:
                skin = 10;
                checkSkin = true;
                break;
            case 狂狼勇士:
                skin = 11;
                checkSkin = true;
                break;
            case 精靈遊俠:
                skin = 12;
                checkSkin = true;
                break;
            case 惡魔:
                checkSkin = checkSkin || skin == 13;
                break;
        }

        if(!checkSkin) {
            return;
        }

        Map<CharCreateItemFlag, Integer> equipIds = new LinkedHashMap<>();

        int i = 0;
        for(CharCreateItemFlag itemFlag : CharCreateItemFlag.values()) {
            if(itemFlag.check(info.getFlag())) {
                int itemId = items.get(i++);
                //TODO 讀取wz資料確定道具ID是否符合
            } else  {
                equipIds.put(itemFlag, 0);
            }
        }


        log.warn("Job: {} sub:{}", info.name(), jobSubCategory);
        //TODO 創立角色

    }
}
