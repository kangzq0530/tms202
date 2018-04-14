package com.msemu.login.client;

/**
 * Created by Weber on 2018/4/13.
 */

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.PicStatus;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Schema
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
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
    @Column(name = "pic")
    private String pic;
    @Column(name = "characterSlots")
    private int characterSlots;
    @Column(name = "creationDate")
    private long creationDate;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "accId")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy(value = "characterPos ASC")
    private List<Character> characters = new ArrayList<>();

    public Account() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public final String getSecureUserName() {
        StringBuilder sb = new StringBuilder(username);
        if (sb.length() >= 4) {
            sb.replace(1, 3, "**");
        } else if (sb.length() >= 3) {
            sb.replace(1, 2, "*");
        }
        if (sb.length() > 4) {
            sb.replace(sb.length() - 1, sb.length(), "*");
        }
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public static Account findById(int id) {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root).where(builder.equal(root.get("id"), id));
        Account result = session.createQuery(query).getSingleResult();
        session.clear();
        return result;
    }

    public static Account findByUserName(String username) {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root);
        query.where(builder.equal(root.get("username"), username));
        List<Account> result = session.createQuery(query).getResultList();
        session.clear();
        return result.size() > 0 ? result.get(0) : null;
    }


    public PicStatus getPicStatus() {
        PicStatus picStatus;
        String pic = getPic();
        if(pic == null || pic.length() == 0) {
            picStatus = PicStatus.CREATE_PIC;
        } else {
            picStatus = PicStatus.ENTER_PIC;
        }
        return picStatus;
    }
}
