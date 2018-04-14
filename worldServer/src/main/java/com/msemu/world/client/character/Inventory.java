package com.msemu.world.client.character;

import com.msemu.world.client.character.items.Item;
import com.msemu.world.enums.BodyPart;
import com.msemu.world.enums.InvType;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventoryId")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Item> items;
    @Column(name = "type")
    private InvType type;
    @Column(name = "slots")
    private byte slots;

    public Inventory() {
        items = new ArrayList<>();
        type = InvType.EQUIP;
    }

    public Inventory(InvType t, int slots) {
        this.type = t;
        items = new ArrayList<>();
        this.slots = (byte) slots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getSlots() {
        return slots;
    }

    public void setSlots(byte slots) {
        this.slots = slots;
    }

    public void addItem(Item item) {
        if (getItems().size() <= getSlots()) {
            getItems().add(item);
            item.setInvType(getType());
            sortItemsByIndex();
        }
    }

    public void removeItem(Item item) {
        if (getItems().contains(item)) {
            getItems().remove(item);
            sortItemsByIndex();
        }
    }

    public int getFirstOpenSlot() {
        for (int i = 1; i <= getSlots(); i++) {
            if (getItemBySlot(i) == null) {
                return i;
            }
        }
        return 0;
    }

    public Item getFirstItemByBodyPart(BodyPart bodyPart) {
        List<Item> items = getItemsByBodyPart(bodyPart);
        return items != null && items.size() > 0 ? items.get(0) : null;
    }

    public List<Item> getItemsByBodyPart(BodyPart bodyPart) {
        return getItems().stream().filter(item -> item.getBagIndex() == bodyPart.getValue()).collect(Collectors.toList());
    }

    public List<Item> getItems() {
        return items;
    }

    public void sortItemsByIndex() {
        getItems().sort(Comparator.comparingInt(Item::getBagIndex));
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public InvType getType() {
        return type;
    }

    public void setType(InvType type) {
        this.type = type;
    }

    public Item getItemBySlot(short bagIndex) {
        return getItemBySlot(bagIndex < 0 ? -bagIndex : bagIndex);
    }

    private Item getItemBySlot(int bagIndex) {
        return getItems().stream().filter(item -> item.getBagIndex() == bagIndex).findAny().orElse(null);
    }

    public Item getItemByItemID(int itemId) {
        return getItems().stream().filter(item -> item.getItemId() == itemId).findFirst().orElse(null);
    }

    public boolean containsItem(int itemID) {
        return getItems().stream().anyMatch(item -> item.getItemId() == itemID);
    }

    public boolean canPickUp(Item item) {
        return !isFull() || (item.getInvType().isStackable() && getItemByItemID(item.getItemId()) != null);
    }

    private boolean isFull() {
        return getItems().size() >= getSlots();
    }
}
