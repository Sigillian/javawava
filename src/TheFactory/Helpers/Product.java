package TheFactory.Helpers;

public class Product {
    public static Product[] prods = {
        new Product("Dowsing-Rod", 40),
    };

    public String name;
    public int price;
    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }
    public Product(String name) throws Exception {
        for(Product p : prods)
            if(p.name.equalsIgnoreCase(name)) {
                this.name = name;
                this.price = p.price;
            }
        else throw new Exception("Invalid product");
    }

    @Override
    public String toString() {
        return name + ": $" + price;
    }

    public String getAsSaveable() {
        return "prd"+name + " " + price;
    }
}
