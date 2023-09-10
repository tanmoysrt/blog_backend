package com.example.blog.service;

import com.example.blog.dao.BlogDao;
import com.example.blog.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogDao repository;
    private final AuthenticationService authenticationService;

    public List<Blog> findAll() {
        return repository.findAll();
    }

    public Blog findById(int id) {
        return repository.findById(id).get();
    }

    public List<Blog> findByAuthorUsername(String username) {
        return repository.findByAuthor_Username(username);
    }

    public void deleteBlog(int id) {
        repository.deleteById(id);
    }

    public void updateBlog(int id, String title, String content, String coverImage, String topics) {
        repository.findById(id).ifPresent(blog -> {
            blog.setTitle(title);
            blog.setContent(content);
            blog.setDate(new java.sql.Date(System.currentTimeMillis()));
            blog.setCoverImage(coverImage);
            blog.setTopics(topics);
            repository.save(blog);
        });
    }

    public Optional<Blog> createBlog(String title, String content, String username,  String coverImage, String topics) {
        return Optional.of(repository.save(new Blog(title, content, authenticationService.getUserByUsername(username).get(), new java.sql.Date(System.currentTimeMillis()), coverImage, topics)));
    }
}
