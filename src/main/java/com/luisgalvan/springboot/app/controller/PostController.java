package com.luisgalvan.springboot.app.controller;

import com.luisgalvan.springboot.app.dto.PostRequest;
import com.luisgalvan.springboot.app.dto.PostResponse;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
         postService.save(postRequest);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.status(OK).body(postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>>getAllPosts(){
        return ResponseEntity.status(OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return ResponseEntity.status(OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username){
        return ResponseEntity.status(OK).body(postService.getPostsByUsername(username));
    }


}
