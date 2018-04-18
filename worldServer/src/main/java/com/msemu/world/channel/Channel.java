package com.msemu.world.channel;

import com.msemu.commons.rmi.model.ChannelInfo;
import com.msemu.core.configs.WorldConfig;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.FieldData;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/18.
 */

public class Channel {

    private int port;
    private String name;
    private int gaugePx, worldId, channelId;
    private boolean adultChannel;
    private List<Field> fields;
    private Map<Integer, Character> transfers = new ConcurrentHashMap<>();
    private Map<Integer, Character> characters = new ConcurrentHashMap<>();

    public Channel(String worldName, int worldId, int channelId) {
        this.name = worldName + "-" + channelId;
        this.gaugePx = 0;
        this.worldId = worldId;
        this.channelId = channelId;
        this.adultChannel = false;
        this.port = WorldConfig.PORT_START + 100 + channelId;
        this.fields = new ArrayList<>();
        this.transfers = new HashMap<>();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getField(int id) {
        return getFields().stream().filter(field -> field.getId() == id).findFirst().orElse(createAndReturnNewField(id));
    }

    private Field createAndReturnNewField(int id) {
        Field newField = FieldData.getFieldCopyById(id);
        getFields().add(newField);
        return newField;
    }

    public boolean isAdultChannel() {
        return adultChannel;
    }

    public void setAdultChannel(boolean adultChannel) {
        this.adultChannel = adultChannel;
    }

    public int getGaugePx() {
        return gaugePx;
    }

    public void setGaugePx(int gaugePx) {
        this.gaugePx = gaugePx;
    }

    public int getWorldId() {
        return worldId;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public boolean isAccountOnPending(int accountId) {
        return getPendingByAccId(accountId) != null;
    }

    @Synchronized
    public List<Character> getPendings() {
        return transfers.values().stream()
                .collect(Collectors.toList());
    }

    public Character getPendingByAccId(int accountId) {
        return getPendings().stream()
                .filter(chr -> chr.getAccId() == accountId)
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
        return channelInfo;
    }

    public boolean isAccountOnChannel(int accountId) {
        return getCharacterByAccId(accountId) != null;
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character.getAccId());
    }
}
