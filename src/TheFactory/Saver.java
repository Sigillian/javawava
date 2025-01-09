package TheFactory;

import TheFactory.Buildings.Factory;
import TheFactory.Buildings.Farm;
import TheFactory.Buildings.Housing;
import TheFactory.Buildings.Mine;
import TheFactory.Helpers.Product;

import java.io.FileReader;
import java.io.FileWriter;

public class Saver {
    public static void saveGame() {
        GUI.clearTerminal();
        GUI.addToCommandOutput("Saving game...");
        String toSave = "";
        toSave += "hqf"+ Headquarters.foodSupply + "\n";
        toSave += "hqw"+Headquarters.wallet + "\n";
        toSave += "lmfac" + Factory.lastFactoryIDMade + "\n";
        toSave += "lmmin" + Mine.lastMineIDMade + "\n";
        toSave += "lmfar" + Farm.lastFarmIDMade + "\n";
        toSave += "lmhou" + Housing.lastHousingIDMade + "\n";
        toSave += "lmemp" + Employee.lastEmployeeIDMade + "\n";
        for(Product p : Headquarters.inventory)
            toSave += p.getAsSaveable() + "\n";
        for(Factory f : Headquarters.factoryList)
            toSave += f.getAsSaveable() + "\n";
        for(Mine m : Headquarters.mineList)
            toSave += m.getAsSaveable() + "\n";
        for(Farm f : Headquarters.farmList)
            toSave += f.getAsSaveable() + "\n";
        for(Housing h : Headquarters.housingList)
            toSave += h.getAsSaveable() + "\n";
        for(Employee e : Headquarters.employeeList)
            toSave += e.getAsSaveable() + "\n";
        try {
            FileWriter file = new FileWriter("save.txt");
            file.write(toSave);
            file.close();
        }catch (Exception e) {
            GUI.clearTerminal();
            GUI.addToCommandOutput("Error saving game");
            e.printStackTrace();
        }
        GUI.addToCommandOutput("Saved game successfully");
        System.exit(0);

    }

    public static void loadGame() {
        String toLoad = "save.txt";
        try {
            FileReader file = new FileReader(toLoad);
            StringBuilder s = new StringBuilder();
            int c;
            while ((c = file.read()) != -1)
                s.append((char) c);
            file.close();
            System.out.println(s.toString());
            String[] lines = s.toString().split("\n");
            Headquarters.foodSupply = Integer.parseInt(lines[0].split("hqf")[1]);
            Headquarters.wallet = Integer.parseInt(lines[1].split("hqw")[1]);
            Factory.lastFactoryIDMade = Integer.parseInt(lines[2].split("lmfac")[1]);
            Mine.lastMineIDMade = Integer.parseInt(lines[3].split("lmmin")[1]);
            Farm.lastFarmIDMade = Integer.parseInt(lines[4].split("lmfar")[1]);
            Housing.lastHousingIDMade = Integer.parseInt(lines[5].split("lmhou")[1]);
            Employee.lastEmployeeIDMade = Integer.parseInt(lines[6].split("lmemp")[1]);
            for(int i = 7; i < lines.length; i++) {
                String[] parts = lines[i].split(" ");
                if(parts[0].equalsIgnoreCase("prd")) {
                    Headquarters.inventory.add(new Product(parts[1]));
                }else if(parts[0].equalsIgnoreCase("min")) {
                    Headquarters.mineList.add(new Mine(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())));
                }else if(parts[0].equalsIgnoreCase("far")) {
                    Headquarters.farmList.add(new Farm(Headquarters.CropType.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())));
                }else if(parts[0].equalsIgnoreCase("hou")) {
                    Headquarters.housingList.add(new Housing(Housing.ResidentType.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())));
                }else if(parts[0].equalsIgnoreCase("emp")) {
                    Headquarters.employeeList.add(new Employee(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Headquarters.Building.valueOf(parts[5].substring(0, 1).toUpperCase() + parts[5].substring(1).toLowerCase())));
                }
            }
        } catch (Exception e) {
            GUI.clearTerminal();
            GUI.addToCommandOutput("Error loading game");
            e.printStackTrace();
        }
        GUI.addToCommandOutput("Loaded game successfully");
    }
    public static void main(String[] args) {
        Headquarters.initializeGame();
        saveGame();
        loadGame();
    }
}