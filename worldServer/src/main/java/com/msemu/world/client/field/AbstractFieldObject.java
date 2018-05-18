package com.msemu.world.client.field;

import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/13.
 */
public abstract class AbstractFieldObject {

    @Getter
    private final Position position = new Position();

    @Getter
    private final Position oldPosition = new Position();

    @Getter
    @Setter
    private int objectId;

    public abstract FieldObjectType getFieldObjectType();

    public abstract void enterScreen(GameClient client);

    public abstract void outScreen(GameClient client);

    public void setPosition(Position position) {
        getPosition().setX(position.getX());
        getPosition().setY(position.getY());
    }

    public void setOldPosition(Position position) {
        getOldPosition().setX(position.getX());
        getOldPosition().setY(position.getY());
    }

}
