package com.example.restfulwebservice.post;

import com.example.restfulwebservice.user.jpa.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FindPostRepository {

    private final EntityManager em;

    public Optional<Post> findOne(User user, Integer postId) {
        return em.createQuery(
                        "select p from Post p where p.user =:user and" +
                                " p.id = :postId",
                        Post.class)
                .setParameter("user", user)
                .setParameter("postId", postId)
                .getResultList()
                .stream()
                .findAny();
    }

}
