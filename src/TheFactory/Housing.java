package TheFactory;

import java.util.ArrayList;

public class Housing {
    public enum ResidentType {
        hut,
        house,
        condo,
        apartmentComplex,
    }
    private static int lastHousingIDMade = 0;
    public final int housingID;
    private final ResidentType residentType;
    public ArrayList<Employee> residents = new ArrayList<>();
    public Housing(ResidentType residentType) {
        this.residentType = residentType;
        housingID = lastHousingIDMade++;
    }
    private boolean checkIfOvercrowded() {
        return switch (residentType) {
            case hut ->residents.size() >  5;
            case house ->residents.size() >  8;
            case condo -> residents.size() > 16;
            case apartmentComplex -> residents.size() > 40;
        };
    }
    private boolean checkIfSpaceLeft() {
        return
            switch (residentType) {
                case hut -> (int) ((5*1.2) - residents.size());
                case house -> (int) ((8*1.2) - residents.size());
                case condo -> (int) ((16*1.2) - residents.size());
                case apartmentComplex -> (int) ((40*1.2) - residents.size());
            } > 0;
    }
    public void updateResidents() {
        for(Employee r : residents)
            r.update(checkIfOvercrowded() ? 2 : 1);
    }
    public void addEmployee(Employee e) throws Exception {
        if (!checkIfOvercrowded() && checkIfSpaceLeft()) {
            residents.add(e);
        }
        else throw new Exception("Housing is full");
    }

    @Override
    public String toString() {
        return "\nHousing ID: " + housingID + "\n\tResidentIDs: \n\t\t" + Employee.writeEmployeeIDs(residents) + "Food Used: " + ((checkIfOvercrowded() ? 2 : 1) * residents.size());
    }
}