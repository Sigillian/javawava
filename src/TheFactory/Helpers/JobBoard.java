package TheFactory.Helpers;

import TheFactory.Employee;
import TheFactory.GUI;
import TheFactory.Headquarters;

import java.util.ArrayList;

public class JobBoard {
    public static ArrayList<Employee> es = new ArrayList<>();;
    private static boolean open = false;
    public static void openJobBoard() {
        es.clear();
        open = true;
        GUI.clearTerminal();
        GUI.addToCommandOutput("---Job Board---");
        for(int i = 0; i < 5; i++) {
            es.add(Employee.generateEmployee());
            GUI.addToCommandOutput("Job Seeker #"+(i+1) + es.getLast().toString());
        }
        GUI.addToCommandOutput("Type 'hire [NUMBER] [JOB] [JOBID] [HOUSINGID]' to hire a job seeker.");
    }
    public static void hireEmployee(int eNum, Headquarters.Building job, int jobID, int houseID) {
        if(open) {
            open = false;
            GUI.clearTerminal();
            Employee.assignEmployee(es.get(eNum - 1), job);
            Headquarters.assignEmployee(es.get(eNum - 1).employeeID, houseID, job, jobID);
            GUI.addToCommandOutput("Hired " + es.get(eNum - 1).toString());
            es.clear();
        }else {
            GUI.clearTerminal();
            GUI.addToCommandOutput("Job board is closed.");
        }
    }
}
