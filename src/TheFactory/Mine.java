package TheFactory;

import java.util.ArrayList;
import java.util.Random;

public class Mine {
    public final int mineID;
    private final Headquarters.RawMaterial product;
    public static int lastMineIDMade = 0; //Needs to be changed when loading game
    public final ArrayList<Employee> employees = new ArrayList<>();
    private static int getProductValue(Headquarters.RawMaterial rm) {
        return switch (rm) {
            case Stone -> 5;
            case Coal -> 10;
            case Iron -> 20;
            case Gold -> 30;
            case Crystal -> 40;
            case Platinum -> 50;
        };
    }
    public Mine(Headquarters.RawMaterial product) {
        this.product = product;
        mineID = lastMineIDMade++;
        Headquarters.mineList.add(this);
    }
    public Mine() {
        mineID = lastMineIDMade++;
        this.product = Headquarters.RawMaterial.values()[new Random().nextInt(Headquarters.RawMaterial.values().length)];
    }
    public void addEmployee(Employee e) throws Exception {
        if(employees.size() < 6)
            employees.add(e);
        else throw new Exception("Mine is full");
    }
    public void update() {
        Headquarters.rawMaterialStorage.addToStorage(product);
    }

    public void addMineToGUI() {
        GUI.addToCommandOutput("Mine ID: " + mineID);
        GUI.addToCommandOutput("Product: " + product);
        GUI.addToCommandOutput("Employees: " + employees.size());
        GUI.addToCommandOutput("Wallet: $" + Headquarters.wallet);
    }

    public String toString() {
        return "mine " + mineID + " making " + product;
    }
}