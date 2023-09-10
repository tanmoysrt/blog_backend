package com.example.blog.dao;

import com.example.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogDao extends JpaRepository<Blog, Integer> {
    List<Blog> findByAuthor_Username(String username);
    Optional<Blog> findById(int id);
}
