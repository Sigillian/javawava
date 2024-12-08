package TheFactory;

import java.util.ArrayList;
import java.util.Random;

public class Employee {
    private static int lastEmployeeIDMade = 0;
    public int strength, intelligence, employeeID;
    private int age;
    public Headquarters.Building jobType;
    public Employee(int age, int strength, int intelligence, int employeeID, Headquarters.Building jobType) {
        this.age = age;
        this.strength = strength;
        this.intelligence = intelligence;
        this.employeeID = employeeID;
        this.jobType = jobType;
    }public Employee(int age, int strength, int intelligence) {
        this.age = age;
        this.strength = strength;
        this.intelligence = intelligence;
        this.jobType = null;
    }
    public static Employee generateEmployee(Headquarters.Building job) {
        Random rg = new Random();
        return new Employee(rg.nextInt(10950) + 1, rg.nextInt(100) + 1, rg.nextInt(100) + 1, lastEmployeeIDMade++, job);
    }
    public static Employee generateEmployee() {
        Random rg = new Random();
        return new Employee(rg.nextInt(10950) + 1, rg.nextInt(100) + 1, rg.nextInt(100) + 1);
    }
    public static void assignEmployee(Employee e, Headquarters.Building job) {
        e.jobType = job;
    }
    public static void buyEmployee(Employee e) {
        Headquarters.employeeList.add(new Employee(e.age, e.strength, e.intelligence, lastEmployeeIDMade++, null));
    }
    public void update(int foodConsumption) {
        age++;
        if(age > 13140 && new Random().nextInt(120 - (age / 365)) % 5 == 0)
            Headquarters.killEmployee(employeeID);
        if(Headquarters.foodSupply >= 1)
            Headquarters.foodSupply--;
        else if(new Random().nextInt(5) == 0)
            Headquarters.killEmployee(employeeID);
    }
    public static String writeEmployeeIDs(ArrayList<Employee> e) {
        String s = "";
        for(Employee a : e)
            s += (a.employeeID) + ",\n\t\t";
        return s;
    }

    @Override
    public String toString() {
        if(jobType != null)
            return "\nEmployee ID: " + employeeID + "\n\tStrength: " + strength + "\n\tIntelligence: " +intelligence + "\n\tAge: " + ((int)age / 365);
        else return "\n\tStrength: " + strength + "\n\tIntelligence: " +intelligence + "\n\tAge: " + ((int)age / 365);
    }
}
