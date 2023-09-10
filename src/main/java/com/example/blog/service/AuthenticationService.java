package com.example.blog.service;

import com.example.blog.config.JwtService;
import com.example.blog.dao.UserDao;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.models.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDao repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = repository.save(user);
        return jwtService.generateToken(savedUser);
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> foundUser = repository.findByUsername(username);
        return foundUser;
    }

    public String authenticate(LoginRequest request){
        Optional<User> foundUser = repository.findByUsername(request.username);
        if (foundUser.isPresent() && passwordEncoder.matches(request.password, foundUser.get().getPassword())){
            return jwtService.generateToken(foundUser.get());
        }
        return "";
    }

    public Role getRoleFromRequest(String username){
        Optional<User> foundUser = repository.findByUsername(username);
        return foundUser.map(User::getRole).orElse(null);
    }

    public List<User> getAllUsers() {
        List<User> users =  repository.findAll();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setPassword("");
        }
        return users;
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }
}