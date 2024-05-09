package com.example.login;

import org.mindrot.jbcrypt.BCrypt;

public class createPwd {

    public static void main(String[] args) {
        String saltForJohn = BCrypt.gensalt();
        String hashPwdJohn = BCrypt.hashpw("aaa", saltForJohn);

        String saltForAlice = BCrypt.gensalt();
        String hashPwdAlice = BCrypt.hashpw("bbb", saltForAlice);

        System.out.println("saltForJohn：" + saltForJohn);
        System.out.println("hashPwdJohn：" + hashPwdJohn);
        System.out.println("saltForAlice：" + saltForAlice);
        System.out.println("hashPwdAlice：" + hashPwdAlice);
    }

}
