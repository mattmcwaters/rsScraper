import java.io.Serializable;

/**
 * Created by Matt on 2015-08-31.
 */
public class Item implements Serializable{
    String itemName;
    int price;
    int gePrice;
    int stock;
    String shopName;
    public Item(String itemName, int price, int stock){
        this.itemName = itemName;
        this.price = price;
        this.stock=stock;
    }

    public void setGePrice(int gePrice) {
        this.gePrice = gePrice;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

}
