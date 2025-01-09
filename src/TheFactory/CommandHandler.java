package TheFactory;

import TheFactory.Buildings.Factory;
import TheFactory.Buildings.Farm;
import TheFactory.Buildings.Housing;
import TheFactory.Buildings.Mine;
import TheFactory.Helpers.JobBoard;
import TheFactory.Helpers.Product;
import TheFactory.Helpers.Shop;

import java.util.Arrays;

public class CommandHandler {
    public static void handleCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) {
            GUI.addToCommandOutput("Invalid command: no input provided.");
            return;
        }

        String mainCommand = parts[0].toLowerCase().trim();



        switch (mainCommand) {
            case "help" -> help();
            case "save" -> Saver.saveGame();
            case "load" -> {
                Saver.loadGame();
                Headquarters.printCorp();
            }
            case "check", "c" -> check(parts);
            case "cc" -> Headquarters.printCorp();
            
            case "sell", "s" -> sell(parts);
            case "shop", "sp" -> Shop.openShop();
            case "demolish", "d" -> {
                GUI.clearTerminal();
                try {
                    Headquarters.destroyBuilding(Headquarters.Building.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase()), Integer.parseInt(parts[2]));
                    for(Farm o : Headquarters.farmList) {
                        System.out.println(o.toString());
                    }
                    GUI.addToCommandOutput("Demolished " + parts[1] + parts[2] + " successfully");
                } catch (ArrayIndexOutOfBoundsException e) {
                    GUI.addToCommandOutput("Invalid ID");
                }catch (IllegalArgumentException e) {
                    GUI.addToCommandOutput("Invalid building");
                }catch (Exception e) {
                    GUI.addToCommandOutput("Error");
                }
            }
            case "construct", "con" -> construct(parts);
            case "autosell" -> {
                GUI.clearTerminal();
                GUI.addToCommandOutput("Autosell is not yet implemented.");
            }
            case "assign" -> {
                break;
            }
            case "fire" -> {
                try {
                    Headquarters.killEmployee(Integer.parseInt(parts[1]));
                }catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    GUI.addToCommandOutput("Invalid employee ID");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "clear" -> GUI.clearTerminal();
            case "exit" -> exit();
            case "buy" -> {
                try {
                    Shop.buyProduct(new Product(parts[1]));
                }catch (Exception e) {
                    GUI.addToCommandOutput("Invalid product: " + parts[1]);
                }
            }
            case "jobboard" -> JobBoard.openJobBoard();
            case "hire" -> hire(parts);
            default -> {
                GUI.clearTerminal();
                GUI.addToCommandOutput("Unknown command: " + mainCommand);
            }
        }
    }
    public static void help() {
        GUI.clearTerminal();
        GUI.addToCommandOutput("Available Commands:\n");

        GUI.addToCommandOutput("General Commands:");
        GUI.addToCommandOutput(" - check [TYPE] [ID]: View detailed information about a specific entity.");
        GUI.addToCommandOutput("   - Types: corp, mine, farm, factory, housing, employee");
        GUI.addToCommandOutput("   - Example: check farm 2");
        GUI.addToCommandOutput(" - save: Save your current game progress.");
        GUI.addToCommandOutput(" - load: Load a previously saved game.");
        GUI.addToCommandOutput(" - clear: Clear the terminal display.");
        GUI.addToCommandOutput(" - exit: Save the game and exit.");

        GUI.addToCommandOutput("\nConstruction Commands:");
        GUI.addToCommandOutput(" - construct [TYPE] [DETAIL]: Build a new building of the specified type.");
        GUI.addToCommandOutput("   - Types: factory [PRODUCT], mine [MATERIAL], farm [CROP], housing [RESIDENT_TYPE]");
        GUI.addToCommandOutput("   - Example: construct mine coal");

        GUI.addToCommandOutput("\nDemolition Commands:");
        GUI.addToCommandOutput(" - demolish [TYPE] [ID]: Destroy a building without reimbursement.");
        GUI.addToCommandOutput("   - Types: factory, mine, farm, housing");
        GUI.addToCommandOutput("   - Example: demolish factory 1");

        GUI.addToCommandOutput("\nShop Commands:");
        GUI.addToCommandOutput(" - shop: Open the shop interface.");
        GUI.addToCommandOutput(" - buy [ITEM]: Purchase an item from the shop.");
        GUI.addToCommandOutput("   - Example: buy pickaxe");

        GUI.addToCommandOutput("\nEmployee Commands:");
        GUI.addToCommandOutput(" - jobboard: Open the job board to view available hires.");
        GUI.addToCommandOutput(" - hire [JOB_ID] [BUILDING_TYPE] [BUILDING_ID] [EMPLOYEE_ID]: Hire an employee for a building.");
        GUI.addToCommandOutput("   - Example: hire 1 factory 2 101");
        GUI.addToCommandOutput(" - fire [EMPLOYEE_ID]: Remove an employee from your corporation.");
        GUI.addToCommandOutput("   - Example: fire 101");

        GUI.addToCommandOutput("\nResource Management Commands:");
        GUI.addToCommandOutput(" - sell [RESOURCE] [AMOUNT]: Sell a specific amount of a resource.");
        GUI.addToCommandOutput("   - Use '*' as the amount to sell all available resources.");
        GUI.addToCommandOutput("   - Example: sell iron 50");
        GUI.addToCommandOutput(" - autosell [TYPE] [ID] [EXCESS]: Automatically sell products from a building when storage exceeds the specified limit.");
        GUI.addToCommandOutput("   - Types: factory, mine, farm");
        GUI.addToCommandOutput("   - Example: autosell factory 3 100");

        GUI.addToCommandOutput("\nProduction Commands:");
        GUI.addToCommandOutput(" - make [PRODUCT] [FACTORY_ID]: Assign a factory to produce the specified product.");
        GUI.addToCommandOutput("   - Example: make bread 2");
        GUI.addToCommandOutput(" - autosell: This feature is not yet implemented.");

        GUI.addToCommandOutput("\nMiscellaneous Commands:");
        GUI.addToCommandOutput(" - help: Display this help menu.");
        GUI.addToCommandOutput(" - assign [EMPLOYEE_ID] [BUILDING_TYPE] [BUILDING_ID]: Assign an employee to a building.");
        GUI.addToCommandOutput("   - Example: assign 101 mine 2");

        GUI.addToCommandOutput("\nTip: Use 'check corp' to see an overview of your corporation's status.\n");
    }


    private static void sell(String[] parts) {
        try {
            if(Headquarters.rawMaterialStorage.getRawMaterialCount(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())) > Integer.parseInt(parts[2])) {
                Headquarters.rawMaterialStorage.removeFromStorage(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase()), Integer.parseInt(parts[2]));
                Headquarters.wallet += Headquarters.getRawMaterialValue(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())) * Integer.parseInt(parts[2]);
                GUI.clearTerminal();
                GUI.addToCommandOutput("Sold " + parts[2] + " " + parts[1] + " from storage");
            }else {
                GUI.clearTerminal();
                GUI.addToCommandOutput("Not enough " + parts[1] + " in storage");
            }
        }catch (NumberFormatException e) {
            if(parts[2].equals("*"))
                Headquarters.rawMaterialStorage.removeFromStorage(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase()), Headquarters.rawMaterialStorage.getRawMaterialCount(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase())));
            else GUI.addToCommandOutput("Invalid number");
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();;
            System.out.println(Arrays.deepToString(parts));
            System.out.println(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase());
            System.out.println(Headquarters.RawMaterial.valueOf(parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase()));
            GUI.addToCommandOutput("Invalid raw material");
        }
    }
    private static void hire(String[] parts) {
        try {
            JobBoard.hireEmployee(Integer.parseInt(parts[1]),
                    Headquarters.Building.valueOf(parts[2].substring(0, 1).toUpperCase() + parts[2].substring(1).toLowerCase()),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            switch(e.getClass().getSimpleName()) {
                case "ArrayIndexOutOfBoundsException" ->
                        GUI.addToCommandOutput("Invalid Housing ID, Job ID, or Tentative Employee ID");
                case "NumberFormatException" ->
                        GUI.addToCommandOutput("Invalid number");
                case "NullPointerException" ->
                        GUI.addToCommandOutput("Job board is cloased.");
                case "IllegalArgumentException" ->
                        GUI.addToCommandOutput("Invalid job");
                case "Exception" ->
                        GUI.addToCommandOutput("Catastrophic failure");
            }
        }
    }

    private static void check(String[] parts) {
        GUI.clearTerminal();
        try {
            switch (parts[1]) {
                case "list" -> {
                    switch (parts[2]) {
                        case "corp", "c" -> Headquarters.printCorp();
                        case "mine" -> Headquarters.mineList.forEach(Mine::addMineToGUI);
                        case "farm" -> Headquarters.farmList.forEach(Farm::addFarmToGUI);
                        case "factory" -> Headquarters.factoryList.forEach(Factory::addFactoryToGUI);
                        case "housing" -> Headquarters.housingList.forEach(Housing::addHousingToGUI);
                        case "employee" -> Headquarters.employeeList.forEach(Employee::addEmployeeToGUI);
                        default -> {
                            GUI.clearTerminal();
                            GUI.addToCommandOutput(parts[2] + " is not a checkable item");
                        }
                    }
                }
                case "corp", "c" -> Headquarters.printCorp();
                case "mine" -> {
                    for(Mine m : Headquarters.mineList)
                        if(m.mineID == Integer.parseInt(parts[2])) {
                            m.addMineToGUI();
                            break;
                        }
                    GUI.addToCommandOutput("Mine ID not found");
                }
                case "farm" -> {
                    for(Farm f : Headquarters.farmList)
                        if(f.farmID == Integer.parseInt(parts[2])) {
                            f.addFarmToGUI();
                            break;
                        }
                    GUI.addToCommandOutput("Farm ID not found");
                }
                case "factory" -> {
                    for(Factory f : Headquarters.factoryList)
                        if(f.factoryID == Integer.parseInt(parts[2])) {
                            f.addFactoryToGUI();
                            break;
                        }
                    GUI.addToCommandOutput("Factory ID not found");
                }
                case "housing" -> {
                    for(Housing h : Headquarters.housingList)
                        if(h.housingID == Integer.parseInt(parts[2])) {
                            h.addHousingToGUI();
                            break;
                        }
                }
                case "employee" -> {
                    for(Employee e : Headquarters.employeeList)
                        if(e.employeeID == Integer.parseInt(parts[2])) {
                            e.addEmployeeToGUI();
                            break;
                        }
                    GUI.addToCommandOutput("Employee ID not found");
                }
                default -> {
                    GUI.clearTerminal();
                    GUI.addToCommandOutput(parts[2] + " is not a checkable item");
                }
            }
        }catch(ArrayIndexOutOfBoundsException e) {
            GUI.addToCommandOutput("ID not found");
        }catch(NumberFormatException e) {
            GUI.addToCommandOutput("Invalid ID");
        }
    }
    private static void construct (String[] type) {
        try {
            switch (type[1]) {
                case "factory" -> {
                    try {
                        Factory f = (new Factory(new Product(type[2])));
                        GUI.clearTerminal();
                        GUI.addToCommandOutput("Factory built successfully");
                        Headquarters.factoryList.getLast().addFactoryToGUI();
                    } catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                        return;
                    }
                    return;
                }
                case "mine" -> {
                    if(Headquarters.inventory.contains(Product.prods[0])) {
                        try {
                            Mine m = (new Mine(Headquarters.RawMaterial.valueOf(type[2])));
                        }catch (Exception e) {
                            GUI.addToCommandOutput("Invalid product");
                            return;
                        }
                    }else if(type.length == 3){
                        GUI.addToCommandOutput("No Dowsing-Rod, cannot specify raw material");
                        return;
                    }else {
                        Headquarters.mineList.add(new Mine());
                    }
                    return;
                }
                case "farm" -> {
                    try {
                        Farm f =  (new Farm(Headquarters.CropType.valueOf(type[2].substring(0, 1).toUpperCase() + type[2].substring(1).toLowerCase())));
                    }catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                        return;
                    }
                    return;
                }
                case "housing" -> {
                    try {
                        Housing h = (new Housing(Housing.ResidentType.valueOf(type[2])));
                    }catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                        return;
                    }
                    return;
                }
            }
        }catch(ArrayIndexOutOfBoundsException e) {
            GUI.addToCommandOutput("Invalid command: no input provided.");
            return;
        }
    }
    private static void exit() {
        Saver.saveGame();
        System.exit(0);
    }

    private static void assign (String[] parts) {
        try {
            if(Headquarters.housingList.get(Integer.parseInt(parts[2].substring(0, 1).toUpperCase() + parts[2].substring(1).toLowerCase())) != null) {
                Headquarters.assignEmployee(Integer.parseInt(parts[1]), Integer.parseInt(parts[2].substring(0, 1).toUpperCase() + parts[2].substring(1).toLowerCase()), Headquarters.Building.Housing, Headquarters.housingList.get(Integer.parseInt(parts[2].substring(0, 1).toUpperCase() + parts[2].substring(1).toLowerCase())).housingID);
            }else GUI.addToCommandOutput("Invalid housing ID");
        }catch (NumberFormatException e) {
            GUI.addToCommandOutput("Invalid employee ID");
        }
    }
}