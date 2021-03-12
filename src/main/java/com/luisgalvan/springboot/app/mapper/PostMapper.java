package com.luisgalvan.springboot.app.mapper;

import com.luisgalvan.springboot.app.dto.PostRequest;
import com.luisgalvan.springboot.app.dto.PostResponse;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.Subreddit;
import com.luisgalvan.springboot.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}
