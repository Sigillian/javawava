package TheFactory;

import java.util.ArrayList;
import java.util.Random;

public class Shop {
    private static boolean mustLeave = false;
    public static void openShop() {
        mustLeave = false;
        GUI.clearTerminal();
        GUI.addToCommandOutput("Welcome to the shop!");
        GUI.addToCommandOutput("---PRODUCTS---");
        for(Product p : generateProductList())
            GUI.addToCommandOutput(p.toString());
        GUI.addToCommandOutput("Type 'buy [PRODUCT]' to buy a product.");
    }
    public static void buyProduct(Product product) {
        if(!mustLeave) {
            GUI.addToCommandOutput("You have bought " + product.name + "!");
            Headquarters.wallet -= product.price;
            Headquarters.inventory.add(product);
            GUI.addToCommandOutput("You now have $" + Headquarters.wallet);
            closeShop();
        }else GUI.addToCommandOutput("You have already purchased a product!");
    }
    public static ArrayList<Product> generateProductList() {
        Product[] possibleProducts = {
            new Product("Dowsing-Rod", 10)
        };
        ArrayList<Product> productList = new ArrayList<>();
        for(int i = 0; i < new Random().nextInt(8,12); i++) {
            Product temp = possibleProducts[new Random().nextInt(possibleProducts.length)];
            if(!productList.contains(temp))
                productList.add(temp);
        }
        return productList;
    }
    public static void closeShop() {
        mustLeave = true;
        GUI.addToCommandOutput("Thank you for shopping!");
        GUI.addToCommandOutput("Type 'exit' to exit!");
    }

}
