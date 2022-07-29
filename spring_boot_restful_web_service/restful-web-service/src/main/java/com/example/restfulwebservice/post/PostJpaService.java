package com.example.restfulwebservice.post;

import com.example.restfulwebservice.user.jpa.User;
import com.example.restfulwebservice.user.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostJpaService {

    private final PostRepository postRepository;

    private final FindPostRepository findPostRepository;

    public Optional<Post> findPost(User user, int postId) {
        return findPostRepository.findOne(user, postId);
    }

    @Transactional
    public Post addPost(Post post, User user) {

        //post와 user의 연관관계를 맺어줌.
        post.setUser(user);

        //post를 db에 저장함.
        postRepository.save(post);

        //user와 post의 관계를 맺기 위해 user를 영속성컨텍스트에 올려줌.
//        User findUser = userRepository.findById(user.getId()).get();
//
//        findUser.getPosts().add(post);

        user.getPosts().add(post);

        return post;

    }

}
