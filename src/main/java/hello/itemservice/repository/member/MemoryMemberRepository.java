package hello.itemservice.repository.member;

import hello.itemservice.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static Map<String,Member> store = new ConcurrentHashMap<>();

    @Override
    public Member save(Member member) {
            store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        // Null 값이 올 수 있을 경우에는 Optional로 감싸
        // 이후 반환되는 값을 처리하기 편하도록 함.
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        // 리스트로 바꾸어 보내서
        // 꺼내기 편하도록 함
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<String> login(Member member) {
        return Optional.empty();
    }


    public void clearStore(){
        store.clear();
    }
}
