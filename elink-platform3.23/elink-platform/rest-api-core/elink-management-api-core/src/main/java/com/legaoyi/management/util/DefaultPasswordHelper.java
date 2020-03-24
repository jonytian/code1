package com.legaoyi.management.util;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.legaoyi.common.util.PasswordHelper;
import com.legaoyi.platform.model.User;

public class DefaultPasswordHelper extends PasswordHelper {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPasswordHelper.class);

    private static final int SALT_LENGTH = 16;

    public static void encryptPassword(User user) throws Exception {
        user.setSalt(Base64Utils.encodeToString(nextSalt()));
        String newPassword = SHA1(user.getPassword(), user.getSalt());
        user.setPassword(newPassword);
    }

    private static byte[] nextSalt() {
        byte[] salt = new byte[SALT_LENGTH];

        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
        } catch (Exception e) {
            logger.error("nextSalt error", e);
        }
        return salt;
    }
}
