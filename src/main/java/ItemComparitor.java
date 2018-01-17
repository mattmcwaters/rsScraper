import java.util.Comparator;

/**
 * Created by Matt on 2015-08-04.
 */
public class ItemComparitor implements Comparator<Item> {
    public int compare(Item i1, Item i2){
        int i1Margin = i1.gePrice-i1.price;
        int i2Margin = i2.gePrice-i2.price;
        if(i1Margin>i2Margin){
            return -1;
        }
        else if(i1Margin<i2Margin){
            return 1;
        }
        return 0;
    }
}
