package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //클래스 변수로 사용
    private static long sequence = 0L; // 클래스 변수로 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String longinId) {
//        List<Member> all = findAll();
//        for (Member member : all) {
//            if (member.getLonginId().equals(longinId)) {
//                return Optional.of(member);
//            }
//        }
//        return Optional.empty();

        return findAll().stream()
                .filter(member -> member.getLoginId().equals(longinId))
                .findAny();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
