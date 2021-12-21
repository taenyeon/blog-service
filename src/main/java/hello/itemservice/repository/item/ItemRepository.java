package hello.itemservice.repository.item;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    // 스프링 컨테이너에서 동작하면 어차피 싱글톤이기때문에 상관이 없지만
    // new 연산자를 통해서 생성하는 경우등이 있기 때문에 방지를 위해 static으로 만들어 줌.
    // 그리고 멀티 쓰레드 환경에서 HashMap을 사용하는것은 문제가 발생할 수 있다.
    // 그래서 ConcurrentHashMap을 주로 사용한다.
    // Long 역시 동시 접근시 문제가 발생할 수 있기 때문에
    // Automic Long과 같은 것으로 바꾸어준다.
    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static Long sequence = 0L; // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }
    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }

}
