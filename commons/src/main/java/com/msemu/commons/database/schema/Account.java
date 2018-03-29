package com.msemu.commons.database.schema;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "accounts")
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "accountTypeMask")
    private int accountType;
    @Column(name = "age")
    private int age;
    @Column(name = "vipGrade")
    private int vipGrade;
    @Column(name = "nBlockReason")
    private int nBlockReason;
    @Column(name = "gmLevel")
    private int gmLevel;
    @Column(name = "gender")
    private byte gender;
    @Column(name = "msg2")
    private byte msg2;
    @Column(name = "purchaseExp")
    private byte purchaseExp;
    @Column(name = "pBlockReason")
    private byte pBlockReason;
    @Column(name = "gradeCode")
    private byte gradeCode;
    @Column(name = "chatUnblockDate")
    private long chatUnblockDate;
    @Column(name = "hasCensoredNxLoginID")
    private boolean hasCensoredNxLoginID;
    @Column(name = "censoredNxLoginID")
    private String censoredNxLoginID;
    @Column(name = "pic")
    private String pic;
    @Column(name = "characterSlots")
    private int characterSlots;
    @Column(name = "creationDate")
    private long creationDate;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "accId")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Character> characters = new ArrayList<>();

    public Account() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(int vipGrade) {
        this.vipGrade = vipGrade;
    }

    public int getnBlockReason() {
        return nBlockReason;
    }

    public void setnBlockReason(int nBlockReason) {
        this.nBlockReason = nBlockReason;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel) {
        this.gmLevel = gmLevel;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getMsg2() {
        return msg2;
    }

    public void setMsg2(byte msg2) {
        this.msg2 = msg2;
    }

    public byte getPurchaseExp() {
        return purchaseExp;
    }

    public void setPurchaseExp(byte purchaseExp) {
        this.purchaseExp = purchaseExp;
    }

    public byte getpBlockReason() {
        return pBlockReason;
    }

    public void setpBlockReason(byte pBlockReason) {
        this.pBlockReason = pBlockReason;
    }

    public byte getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(byte gradeCode) {
        this.gradeCode = gradeCode;
    }

    public long getChatUnblockDate() {
        return chatUnblockDate;
    }

    public void setChatUnblockDate(long chatUnblockDate) {
        this.chatUnblockDate = chatUnblockDate;
    }

    public boolean isHasCensoredNxLoginID() {
        return hasCensoredNxLoginID;
    }

    public void setHasCensoredNxLoginID(boolean hasCensoredNxLoginID) {
        this.hasCensoredNxLoginID = hasCensoredNxLoginID;
    }

    public String getCensoredNxLoginID() {
        return censoredNxLoginID;
    }

    public void setCensoredNxLoginID(String censoredNxLoginID) {
        this.censoredNxLoginID = censoredNxLoginID;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCharacterSlots() {
        return characterSlots;
    }

    public void setCharacterSlots(int characterSlots) {
        this.characterSlots = characterSlots;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character character) {
        getCharacters().add(character);
    }

    public Account findById(int id) {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root).where(builder.equal(root.get("id"), id));
        Account result = session.createQuery(query).getSingleResult();
        session.clear();
        return result;
    }
}
