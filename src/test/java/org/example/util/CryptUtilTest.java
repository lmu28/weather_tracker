package org.example.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CryptUtilTest {

    public String password = "12345678";

    public CryptUtil cryptUtil = new CryptUtil();


    @Test
    void checkPassword() {
        String hash = cryptUtil.hashPassword(password);
        boolean matches = cryptUtil.checkPassword(password, hash);
        assertThat(matches).isTrue();
    }
}