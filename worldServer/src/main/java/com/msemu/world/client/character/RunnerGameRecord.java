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

package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/14.
 */
public class RunnerGameRecord {
    private FileTime lastPlayed = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    private int characterID;
    private int lastScore;
    private int highScore;
    private int runnerPoint;
    private int totalLeft;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getCharacterID());
        outPacket.encodeInt(getLastScore());
        outPacket.encodeInt(getHighScore());
        outPacket.encodeInt(getRunnerPoint());
        outPacket.encodeFT(getLastPlayed());
        outPacket.encodeInt(getTotalLeft());
    }

    public FileTime getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(FileTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getRunnerPoint() {
        return runnerPoint;
    }

    public void setRunnerPoint(int runnerPoint) {
        this.runnerPoint = runnerPoint;
    }

    public int getTotalLeft() {
        return totalLeft;
    }

    public void setTotalLeft(int totalLeft) {
        this.totalLeft = totalLeft;
    }
}
