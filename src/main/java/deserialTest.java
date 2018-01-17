
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matt on 2015-08-03.
 */
public class deserialTest {
    public static void main(String[] args) {

        ShopList shopList = new ShopList();
        try
        {
            FileInputStream fileIn = new FileInputStream("shopList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            shopList = (ShopList) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("PC class not found");
            c.printStackTrace();
            return;
        }
        for(Shop shop: shopList.getShopList()){
            if(shop.nonGoldCurrency){
                System.out.println("Shop "+ shop.getShopName() + " takes non gold currency");
            }

        }
    }
}
