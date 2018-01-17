import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matt on 2015-09-01.
 */
public class AssignGePrices {
    public static void main(String[] args)throws Exception {
        ShopList shopList = new ShopList();
        try {
            FileInputStream fileIn = new FileInputStream("shopList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            shopList = (ShopList) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("shopList class not found");
            c.printStackTrace();
            return;
        }

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(
            Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        WebClient wc = new WebClient();
        wc.getOptions().setThrowExceptionOnScriptError(false);

        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        wc.setAjaxController(new AjaxController(){
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
            {
                return true;
            }
        });
        String url =
            "https://rsbuddy.com/exchange/summary.json";
        String result = "";

        URL newUrl = new URL(url);
        URLConnection conn = newUrl.openConnection();
        conn.setRequestProperty("Authorization", "Basic");
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            result += output;
        }


        int count=0;
        for(Shop shop: shopList.getShopList()){
            for(Item item: shop.getItemList()){
                count++;
                if(!result.contains(item.itemName)){
                    System.out.println("Item "+item.itemName+ " not in summary");
                }
                else{
                    JSONArray ObjectsAll = new JSONArray();

                    JSONObject jsonResult = new JSONObject(result);
                    ObjectsAll = jsonResult.toJSONArray(jsonResult.names());

                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject object = (JSONObject) ObjectsAll.get(i);
                        if(object.getString("name").equals(item.itemName)){
                            item.setGePrice(object.getInt("overall_average"));
                            System.out.println("Completed item number " + count);

                        }
                    }
                    System.out.println("Completed item number " + count);

                }

            }
        }
        try
        {
            FileOutputStream fileOut =
                new FileOutputStream("shopList.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shopList);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in shopList.ser");
        }catch(IOException i)
        {
            i.printStackTrace();
        }
        System.out.println("Itemx" );

    }
}
