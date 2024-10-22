package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class CryptUtil {


    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public  boolean checkPassword(String password, String hashed){
        return BCrypt.checkpw(password,hashed);
    }
}
