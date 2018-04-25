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
    private Map<Integer, Character> transfers; // key -> accountId
    private Map<Integer, Character> characters; // key -> accountId

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

    private Field createAndReturnNewField(int templateId) {
        Field newField = FieldData.getInstance().getFieldFromTemplate(templateId);
        getFields().add(newField);
        return newField;
    }

    public List<Character> getTransferList() {
        return getTransfers().values().stream()
                .collect(Collectors.toList());
    }

    @Synchronized
    private Map<Integer, Character> getTransfers() {
        return transfers;
    }

    @Synchronized
    public Character getTransferIdAndRemoveByCharacterId(int characterId) {
        Character chr = getTransferList().stream()
                .filter(c -> c.getId() == characterId)
                .findFirst().orElse(null);
        getTransfers().remove(chr.getAccId());
        return chr;
    }

    public List<Character> getCharactersList() {
        return characters.values().stream().collect(Collectors.toList());
    }

    @Synchronized
    public Map<Integer, Character> getCharacters() {
        return characters;
    }

    public Character getCharacterByAccId(int accountId) {
        return getCharactersList().stream()
                .filter(chr -> chr.getAccId() == accountId)
                .findFirst().orElse(null);
    }

    public ChannelInfo getChannelInfo() {
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setName(getName());
        channelInfo.setChannel(getChannelId());
        channelInfo.setConnectedClients(getCharactersList().size());
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

    @Synchronized
    public void addTransfer(int accountId, int characterId) {
        Character chr = Character.findById(characterId);
        if(chr == null)
            return;
        getTransfers().put(accountId, chr);
    }

    public void addCharacter(Character chr) {
        getCharacters().put(chr.getAccId(), chr);
    }

    public boolean hasTransferByCharacterId(int characterId) {
        return getTransferList()
                .stream().filter(chr -> chr.getId() == characterId)
                .count() > 0;
    }
}
