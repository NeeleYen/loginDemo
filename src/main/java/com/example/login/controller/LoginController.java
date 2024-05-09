package com.example.login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.login.dto.LoginDTO;
import com.example.login.model.User;
import com.example.login.service.UserService;
import com.example.login.util.JWTUtil;

@Controller
public class LoginController {

    @Autowired UserService userService;

    /**
     * 跳到登入頁
    */
    @GetMapping("/")
    public String loginPage(){
        
        return "index";
    }

    /**
     * 如果不是登入成功的情況，則導回登入頁，並顯示訊息
     * 
     * @param String message 登入失敗顯示的訊息
     * @param Model model
    */
    @GetMapping("/loginFail")
    public String loginFail(String message, Model model){
        
        if (message != null) {
            model.addAttribute("message", message);
        } else {
            return "redirect:/";
        }
        return "index";
    }
    
    /**
     * 登入
     * 
     * @param LoginDTO loginDTO 
     * @return ResponseEntity<Map<String, String>>
     */
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO){

        //回覆的body
        Map<String, String> response = new HashMap<>();

        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        //確認user有輸入帳密
        if (email == null || email.equals("") || password == null || password.equals("")) {                
            response.put("message", "帳密不得為空");    
            response.put("url", "/loginFail");
            return ResponseEntity.badRequest().body(response);
        }
        
        //確認資料庫中有用此email的用戶
        User user = userService.findUserByEmail(email);
        if (user == null) { 
            response.put("message", "無此帳號");    
            response.put("url", "/loginFail");
            return ResponseEntity.badRequest().body(response);
                
        }

        //確認密碼正確
        String userHashPwd = user.getPassword();
        boolean isPwdRight = BCrypt.checkpw(password, userHashPwd);
        if (isPwdRight) {
            
            //驗證成功
            //產生Header
            HttpHeaders headers = new HttpHeaders();
    
            //產生jwt
            String username = user.getUsername();
            String jwtId = UUID.randomUUID().toString();
    
            JWTUtil jWTUtil = new JWTUtil();
            String jwtToken = jWTUtil.creatJWT(username, jwtId, null);
    
            //將token塞到header
            headers.add("Authorization", "Bearer "+jwtToken);
    
            //跳到登入成功頁面
            Long userId = user.getId();     
            response.put("message", "success");         
            response.put("url", "/loginSuccess/");
            response.put("userId", userId.toString());
            return ResponseEntity.ok().headers(headers).body(response);
            
        } else{                
            response.put("message", "密碼不正確");    
            response.put("url", "/loginFail");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 跳到登入成功頁
    */
    @GetMapping("/loginSuccess/{userId}")
    public String loginSuccess(@PathVariable String userId, Model model){

        User user = userService.findById(userId);
        String username = user.getUsername();

        model.addAttribute("message", "登入成功：" + username);

        return "loginSuccess";
    }

}
