package com.example.blog.dao;
import java.util.Optional;

import com.example.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserDao  extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);

}