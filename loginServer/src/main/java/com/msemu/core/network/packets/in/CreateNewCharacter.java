package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/19.
 */
public class CreateNewCharacter extends InPacket<LoginClient> {

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

        int x = 1;
    }

    @Override
    public void runImpl() {

    }
}
