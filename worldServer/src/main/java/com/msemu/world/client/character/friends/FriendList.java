package com.msemu.world.client.character.friends;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    public FriendEntry getByCharId(int charID) {
        return friendEntries
                .stream().filter(f -> f.getCharId() == charID)
                .findFirst().orElse(null);
    }

    public FriendEntry getByAccountId(int accountID) {
        return friendEntries
                .stream().filter(f -> f.getAccountID() == accountID)
                .findFirst().orElse(null);
    }
}
