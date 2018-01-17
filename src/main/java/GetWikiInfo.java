import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.commons.logging.LogFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matt on 2015-08-31.
 */
public class GetWikiInfo {
    static ShopList shopList = new ShopList();
    public GetWikiInfo(){

    }
    public static void main(String[] args) throws Exception{

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

        populateShopList(wc);
        addItemsToShops(wc);
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
        /*
        int count=0;
        for(Shop shop: shopList.getShopList()){
            if (shop.nonGoldCurrency==true){
                count+=1;
            }
        }
        System.out.println("non gold currency shop count is " + count);
*/
    }
    public static void populateShopList(WebClient wc)throws Exception{


        HtmlPage wikiPage = wc.getPage("http://2007.runescape.wikia.com/wiki/Solihib's_Food_Stall");
        List tableElements = wikiPage.getByXPath("//table[@class='nowraplinks navbox collapsible collapsed']");
        HtmlElement firstTable = (HtmlElement) tableElements.get(0);
        List allShops = firstTable.getFirstChild().getChildNodes();

        for (int i = 1; i < allShops.size(); i++) {
            firstTable = (HtmlElement) allShops.get(i);
            List allMiniStores = firstTable.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
            for (int l = 1; l < allMiniStores.size(); l++) {
                firstTable = (HtmlElement) allMiniStores.get(l);
                firstTable = (HtmlElement) firstTable.getFirstChild().getNextSibling().getFirstChild();
                tableElements =  firstTable.getChildNodes();

                for (int j = 0; j < tableElements.size(); j++) {
                    if(tableElements.get(j) instanceof HtmlAnchor){
                        if(((HtmlAnchor) tableElements.get(j)).getAttribute("href").startsWith("/wiki")){

                            Pattern nameGetter = Pattern.compile(".*?title=\"(.*?)\">.*?");
                            Matcher nameGetterMatch = nameGetter.matcher(
                                tableElements.get(j).toString());
                            nameGetterMatch.find();
                            String shopName = nameGetterMatch.group(1);


                            Pattern linkGetter = Pattern.compile("HtmlAnchor\\[<a href=\"(.*?)\" title=.*");
                            Matcher linkGetterMatcher = linkGetter.matcher(tableElements.get(j).toString());
                            String shopLink = "http://2007.runescape.wikia.com";
                            linkGetterMatcher.find();
                            shopLink+= linkGetterMatcher.group(1);

                            Shop tempShop = new Shop(shopName, shopLink);
                            shopList.getShopList().add(tempShop);
                        }
                    }
                }
            }
        }
    }
    public static void addItemsToShops(WebClient wc) throws Exception{
        int count=0;
        for(Shop shop : shopList.getShopList()){
            count++;
            if(true){
                HtmlPage shopPage = wc.getPage(shop.getShopLink());
                HtmlElement itemTable = shopPage.getFirstByXPath("//table[@class='wikitable sortable jquery-tablesorter']");
                List shopItems=null;
                int i = 0;
                if(itemTable == null){
                    itemTable = shopPage.getFirstByXPath("//table[@class='wikitable']");
                    if(itemTable==null){
                        continue;
                    }
                    shopItems = itemTable.getFirstChild().getChildNodes();
                    i=1;
                    List tableTitles = ((HtmlElement)shopItems.get(0)).getChildNodes();
                    HtmlElement priceInCheck = (HtmlElement) tableTitles.get(3);
                    if(priceInCheck.getTextContent().contains("in")){
                        shop.setNonGoldCurrency(true);
                        continue;
                    }

                    System.out.println("");
                }
                else{
                    shopItems = itemTable.getFirstChild().getNextSibling().getChildNodes();

                }
                while (i < shopItems.size()) {
                    if(shopItems.get(i) instanceof DomText){
                        break;
                    }
                    List itemAttributes = ((HtmlElement) shopItems.get(i)).getChildNodes();
                    String itemName = ((HtmlElement)itemAttributes.get(1)).getFirstChild().getTextContent();

                    String stockDirt = ((HtmlElement) itemAttributes.get(2)).getTextContent();
                    String priceDirt = ((HtmlElement) itemAttributes.get(3)).getTextContent();

                    int price = clean(priceDirt);
                    int stock = clean(stockDirt);

                    Item tempItem = new Item(itemName, price, stock);
                    tempItem.setShopName(shop.getShopName());
                    shop.getItemList().add(tempItem);
                    i++;
                }
                System.out.println("Finished shop " + count);
            }

        }
    }
    public static int clean(String toClean){
        toClean = toClean.replaceAll("[^0-9]", "");
        toClean = toClean.replaceAll("\\n", "");
        try{
            return Integer.parseInt(toClean);
        }
        catch(Exception e){
            return -1;
        }
    }
}
