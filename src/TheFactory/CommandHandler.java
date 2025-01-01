package TheFactory;

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
            case "assign" -> System.out.print("LALALALLA");
            case "fire" -> {
                try {
                    Headquarters.killEmployee(Integer.parseInt(parts[1]));
                }catch (Exception e) {
                    GUI.addToCommandOutput("Invalid employee ID");
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
        }catch (IllegalArgumentException e) {
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
                            GUI.addToCommandOutput("Not a checkable item");
                        }
                    }
                }
                case "corp", "c" -> Headquarters.printCorp();
                case "mine" -> Headquarters.mineList.get(Integer.parseInt(parts[2])).addMineToGUI();
                case "farm" -> Headquarters.farmList.get(Integer.parseInt(parts[2])).addFarmToGUI();
                case "factory" -> Headquarters.factoryList.get(Integer.parseInt(parts[2])).addFactoryToGUI();
                case "housing" -> Headquarters.housingList.get(Integer.parseInt(parts[2])).addHousingToGUI();
                case "employee" -> Headquarters.employeeList.get(Integer.parseInt(parts[2])).addEmployeeToGUI();
                default -> {
                    GUI.clearTerminal();
                    GUI.addToCommandOutput("Not a checkable item");
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
                        Headquarters.factoryList.add(new Factory(new Product(type[2])));
                        GUI.clearTerminal();
                        GUI.addToCommandOutput("Factory built successfully");
                        Headquarters.factoryList.getLast().addFactoryToGUI();
                    } catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                    }
                }
                case "mine" -> {
                    if(Headquarters.inventory.contains(Product.prods[0])) {
                        try {
                            Headquarters.mineList.add(new Mine(Headquarters.RawMaterial.valueOf(type[2])));
                        }catch (Exception e) {
                            GUI.addToCommandOutput("Invalid product");
                        }
                    }else if(type.length == 3){
                        GUI.addToCommandOutput("No Dowsing-Rod, cannot specify raw material");
                    }else {
                        Headquarters.mineList.add(new Mine());
                    }
                }
                case "farm" -> {
                    try {
                        Headquarters.farmList.add(new Farm(Headquarters.CropType.valueOf(type[2])));
                    }catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                    }
                }
                case "housing" -> {
                    try {
                        Headquarters.housingList.add(new Housing(Housing.ResidentType.valueOf(type[2])));
                    }catch (Exception e) {
                        GUI.addToCommandOutput("Invalid product");
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e) {
            GUI.addToCommandOutput("Invalid command: no input provided.");
        }
    }
    private static void exit() {
        Saver.saveGame();
        System.exit(0);
    }
}
