import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matt on 2015-08-31.
 */
public class Shop implements Serializable {
    String shopName;
    boolean nonGoldCurrency=false;

    String shopLink;
    ArrayList<Item> itemList = new ArrayList<Item>();
    public Shop(String shopName, String shopLink){
        this.shopName = shopName;
        this.shopLink = shopLink;
    }

    public String getShopName() {
        return shopName;
    }

    public void setNonGoldCurrency(boolean nonGoldCurrency) {
        this.nonGoldCurrency = nonGoldCurrency;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public String getShopLink() {
        return shopLink;
    }

}
