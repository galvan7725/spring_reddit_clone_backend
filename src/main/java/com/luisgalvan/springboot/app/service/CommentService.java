package com.luisgalvan.springboot.app.service;

import com.luisgalvan.springboot.app.dto.CommentsDto;
import com.luisgalvan.springboot.app.exceptions.PostNotFoundException;
import com.luisgalvan.springboot.app.mapper.CommentMapper;
import com.luisgalvan.springboot.app.model.Comment;
import com.luisgalvan.springboot.app.model.NotificationEmail;
import com.luisgalvan.springboot.app.model.Post;
import com.luisgalvan.springboot.app.model.User;
import com.luisgalvan.springboot.app.repository.CommentRepository;
import com.luisgalvan.springboot.app.repository.PostRepository;
import com.luisgalvan.springboot.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto){
      Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException(commentsDto.getPostId().toString()));

      Comment comment = commentMapper.map(commentsDto,post, authService.getCurrentUser());
      commentRepository.save(comment);

      String message = mailContentBuilder.build(post.getUser().getUsername() + "posted a comment on your post "+ POST_URL);
      sendCommentNotification(message, post.getUser());

    }

    private void sendCommentNotification(String message, User user){
        mailService.sendMail(new NotificationEmail(user.getUsername() + "Commented on your post",user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(postId.toString()));

       return  commentRepository.findByPost(post)
                .stream().map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
