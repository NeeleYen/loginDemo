package com.example.login.service;

import com.example.login.model.User;

public interface UserService {


    /**
     * 找出信箱符合的User
     */
    public User findUserByEmail(String email);

    public User findById(String userId);

}
