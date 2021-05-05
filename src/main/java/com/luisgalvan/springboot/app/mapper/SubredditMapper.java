package com.luisgalvan.springboot.app.mapper;

import com.luisgalvan.springboot.app.dto.SubredditDto;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.Subreddit;
import com.luisgalvan.springboot.app.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    default Instant getCurrentTime(){
        return Instant.now();
    }

    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(getCurrentTime())")
    @Mapping(target = "user", source = "user")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);
}
