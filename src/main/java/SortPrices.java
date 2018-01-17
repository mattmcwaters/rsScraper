import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Matt on 2015-09-02.
 */
public class SortPrices {
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
        ArrayList<Item> fullItemList = new ArrayList<Item>();
        for(Shop shop: shopList.getShopList()){
            for(Item item: shop.getItemList()){
                fullItemList.add(item);
            }
        }
        int count=0;
        Collections.sort(fullItemList, new ItemComparitor());
        for(Item item: fullItemList){
            if(item.stock>1&&!item.shopName.contains("Farming")){
                count++;
                System.out.print("Item #"+count+": " + item.itemName + "\n\t\tShop Price: "+item.price+"\n\t\tGe Price: "+item.gePrice+"\n\t\tStock: "+item.stock+"\n\t\tShop name: "+item.shopName+"\n\n");
            }

        }






    }
}
