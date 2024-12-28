package TheFactory;

import java.util.ArrayList;

public class Farm {
    public final int farmID;
    private final Headquarters.CropType product;
    public static int lastFarmIDMade = 0; //Needs to be changed when loading game
    public final ArrayList<Employee> employees = new ArrayList<>();
    private static int getProductValue(Headquarters.CropType rm) {
        return switch (rm) {
            case Potatoes -> 1;
            case Carrots -> 2;
            case Bread -> 3;
        };
    }
    public Farm(Headquarters.CropType product) {
        this.product = product;
        farmID = lastFarmIDMade++;
        Headquarters.farmList.add(this);
    }
    public void addEmployee(Employee e) throws Exception {
        if(employees.size() < 6)
            employees.add(e);
        else throw new Exception("Farm is full");
    }
    public void update() {
        Headquarters.foodSupply += getProductValue(product);
    }

    public void addFarmToGUI() {
        GUI.addToCommandOutput("Farm ID: " + farmID);
        GUI.addToCommandOutput("Product: " + product + " (" + getProductValue(product) + ")f");
        GUI.addToCommandOutput("Employees: " + employees.size());
    }

    @Override
    public String toString() {
        return "farm " + farmID + " making " + product;
    }
}