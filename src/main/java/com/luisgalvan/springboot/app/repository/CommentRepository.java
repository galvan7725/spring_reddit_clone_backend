package com.luisgalvan.springboot.app.repository;

import com.luisgalvan.springboot.app.model.Comment;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}