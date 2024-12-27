package TheFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Employees
 * 	->Take food and housing
 * 	->Strength and intelligence get better when player invests in education
 * Farms
 * 	->Requires employees, no min str/int, efficiency increases with str/int balance over all employees (DIV(str, int), 1 is ideal)
 * 	->Will randomly pick a crop every 121 days
 * 	->Requires progressively better tools
 * 		|->death less likely with better equipment
 * 	->Can sell raw materials
 * Factories
 * 	->Requires employees: max of 10
 * 	->products have a minimum str/int, efficiency increases as threshold is surpassed (base is 50%)
 * 	->Requires progressively better tools to make more/better products
 * 	->Can sell things to shop automatically
 * Mines
 * 	->Requires employees, no min int, efficiency increases with strength
 * 	->When built, will randomly decide what will be mined, can be set with dowsing rod
 * 	->Gets faster with better equipment (shop & produt)
 * 		|->Death less likely with better equipment
 * Shop
 * 	->can buy basic products
 * 	->hire employees
 * 		|->job board gens 3 random employees, user picks one, can refresh for $
 * Housing
 * 	->Very basic
 * 	->Just allows you to hire more employees
 * 	->Can be overcrowded to 1.5x max capacity, but each employee over the limit requires double food
 *
 *
 * --COMMAND LIST--
 * General
 * 	->check [TYPE] [ID]
 * 		|:Lists information on specified type
 * 		|Types: Factory; Employee; Mine; Corp; Farm; Housing
 * 			|Factory: Return all employees, current product, current efficiency, product sell rate, autosell, material usage, product min str/int, autosell min
 * 			|Employee: Return ID, str/int, and age
 * 			|Mine: Return materials/day, material, material value, efficiency, autosell min
 * 			|Corp: Return # of employees, # of factories, current number of mats, $, food supply {--IGNORES ID--}
 * 			|Farm: Return # of employees, efficiency, current product, product/min, autosell min
 * 			|Housing: # of residents, food usage
 * 	->sell [PRODUCT/MATERIAL] [AMT (* for sell all)]
 * 	->shop
 * 		|:Opens shop
 * 	->demolish [TYPE] [ID]
 * 		|:Demolishes any building without reimbursement
 * 		|Types: Factory; Mine; Farm; Housing
 * 	->construct [TYPE]
 * 		|:Builds another production line
 * 		|Types: Factory; Mine; Farm; Housing
 * 			|Housing: Will prompt user to pick a type
 * 			|-> construct mine [MATERIAL]
 * 				|:Only works when you own a dowsing rod
 * 	->make [PRODUCT] [ID]
 * 		|:sets a factory to make a certain product
 * 		|->make idle: sets factory to idle, employees will still take food
 * 	-> exit {WHILE IN SUBMENU}
 * 		|:exits all submenus back to main screen
 * Factories, Farms, Mines
 * 	->autosell [TYPE] [ID] [EXCESS]
 * 		|:Line will automatically sell it's products, [EXCESS] is an integer where it will only sell products made when storage is over that cap
 * 		|Types: Factory; Mine; Farm
 * Employees
 * 	->check employees
 * 		|:Lists all employees, where they work, their str/int, and their age
 * 	->assign [EMPLOYEEID] [TYPE] [ID]
 * 		|:assigns an employee to a building
 * 	->fire [EMPLOYEEID]
 * 		|:fires an employee, cannot be undone
 * Shop {WHILE IN SHOP}
 * 	->buy [OPTION] [AMT]
 * 	->jobboard
 * 		|:opens the job board
 * 		|->refresh
 * 		|->hire [ORDERINLIST]
 *
 *
 * --PRODUCTION BOOK--
 * Farms: 50 Stone
 * 	Bread (1 food), Potatoes (2 food), Carrots (3 food)
 * Mines: 100 Stone
 * 	Stone, Coal, Iron, Gold, Crystal, Platinum
 * Factories:
 * 	Crude Pickaxe -> 15 Stone
 * 	Iron Pickaxe -> 40 Processed Iron
 * 	Steel Pickaxe -> 40 Steel
 *
 * 	Crude Trowel -> 10 Stone
 * 	Iron Scythe -> 30 Processed Iron
 * 	Steel Plow -> 80 Steel
 *
 * 	Processed Iron ($80) -> 10 Iron
 * 	Processed Gold ($120) -> 10 Gold
 * 	Processed Crystal ($160) -> 10 Crystal
 * 	Processed Platinum ($200) -> 10 Platinum
 * 	Steel ($300) -> 100 Processed Iron 100 Coal 15 Processed Crystal
 *
 *
 */
public class Headquarters {
    public enum Building {
        Factory,
        Mine,
        Farm,
        Housing
    }
    public enum CropType {
        Potatoes,
        Carrots,
        Bread
    }
    public enum RawMaterial {
        Stone,
        Coal,
        Iron,
        Gold,
        Crystal,
        Platinum
    }
    public static class RawMaterialStorage {
        private final Object[][] matCommaCount = new Object[RawMaterial.values().length][2];
        public RawMaterialStorage() {
            for(int i = 0; i < RawMaterial.values().length; i++) {
                matCommaCount[i][0] = RawMaterial.values()[i];
                matCommaCount[i][1] = 0;
            }
        }
        public void addToStorage (RawMaterial rm){
            for(Object[] j : matCommaCount) {
                if( rm == j[0])
                    j[1] = Integer.parseInt(j[1].toString()) + 1;
            }
        }
        public String getAsString() {
            String k = "";
            for(Object[] o : matCommaCount) {
                k += (o[0] + " " + o[1]+"ct]");
            }
            return k;
        }
    }
    public static final RawMaterialStorage rawMaterialStorage = new RawMaterialStorage();
    public static ArrayList<Product> inventory = new ArrayList<>();
    public final static ArrayList<Factory> factoryList = new ArrayList<>();
    public final static ArrayList<Housing> housingList = new ArrayList<>();
    public final static ArrayList<Mine> mineList = new ArrayList<>();
    public final static ArrayList<Farm> farmList = new ArrayList<>();
    public final static ArrayList<Employee> employeeList = new ArrayList<>();
    public static int foodSupply = 0, wallet = 0;
    public static void killEmployee(int eID) {
        for(Employee e : employeeList)
            if(e.employeeID == eID)
                e = null;
        employeeList.removeIf(Objects::isNull);
        for(Factory f : factoryList)
            f.employees.removeIf(Objects::isNull);
        for(Farm f : farmList)
            f.employees.removeIf(Objects::isNull);
        for(Mine m : mineList)
            m.employees.removeIf(Objects::isNull);
        for(Housing h : housingList)
            h.residents.removeIf(Objects::isNull);
        System.gc();
    }
    public static void destroyBuilding(Building type, int ID) {
        switch (type) {
            case Factory:
                for(Factory i : factoryList) {
                    if(i.factoryID == ID) {
                        i = null;
                        factoryList.removeIf(Objects::isNull);
                        break;
                    }
                }
            case Mine:
                for(Mine i : mineList) {
                    if(i.mineID == ID) {
                        i = null;
                        mineList.removeIf(Objects::isNull);
                        break;
                    }
                }
            case Farm:
                for(Farm i : farmList) {
                    if(i.farmID == ID) {
                        i = null;
                        farmList.removeIf(Objects::isNull);
                        break;
                    }
                }
            case Housing:
                for(Housing i : housingList) {
                    if(i.housingID == ID) {
                        i = null;
                        housingList.removeIf(Objects::isNull);
                        break;
                    }
                }
        }
        System.gc();
    }
    public static void assignEmployee(int employeeID, int houseID, Building job, int BuildingID) {
        for(Employee e : employeeList)
            if(e.employeeID == employeeID) {
                if(e.age < 5110) {
                    GUI.clearTerminal();
                    GUI.addToCommandOutput("Employee too young");
                }
                Employee.assignEmployee(e, job);
                switch (job.getClass().getSimpleName()) {
                    case "Factory":
                        for(Factory i : factoryList)
                            if(i.factoryID == BuildingID) {
                                try {
                                    i.addEmployee(e);
                                }catch (Exception ex) {
                                    GUI.clearTerminal();
                                    GUI.addToCommandOutput("Factory unavailable");
                                }
                            }break;
                    case "Mine":
                        for(Mine i : mineList)
                                if(i.mineID == BuildingID) {
                                    try {
                                        i.addEmployee(e);
                                    }catch (Exception ex) {
                                        GUI.clearTerminal();
                                        GUI.addToCommandOutput("Factory unavailable");
                                    }
                                }break;
                    case "Farm":
                        for(Farm i : farmList)
                            if(i.farmID == BuildingID) {
                                try {
                                    i.addEmployee(e);
                                }catch (Exception ex) {
                                    GUI.clearTerminal();
                                    GUI.addToCommandOutput("Factory unavailable");
                                }
                            }break;
                    case "Housing":
                        GUI.clearTerminal();
                        GUI.addToCommandOutput("Not a job");
                }
                for(Housing h : housingList)
                    if(h.housingID == houseID) {
                        try {
                            h.addEmployee(e);
                        }catch (Exception ex) {
                            GUI.addToCommandOutput("Housing unavailable");
                        }
                    }
            }
    }

    public static void initializeGame() {
        Housing housing = new Housing(Housing.ResidentType.hut);

        Mine mine = new Mine(RawMaterial.Stone);

        Farm farm = new Farm(CropType.Potatoes);
        Farm farm1 = new Farm(CropType.Potatoes);
        Farm farm2 = new Farm(CropType.Potatoes);
        Farm farm3 = new Farm(CropType.Potatoes);

        for (int i = 0; i < 5; i++) {
            Employee employee = Employee.generateEmployee(Headquarters.Building.Factory);

            try {
                housing.addEmployee(employee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Optionally set initial food supply, wallet, or any other game stats
        foodSupply = 100;
        wallet = 500;

        printCorp();
    }

    public static void printCorp () {
        GUI.clearTerminal();
        GUI.addToCommandOutput("Factories: " + factoryList.size());
        GUI.addToCommandOutput("Mines: " + mineList.size());
        GUI.addToCommandOutput("Farms: " + farmList.size());
        GUI.addToCommandOutput("Housing Units: " + housingList.size());
        GUI.addToCommandOutput("Employees: " + employeeList.size());
        GUI.addToCommandOutput("Inventory: " + inventory.toString());
        GUI.addToCommandOutput("Food Supply: " + foodSupply);
        GUI.addToCommandOutput("Wallet: $" + wallet);
        GUI.addToCommandOutput("Raw Materials: " + Arrays.toString(rawMaterialStorage.getAsString().split("]")));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
        initializeGame();

        // Update the display to show the initial game state
        GUI.updateDisplay();
        Updater u = new Updater();
        u.start();
    }
}