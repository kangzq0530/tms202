package com.msemu.world.service;


import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.guild.Guild;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@StartupComponent("Service")
public class PartyService {

    private final Map<Integer, Party> parties = new ConcurrentHashMap<>();

    private static final AtomicReference<PartyService> instance = new AtomicReference<>();

    public static PartyService getInstance() {
        PartyService value = instance.get();
        if (value == null) {
            synchronized (PartyService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new PartyService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public Map<Integer, Party> getAllParties() {
        return Collections.unmodifiableMap(parties);
    }




    public Party createNewParty() {
        Party party = new Party();
        this.parties.put(party.getId(), party);
        return party;
    }

    public boolean removeParty(Party party) {
        if(this.parties.containsKey(party.getId())) {
            this.parties.remove(party.getId());
            return true;
        }
        return false;
    }

    public Party getPartyById(int partyId) {
        return this.parties.getOrDefault(partyId, null);
    }

}
