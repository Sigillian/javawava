package TheFactory;

import java.util.ArrayList;

public class Factory {
    public final int factoryID;
    private final Product product;
    public static int lastFactoryIDMade = 0;
    public final ArrayList<Employee> employees = new ArrayList<>();
    public Factory(Product product) {
        this.product = product;
        factoryID = lastFactoryIDMade++;
    }
    public void addEmployee(Employee e) throws Exception {
        if(employees.size() < 6)
            employees.add(e);
        else throw new Exception("Mine is full");
    }
    public void update() {
        Headquarters.wallet += product.price;
    }
    public String getAsSaveable() {
        String employeeIds = "";
        for(Employee e : employees)
            employeeIds += e.employeeID + ",";
        return "fac" + factoryID + " " + product.name + "[" + employeeIds + "]";
    }

    public void addFactoryToGUI() {
        GUI.addToCommandOutput("Factory ID: " + factoryID);
        GUI.addToCommandOutput("Product: " + product + " (" + product.price + "p)");
        GUI.addToCommandOutput("Employees: " + employees.size());
        GUI.addToCommandOutput("Wallet: $" + Headquarters.wallet);
    }
    public String toString() {
        return "factory " + factoryID + " making " + product;
    }
}