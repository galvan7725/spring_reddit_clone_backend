package com.luisgalvan.springboot.app.service;

import com.luisgalvan.springboot.app.dto.PostRequest;
import com.luisgalvan.springboot.app.dto.PostResponse;
import com.luisgalvan.springboot.app.exceptions.PostNotFoundException;
import com.luisgalvan.springboot.app.exceptions.SubredditNotFoundException;
import com.luisgalvan.springboot.app.mapper.PostMapper;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.Subreddit;
import com.luisgalvan.springboot.app.model.User;
import com.luisgalvan.springboot.app.repository.PostRepository;
import com.luisgalvan.springboot.app.repository.SubredditRepository;
import com.luisgalvan.springboot.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final  AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(()-> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

}
