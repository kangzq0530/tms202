package com.msemu.world.client.guild;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@Table(name = "guilds")
@Getter
@Setter
public class Guild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "leaderID")
    private int leaderID;
    @Column(name = "worldID")
    private int worldID;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "guildrequestors", joinColumns = @JoinColumn(name = "guildID"))
    private List<GuildRequestor> requestors = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "gradeNames", joinColumns = @JoinColumn(name = "guildID"))
    @Column(name = "gradeName")
    private List<String> gradeNames = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "guildID")
    private List<GuildMember> members = new ArrayList<>();
    @Column(name = "markBg")
    private int markBg;
    @Column(name = "markBgColor")
    private int markBgColor;
    @Column(name = "mark")
    private int mark;
    @Column(name = "markColor")
    private int markColor;
    @Column(name = "maxMembers")
    private int maxMembers;
    @Column(name = "notice")
    private String notice;
    @Column(name = "points")
    private int points;
    @Column(name = "seasonPoints")
    private int seasonPoints;
    @Column(name = "allianceID")
    private int allianceID;
    @Column(name = "level")
    private int level;
    @Column(name = "rank")
    private int rank;
    @Column(name = "ggp")
    private int ggp;
    @Column(name = "appliable")
    private boolean appliable;
    @Column(name = "joinSetting")
    private int joinSetting;
    @Column(name = "reqLevel")
    private int reqLevel;


    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "guildskills")
    @MapKeyColumn(name = "skillID")
    private Map<Integer, GuildSkill> skills = new HashMap<>();

    public Guild() {
        setGradeNames(Arrays.asList("Guild Master", "Junior", "Veteran", "Member", "Newbie"));
        setAppliable(true);
        setMaxMembers(10);
        setName("Default guild");
    }

    public static void defaultEncodeForRemote(OutPacket outPacket) {
        outPacket.encodeString("");
        outPacket.encodeShort(0);
        outPacket.encodeByte(0);
        outPacket.encodeShort(0);
        outPacket.encodeByte(0);
    }

    public void encodeForRemote(OutPacket outPacket) {
        outPacket.encodeString(getName());
        outPacket.encodeShort(getMarkBg());
        outPacket.encodeByte(getMarkBgColor());
        outPacket.encodeShort(getMark());
        outPacket.encodeByte(getMarkColor());
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getId());
        outPacket.encodeString(getName());
        // 5 times total
        getGradeNames().forEach(outPacket::encodeString);
        outPacket.encodeShort(getMembers().size());
        getMembers().forEach(gm -> outPacket.encodeInt(gm.getCharID()));
        getMembers().forEach(gm -> gm.encode(outPacket));
        outPacket.encodeShort(getRequestors().size());
        getRequestors().forEach(gm -> outPacket.encodeInt(gm.getCharID()));
        getRequestors().forEach(gm -> gm.encode(outPacket));
        outPacket.encodeInt(getMaxMembers());
        outPacket.encodeShort(getMarkBg());
        outPacket.encodeByte(getMarkBgColor());
        outPacket.encodeShort(getMark());
        outPacket.encodeByte(getMarkColor());
        outPacket.encodeString(getNotice());
        outPacket.encodeInt(getPoints());
        outPacket.encodeInt(getSeasonPoints());
        outPacket.encodeInt(getAllianceID());
        outPacket.encodeByte(getLevel());
        outPacket.encodeShort(getRank());
        outPacket.encodeInt(getGgp());
        outPacket.encodeShort(getSkills().size());
        getSkills().forEach((id, skill) -> {
            outPacket.encodeInt(id);
            skill.encode(outPacket);
        });
        outPacket.encodeByte(isAppliable());
        if (isAppliable()) {
            outPacket.encodeByte(getJoinSetting());
            outPacket.encodeInt(getReqLevel());
        }
    }

    public void addMember(GuildMember guildMember) {
        getMembers().add(guildMember);
        if (guildMember.getCharacter() != null && guildMember.getCharacter().getGuild() == null) {
            guildMember.getCharacter().setGuild(this);
        }
        if (getLeaderID() == 0) {
            setLeader(guildMember);
        } else {
            guildMember.setGrade(getGradeNames().size());
        }
    }

    public void demote(GuildMember guildMember) {
        guildMember.setGrade(Math.min(guildMember.getGrade() + 1, getGradeNames().size()));
    }

    public void promote(GuildMember guildMember) {
        guildMember.setGrade(Math.max(guildMember.getGrade() - 1, 1));
    }

    public void addMember(Character chr) {
        addMember(new GuildMember(chr));
    }

    public void removeMember(GuildMember guildMember) {
        if (guildMember.getCharacter() != null) {
            guildMember.getCharacter().setGuild(null);
        }
        getMembers().remove(guildMember);
    }

    public void removeMember(Character chr) {
        getMembers().remove(getMemberByChar(chr));
    }

    public GuildMember getMemberByChar(Character chr) {
        return getMembers().stream().filter(gm -> gm.getCharacter().equals(chr)).findAny().orElse(null);
    }

    public void setLeader(GuildMember leader) {
        int oldGrade = leader.getGrade();
        if (getLeaderID() != 0) {
            getMemberByID(getLeaderID()).setGrade(oldGrade);
        }
        this.leaderID = leader.getCharID();
        leader.setGrade(1);
    }

    public boolean isGuildMember(GuildMember gm) {
        return getLeaderID() == gm.getCharID();
    }

    public GuildMember getMemberByID(int id) {
        return getMembers().stream().filter(gm -> gm.getCharID() == id).findAny().orElse(null);
    }

    public List<GuildMember> getOnlineMembers() {
        return getMembers().stream().filter(GuildMember::isOnline).collect(Collectors.toList());
    }

    public void broadcast(OutPacket outPacket) {
        getOnlineMembers().forEach(gm -> {
            System.out.println(gm.getCharacter() == null ? "Null!" : gm.getCharacter().getName() + ", " + gm.isOnline() + ", " + gm.getCharacter());
        });
        getOnlineMembers().stream().filter(gm -> gm.getCharacter() != null).forEach(gm -> gm.getCharacter().write(outPacket));
    }

    public void broadcast(OutPacket outPacket, Character exceptChr) {
        getOnlineMembers().stream().filter(gm -> !gm.getCharacter().equals(exceptChr)).forEach(gm -> gm.getCharacter().write(outPacket));
    }

    public void addGuildSkill(GuildSkill gs) {
        getSkills().put(gs.getSkillID(), gs);
    }
}