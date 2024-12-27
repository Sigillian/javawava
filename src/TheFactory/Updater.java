package TheFactory;

public class Updater extends Thread{
    @Override
    public void run (){
        while (true) {
            try {
                Thread.sleep(1500);
            }catch(Exception _) {}
            update();
        }
    }
    private void update() {
        for(Factory f : Headquarters.factoryList)
            f.update();
        for(Mine f : Headquarters.mineList)
            f.update();
        for(Farm f : Headquarters.farmList)
            f.update();
        for(Housing f : Headquarters.housingList)
            f.updateResidents();
    }
}
