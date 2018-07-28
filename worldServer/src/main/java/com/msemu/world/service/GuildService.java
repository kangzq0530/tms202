/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.service;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.guild.Guild;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@StartupComponent("Service")
public class GuildService {

    private static final Logger log = LoggerFactory.getLogger(GuildService.class);

    private static final AtomicReference<GuildService> instance = new AtomicReference<>();

    @Getter(AccessLevel.PRIVATE)
    private Map<Integer, Guild> guilds = new HashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private Map<Integer, String> reservedGuildNames = new HashMap<>();

    private GuildService() {
        try (Session session = DatabaseFactory.getInstance().getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Guild> query = builder.createQuery(Guild.class);
            Root<Guild> root = query.from(Guild.class);
            query.select(root);
            session.createQuery(query).getResultList().forEach(guild -> {
                getGuilds().put(guild.getId(), guild);
            });
            log.info(String.format("%d guilds are loaded.", guilds.size()));
        }
    }

    public static GuildService getInstance() {
        GuildService value = instance.get();
        if (value == null) {
            synchronized (GuildService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new GuildService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public Guild getGuildById(final int id) {
        return getGuilds().getOrDefault(id, null);
    }

    public Guild getGuildByName(final String name) {
        return getGuilds().values().stream()
                .filter(guild -> guild.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean isGuildNameExists(final String name) {
        return getGuildByName(name) != null;
    }

    public boolean isGuildNameLegal(String name) {
        return name.getBytes().length >= 3 && name.getBytes().length <= 12;
    }

    public void addReservedGuildName(final Character chr, final String guildName) {
        getReservedGuildNames().put(chr.getId(), guildName);
    }

    public boolean hasReservedGuildName(final Character chr) {
        return getReservedGuildNames().containsKey(chr.getId());
    }

    public String getReservedGuildName(final Character chr) {
        return getReservedGuildNames().getOrDefault(chr.getId(), null);
    }

    public void removeRerservedGuildName(final Character chr) {
        getReservedGuildNames().remove(chr.getId());
    }

    public Guild createNewGuild(Character chr, String guildName) {
        Guild guild = new Guild();
        guild.setLevel(1);
        guild.setName(guildName);
        guild.addMember(chr);
        guild.setWorldID(chr.getClient().getWorld().getWorldId());
        DatabaseFactory.getInstance().saveToDB(guild);
        getGuilds().put(guild.getId(), guild);
        return guild;
    }
}
