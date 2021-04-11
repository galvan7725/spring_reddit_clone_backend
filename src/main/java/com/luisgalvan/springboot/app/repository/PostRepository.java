package com.luisgalvan.springboot.app.repository;

import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.Subreddit;
import com.luisgalvan.springboot.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
