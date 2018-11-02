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

package com.msemu.world.client.character.friends;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
@Setter
public class FriendList {

    //    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "friendListId")
    List<FriendEntry> friendEntries;
    private int id;
    private int maxCount;

    public void addMaxCount(int amount) {
        this.maxCount += amount;
    }

    public FriendEntry getCharacterById(int charID) {
        return friendEntries
                .stream().filter(f -> f.getCharId() == charID)
                .findFirst().orElse(null);
    }

    public FriendEntry getByAccountId(int accountID) {
        return friendEntries
                .stream().filter(f -> f.getAccountID() == accountID)
                .findFirst().orElse(null);
    }

    public void broadcast(OutPacket<GameClient> packet, List<Integer> targets, Character chr) {
        final World world = chr.getClient().getWorld();
        targets.stream()
                .filter(target -> this.getCharacterById(target) != null)
                .map(world::getCharacterById)
                .filter(Objects::nonNull)
                .forEach(target -> target.write(packet));
    }

}
