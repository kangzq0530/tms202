package com.msemu.world;

import com.msemu.commons.rmi.model.ChannelInfo;
import com.msemu.commons.thread.EventManager;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.configs.WorldConfig;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.FieldData;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
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
    private final List<Field> fields;
    private final Map<Integer, Character> transfers; // key -> accountId
    private final Map<Integer, Character> characters; // key -> characterId
    @Getter
    @Setter
    private int port;
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
        EventManager.getInstance().addFixedDelayEvent(this::updateField, 0, 1000);
    }

    private void updateField() {
        try {
            ArrayList<Field> newFieldsAry = new ArrayList<>(this.getFields());
            newFieldsAry.forEach(field -> {
                field.update(LocalDateTime.now());
            });
        } catch (Exception ex) {
            LoggerFactory.getLogger(Channel.class).error("update error", ex);
        }
    }

    public Field getField(int fieldId) {
        for (Field field : getFields()) {
            if (field.getId() == fieldId) {
                return field;
            }
        }
        return createAndReturnNewField(fieldId);
    }

    private Field createAndReturnNewField(int templateId) {
        Field newField = FieldData.getInstance().getFieldFromTemplate(templateId);
        if (newField == null)
            return null;
        fields.add(newField);
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
        if (chr == null)
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

    public Character getCharacterByName(String charName) {
        return getCharactersList().stream()
                .filter(chr -> chr.getName().equalsIgnoreCase(charName)).findFirst().orElse(null);
    }
}
