package com.example.login.dao;

import org.springframework.stereotype.Repository;

import com.example.login.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserDao extends JpaRepository<User, Long>{

    @Query(value = "select * from user where email= :email", nativeQuery = true)
    public User findUserByEmail(@Param("email") String email);

}
