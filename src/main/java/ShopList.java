import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matt on 2015-09-01.
 */
public class ShopList implements Serializable{
     private ArrayList<Shop> shopList = new ArrayList<Shop>();

    public  void setShopList(ArrayList<Shop> shopList) {
        this.shopList = shopList;
    }

    public  ArrayList<Shop> getShopList() {
        return this.shopList;
    }
}
