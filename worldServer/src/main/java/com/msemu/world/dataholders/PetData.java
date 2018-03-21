package com.msemu.world.dataholders;

import com.msemu.commons.data.provider.MapleDataProviderFactory;
import com.msemu.commons.data.provider.interfaces.MapleDataDirectoryEntry;
import com.msemu.commons.data.provider.interfaces.MapleDataProvider;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.model.items.pet.templactes.PetInfoT;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/21.
 */
@Reloadable(name = "pet", group = "wz")
@StartupComponent("Data")
public class PetData implements IReloadable {

    private static final AtomicReference<PetData> instance = new AtomicReference<>();

    private  final HashMap<Integer, PetInfoT> petsInfo = new HashMap<>();

    private PetData() {
        load();
    }

    public static PetData getInstance() {
        PetData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new PetData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    private void load() {
        ServerInfoUtils.printSection("PetsData Loading");
        MapleDataProvider itemWz = MapleDataProviderFactory.getDataProvider("Item.wz");
        MapleDataDirectoryEntry pets = (MapleDataDirectoryEntry) itemWz.getRoot().getEntry("Pet");
        pets.getFiles().forEach(petImages-> {
            PetInfoT petInfoT = new PetInfoT(itemWz.getData("Pet/" + petImages.getName()));
            petsInfo.put(petInfoT.getItemID(), petInfoT);
        });
    }

    @Override
    public void reload() {
        this.petsInfo.clear();
        load();
    }

    public PetInfoT getPetInfo(int itemID) {
        return petsInfo.get(itemID);
    }
}
