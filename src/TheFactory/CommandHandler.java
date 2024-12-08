package TheFactory;

public class CommandHandler {
    public static void handleCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) {
            GUI.addToCommandOutput("Invalid command: no input provided.");
            return;
        }

        String mainCommand = parts[0].toLowerCase();

        switch (mainCommand) {
            case "check" -> check(parts[1]);
            case "sell" -> System.out.print("LALALALLA");
            case "shop" -> Shop.openShop();
            case "demolish" -> System.out.print("LALALALLA");
            case "construct" -> System.out.print("LALALALLA");
            case "make" -> System.out.print("LALALALLA");
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
            case "exit" -> GUI.clearTerminal();
            case "buy" -> {
                try {
                    Shop.buyProduct(new Product(parts[1]));
                }catch (Exception e) {
                    GUI.addToCommandOutput("Invalid product: " + parts[1]);
                }
            }
            case "jobboard" -> JobBoard.openJobBoard();
            case "hire" -> {
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
            default -> GUI.addToCommandOutput("Unknown command: " + mainCommand);
        }
    }
    public static void check(String type) {
        switch (type) {
            case "corp" -> Headquarters.printCorp();
        }
    }
}
