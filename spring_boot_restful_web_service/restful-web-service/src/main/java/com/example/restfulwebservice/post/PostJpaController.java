package com.example.restfulwebservice.post;

import com.example.restfulwebservice.user.UserNotFoundException;
import com.example.restfulwebservice.user.jpa.User;
import com.example.restfulwebservice.user.jpa.UserJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class PostJpaController {

    private final UserJpaService userJpaService;

    private final PostJpaService postJpaService;

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        User user = findUser(id);

        return user.getPosts();
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public Post retrievePostByUser(@PathVariable int userId,
                                   @PathVariable int postId) {
        User user = findUser(userId);

        return findPost(user, postId);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        User user = findUser(id);

        Post savedPost = postJpaService.addPost(post, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    private User findUser(@PathVariable int id) {
        Optional<User> user = userJpaService.findUser(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user.get();
    }

    private Post findPost(User user, int postId) {
        Optional<Post> post = postJpaService.findPost(user, postId);

        if (!post.isPresent()) {
            throw new PostNotFoundException(String.format("ID[%s] not found", postId));
        }

        return post.get();
    }
}
