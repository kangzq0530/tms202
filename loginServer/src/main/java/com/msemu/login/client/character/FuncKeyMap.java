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

package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/21.
 */
@Schema
@Entity
@Table(name = "funckeymap")
public class FuncKeyMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fkMapId")
    private List<KeyMapping> keymap = new ArrayList<>();

    @Transient
    private static final int MAX_KEYBINDS = 89;


    public FuncKeyMap() {

    }

    public List<KeyMapping> getKeymap() {
        return keymap;
    }

    public void setKeymap(List<KeyMapping> keymap) {
        this.keymap = keymap;
    }

    public KeyMapping getMappingAt(int index) {
        for(KeyMapping km : getKeymap()) {
            if(km.getIndex() == index) {
                return km;
            }
        }
        return null;
    }

    public void encode(OutPacket<LoginClient> outPacket) {
        if(getKeymap().size() == 0) {
            outPacket.encodeByte(true);
        } else {
            outPacket.encodeByte(false);
            for (int i = 0; i < MAX_KEYBINDS; i++) {
                KeyMapping tuple = getMappingAt(i);
                if(tuple == null) {
                    outPacket.encodeByte(0);
                    outPacket.encodeInt(0);
                } else {
                    outPacket.encodeByte(tuple.getType());
                    outPacket.encodeInt(tuple.getVal());
                }
            }
        }
    }

    public void putKeyBinding(int index, byte type, int value) {
        KeyMapping km = getMappingAt(index);
        if(km == null) {
            km = new KeyMapping();
            km.setIndex(index);
            km.setType(type);
            km.setVal(value);
            getKeymap().add(km);
        } else {
            km.setType(type);
            km.setVal(value);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static FuncKeyMap getDefaultMapping() {
        // got these from mushy, too lazy to make this myself ;D
        FuncKeyMap fkm = new FuncKeyMap();
        int[] array1 = {2, 3, 64, 4, 65, 5, 6, 7, 8, 13, 17, 16, 19, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 31, 34, 35, 33, 38, 39, 37, 43, 40, 41, 46, 47, 44, 45, 51, 50, 49, 48, 59, 57, 56, 63, 62, 61, 60};
        int[] array2 = {4, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 6, 5, 5, 6, 6, 6, 6};
        int[] array3 = {10, 12, 105, 13, 106, 18, 24, 21, 29, 33, 5, 8, 4, 0, 31, 28, 1, 34, 19, 25, 15, 14, 52, 2, 17, 11, 26, 20, 27, 3, 9, 16, 23, 6, 32, 50, 51, 35, 7, 22, 30, 100, 54, 53, 104, 103, 102, 101};
        for (int i = 0; i < array1.length; i++) {
            fkm.putKeyBinding(array1[i], (byte) array2[i], array3[i]);
        }
        return fkm;
    }
}