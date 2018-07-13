package com.msemu.world.service;


import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.guild.Guild;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@StartupComponent("Service")
public class PartyService {

    private static final AtomicReference<PartyService> instance = new AtomicReference<>();

    private final Map<Integer, Party> parties = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> invitations = new ConcurrentHashMap<>();


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

    public boolean registerParty(Party party) {
        if(this.parties.containsKey(party.getPartyLeaderId())) {
            return false;
        } else {
            this.parties.put(party.getPartyLeaderId(), party);
            return true;
        }
    }

    public boolean unregisterParty(Party party) {
        if(this.parties.containsKey(party.getPartyLeaderId())) {
            this.parties.remove(party.getPartyLeaderId());
            return true;
        }
        return false;
    }

    public void addInvitation(Character invited, Party toParty) {
        this.invitations.put(invited.getId(), toParty.getId());
    }

    public void removeInvitation(Character chr) {
        this.invitations.remove(chr.getId());
    }

    public boolean hasInvitation(Character chr, Party party) {
        return this.invitations.containsKey(chr.getId())
                && this.invitations.get(chr.getId()) == party.getId();
    }

    public Party getPartyById(int partyId) {
        return getAllParties().values().stream().
                filter(party -> party.getId() == partyId)
                .findFirst().orElse(null);
    }

    public Party getPartyByLeaderId(int leaderId) {
        return this.parties.getOrDefault(leaderId, null);
    }

    public void updatePartyLeader(int oldLeaderId, Party party) {
        if(this.parties.containsKey(oldLeaderId)) {
            this.parties.remove(oldLeaderId);
            this.parties.put(party.getPartyLeaderId(), party);
        }
    }

    public Party findParty(int id) {
        return getAllParties().values()
                .stream()
                .filter(party -> party.getMemberById(id) != null)
                .findFirst().orElse(null);
    }
}
