package com.luisgalvan.springboot.app.repository;

import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.User;
import com.luisgalvan.springboot.app.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
