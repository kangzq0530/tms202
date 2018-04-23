package com.msemu.world.channel;

import com.msemu.commons.rmi.model.ChannelInfo;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.configs.WorldConfig;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.FieldData;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/18.
 */

public class Channel {

    @Getter
    @Setter
    private short port;
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int gaugePx, worldId, channelId;
    @Getter
    @Setter
    private boolean adultChannel;
    @Getter
    private List<Field> fields;
    private Map<Integer, Character> transfers;
    private Map<Integer, Character> characters;

    public Channel(String worldName, int worldId, int channelId) {
        this.name = worldName + "-" + channelId;
        this.gaugePx = 0;
        this.worldId = worldId;
        this.channelId = channelId;
        this.adultChannel = false;
        this.port = (short) (NetworkConfig.PORT + 100 + channelId);
        this.host = NetworkConfig.HOST;
        this.fields = new ArrayList<>();
        this.characters = new HashMap<>();
        this.transfers = new HashMap<>();
    }

    public Field getField(int id) {
        return getFields().stream().filter(field -> field.getId() == id).findFirst().orElse(createAndReturnNewField(id));
    }

    private Field createAndReturnNewField(int id) {
        Field newField = FieldData.getInstance().getFieldFromTemplate(id);
        getFields().add(newField);
        return newField;
    }

    public boolean isAccountTransfered(int accountId) {
        return getTransferByAccountId(accountId) != null;
    }

    @Synchronized
    public List<Character> getTransferCharacters() {
        return transfers.values().stream()
                .collect(Collectors.toList());
    }

    public Character getTransferByAccountId(int accountId) {
        return getTransferCharacters().stream()
                .filter(chr -> chr.getAccId() == accountId)
                .findFirst()
                .orElse(null);
    }

    public Character getTransferByCharacterId(int characterId) {
        return getTransferCharacters().stream()
                .filter(chr -> chr.getId() == characterId)
                .findFirst()
                .orElse(null);
    }

    public List<Character> getCharacters() {
        return characters.values().stream().collect(Collectors.toList());
    }

    public Character getCharacterByAccId(int accountId) {
        return getCharacters().stream()
                .filter(chr -> chr.getAccId() == accountId)
                .findFirst().orElse(null);
    }

    public ChannelInfo getChannelInfo() {
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setName(getName());
        channelInfo.setChannel(getChannelId());
        channelInfo.setConnectedClients(getCharacters().size());
        channelInfo.setMaxConnectedClients(WorldConfig.CHANNEL_MAX_LOADING);
        channelInfo.setWorldId(getWorldId());
        channelInfo.setHost(getHost());
        channelInfo.setPort(getPort());
        return channelInfo;
    }

    public boolean isAccountOnChannel(int accountId) {
        return getCharacterByAccId(accountId) != null;
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character.getAccId());
    }

    public void addTransfer(int accountId, int characterId) {
        Character character = Character.findById(characterId);
        if(character != null) {
            getTransferCharacters().add(character);
        }
    }
}
