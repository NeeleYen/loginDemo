package com.example.login.util;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//參考：https://yen0304.github.io/p/jwtjson-web-token%E5%8E%9F%E7%90%86%E4%BB%8B%E7%B4%B9%E5%AF%A6%E4%BD%9Cjws/

public class JWTUtil {

    public static final long EXPIRE_TIME = 30 * 60 * 1000;  //預設token在30分後過期
    //私有密鑰
    public static final String TOKEN_SECRET = "cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=";

    /**
     * 生成token
     * 
     * @param String username 用戶名
     * @param String jwtId jwt識別碼
     * @param Long validMillis token有效時間，預設30分鐘
     * @return token字串
     */
    public String creatJWT(String username, String jwtId, Long validMillis) {

        
        //獲取當前時間
        long currentMillis = System.currentTimeMillis();
        Date now = new Date(currentMillis); 
        
        //預設過期時間為30分鐘
        if (validMillis == null) {
            validMillis = JWTUtil.EXPIRE_TIME;
        }
        
        //過期時間點
        long expireMillis = currentMillis + validMillis;
        Date expirDate = new Date(expireMillis);
        
        //產生密鑰
        Key secretKey = generalKey();
        
        //密鑰加密演算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        //產生jws
        JwtBuilder builder = Jwts.builder()
        .setId(jwtId)
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expirDate)
        .signWith(secretKey, signatureAlgorithm);

        return builder.compact();
    }

    /**
     * 解析JWT
     * 
     * @param String jwtString 要解析的token
     * @return Claims 聲明內容的物件
     */
    public Claims parseJWT(String jwtString){

        //和生成token密鑰一樣的密鑰
        Key secretKey = generalKey();

        Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(jwtString)
        .getBody();

        return claims;
    }

    //產生密鑰
    public Key generalKey(){
        
        byte[] encodeKey = Decoders.BASE64.decode(JWTUtil.TOKEN_SECRET);
        Key key= Keys.hmacShaKeyFor(encodeKey);
        return key;

    }


}
