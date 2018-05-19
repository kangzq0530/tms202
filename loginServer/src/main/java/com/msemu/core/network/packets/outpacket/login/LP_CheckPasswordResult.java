package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.Account;
import com.msemu.login.constants.JobConstants;
import com.msemu.login.enums.CharCreateInfo;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_CheckPasswordResult extends OutPacket<LoginClient> {

    public LP_CheckPasswordResult(Account account, LoginResultCode status, FileTime dtUnblockDate) {
        super(OutHeader.LP_CheckPasswordResult);
        encodeByte(status.getValue());
        if (status == LoginResultCode.LoginSuccess || status == LoginResultCode.AuthFail) {
            encodeString(account.getUsername());
            encodeInt(account.getId());
            encodeByte(account.getGender());
            encodeByte(account.getMsg2());
            encodeInt(account.getAccountType());
            encodeInt(account.getAge());
            encodeInt(0);
            encodeInt(0x23);
            encodeByte(account.getCharacterSlots() + 3);
            encodeByte(account.getPBlockReason());
            encodeLong(account.getChatUnblockDate());
            encodeByte(0);
            encodeLong(account.getChatUnblockDate());
            encodeByte(0);
            encodeString(account.getSecureUserName());
            encodeString("");
            encodeByte(JobConstants.enableJobs);
            if (JobConstants.enableJobs) {
                encodeByte(JobConstants.jobOrder);
                for(CharCreateInfo job : CharCreateInfo.values()) {
                    encodeByte(job.enableCreate());
                    encodeShort(1);
                }
            }
            encodeByte(1);
            encodeInt(-1);
            encodeByte(0);
        } else if (status == LoginResultCode.BlockForIndecencyBehavior) {
            encodeByte(dtUnblockDate != null);
            encodeFT(dtUnblockDate);
        }
    }
}
