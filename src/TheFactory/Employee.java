package TheFactory;

import java.util.ArrayList;
import java.util.Random;

    public class Employee {
        public static int lastEmployeeIDMade = 0;
        public Headquarters.Building jobType;
        public int strength, intelligence, employeeID, age;
    public Employee(int age, int strength, int intelligence, int employeeID, Headquarters.Building jobType) {
        this.age = age;
        this.strength = strength;
        this.intelligence = intelligence;
        this.employeeID = employeeID;
        this.jobType = jobType;
        Headquarters.employeeList.add(this);
    }
    public Employee(int age, int strength, int intelligence) {
        this.age = age;
        this.strength = strength;
        this.intelligence = intelligence;
        this.jobType = null;
        Headquarters.employeeList.add(this);
    }
    public static Employee generateEmployee(Headquarters.Building job) {
        Random rg = new Random();
        return new Employee(rg.nextInt(5840,10950), rg.nextInt(100) + 1, rg.nextInt(100) + 1, lastEmployeeIDMade++, job);
    }
    public static Employee generateEmployee() {
        Random rg = new Random();
        return new Employee(rg.nextInt(5840,10950), rg.nextInt(100) + 1, rg.nextInt(100) + 1);
    }
    public static void assignEmployee(Employee e, Headquarters.Building job) {
        e.jobType = job;
    }
    public static void buyEmployee(Employee e) {
        Headquarters.employeeList.add(new Employee(e.age, e.strength, e.intelligence, lastEmployeeIDMade++, null));
    }
    public void update(int foodConsumption) {
        age++;
        if(age > 13140 && new Random().nextInt(120 - (age / 365)) % 5 == 0) {
            Headquarters.killEmployee(employeeID);
            return;
        }
        if(Headquarters.foodSupply >= foodConsumption)
            Headquarters.foodSupply-=foodConsumption;
        else if (new Random().nextInt(8) % 2 == 0) Headquarters.killEmployee(employeeID);
        try {
            if (age > 7300 && age < 22000 && new Random().nextInt() % (525 / (Headquarters.foodSupply * 5 / ((Headquarters.employeeList.size()) + 1))) == 0) {//Add a child at random
                Headquarters.employeeList.add(
                        new Employee(
                                0,
                                (strength > 84 ? (int) (strength * new Random().nextDouble(.8, 1.2)) : 100),
                                (intelligence > 84 ? (int) (intelligence * new Random().nextDouble(.8, 1.2)) : 100)
                        ));
            }
        }catch (Exception _) {}
    }
    public static String writeEmployeeIDs(ArrayList<Employee> e) {
        StringBuilder s = new StringBuilder();
        for(Employee a : e)
            s.append((a.employeeID)).append(",\n\t\t");
        return s.toString();
    }

    @Override
    public String toString() {
        if(jobType != null)
            return "\nEmployee ID: " + employeeID + "\n\tStrength: " + strength + "\n\tIntelligence: " +intelligence + "\n\tAge: " + ((int)age / 365);
        else return "\n\tStrength: " + strength + "\n\tIntelligence: " +intelligence + "\n\tAge: " + ((int)age / 365);
    }

    public void addEmployeeToGUI() {
        GUI.addToCommandOutput("Employee ID: " + employeeID);
        GUI.addToCommandOutput("Strength: " + strength);
        GUI.addToCommandOutput("Intelligence: " + intelligence);
        GUI.addToCommandOutput("Age: " + (age / 365));
    }
}
