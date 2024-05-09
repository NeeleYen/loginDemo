package com.example.login.serviceImplement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.login.dao.UserDao;
import com.example.login.model.User;
import com.example.login.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired UserDao userDao;

    /**
     * 找出信箱符合的User
     */
    @Override
    public User findUserByEmail(String email) {
        
        User user = userDao.findUserByEmail(email);

        if (user != null) {
            return user;
        }

        return null;
    }

    @Override
    public User findById(String userId) {
        
        Long idLong = Long.parseLong(userId);
        Optional<User> optional = userDao.findById(idLong);

        if(optional.isPresent()){
            return optional.get();
        }

        return null;

    }

    



}
