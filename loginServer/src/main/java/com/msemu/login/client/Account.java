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

package com.msemu.login.client;

/**
 * Created by Weber on 2018/4/13.
 */

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.PicStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Schema
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    @Column(name = "username")
    private String username;
    @Getter
    @Setter
    @Column(name = "password")
    private String password;
    @Getter
    @Setter
    @Column(name = "accountTypeMask")
    private int accountType;
    @Getter
    @Setter
    @Column(name = "age")
    private int age;
    @Getter
    @Setter
    @Column(name = "vipGrade")
    private int vipGrade;
    @Getter
    @Setter
    @Column(name = "nBlockReason")
    private int nBlockReason;
    @Getter
    @Setter
    @Column(name = "gmLevel")
    private int gmLevel;
    @Getter
    @Setter
    @Column(name = "gender")
    private byte gender;
    @Getter
    @Setter
    @Column(name = "msg2")
    private byte msg2;
    @Getter
    @Setter
    @Column(name = "purchaseExp")
    private byte purchaseExp;
    @Column(name = "pBlockReason")
    @Getter
    @Setter
    private byte pBlockReason;
    @Column(name = "gradeCode")
    @Getter
    @Setter
    private byte gradeCode;
    @Column(name = "chatUnblockDate")
    @Getter
    @Setter
    private long chatUnblockDate;
    @Getter
    @Setter
    @Column(name = "pic")
    private String pic;
    @Column(name = "characterSlots")
    @Getter
    @Setter
    private int characterSlots;
    @Column(name = "creationDate")
    @Getter
    @Setter
    private long creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "accId")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy(value = "characterPos ASC")
    private Set<Character> characters = new HashSet<>();

    public Account() {

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

    public Set<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character character) {
        getCharacters().add(character);
    }

    public static Account findById(int id) {
        return (Account) DatabaseFactory.getInstance().getObjFromDB(Account.class, id);
    }

    public static Account findByUserName(String username) {
        List<Account> result;
        try (Session session = DatabaseFactory.getInstance().getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = builder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            query.where(builder.equal(root.get("username"), username));
            result = session.createQuery(query).getResultList();
        }
        return result != null && result.size() > 0 ? result.get(0) : null;
    }


    public PicStatus getPicStatus() {
        PicStatus picStatus;
        String pic = getPic();
        if (pic == null || pic.length() == 0) {
            picStatus = PicStatus.CREATE_PIC;
        } else {
            picStatus = PicStatus.ENTER_PIC;
        }
        return picStatus;
    }
}
