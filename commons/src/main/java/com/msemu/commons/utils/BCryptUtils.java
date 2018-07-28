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

package com.msemu.commons.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by Weber on 2018/3/30.
 */
public class BCryptUtils {

    private static int workload = 12;

    /**
     * This method can be used to generate a string representing an account password
     * suitable for storing in a database. It will be an OpenBSD-style crypt(3) formatted
     * hash string of length=60
     * The bcrypt workload is specified in the above static variable, a value from 10 to 31.
     * A workload of 12 is a very reasonable safe default as of 2013.
     * This automatically handles secure 128-bit salt generation and storage within the hash.
     *
     * @param password_plaintext The account's plaintext password as provided during account creation,
     *                           or when changing an account's password.
     * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
     */
    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        return (BCrypt.hashpw(password_plaintext, salt));
    }

    /**
     * This method can be used to verify a computed hash from a plaintext (e.g. during a login
     * request) with that of a stored hash from a database. The password hash from the database
     * must be passed as the second variable.
     *
     * @param password_plaintext The account's plaintext password, as provided during a login request
     * @param stored_hash        The account's stored password hash, retrieved from the authorization database
     * @return boolean - true if the password matches the password of the stored hash, false otherwise
     */
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        if (null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
        return (password_verified);
    }
}
