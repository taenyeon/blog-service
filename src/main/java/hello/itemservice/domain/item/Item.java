package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    // int로 쓰면 아예 안들어간 경우에 애매할 수 있기 때문에
    // null도 들어갈 수 있는 Integer 사용
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
