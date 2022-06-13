package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        //ArrayList로 감싸서 반환하게 되면 반환되는 List의 값이 변경되어도
        //store의 value값에는 변경이 생기지 않는다.
        return new ArrayList<>(store.values());
    }

    // updateParam에서 id는 사용되지 않기 떄문에 id를 제외한 필드값을 가지는
    // dto를 별도로 만들어서 넣어주는 것이 좋다.
    // 그럼 코드가 중복되는데 중복이냐 명확성이냐를 따를 때 항상 명확성을 따르는 것이 좋다.
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
