public class SettingsSingleton {


    private int wattage = 0;
    private int time = 0;
    private boolean doorOpen = false;

    private static SettingsSingleton instance = null;


    public static SettingsSingleton getInstance(){
        if(instance == null){
            instance = new SettingsSingleton();
        }
        return instance;
    }

    private SettingsSingleton(){
        //getting saved settings from db
    }


    public int getWattage() {
        return wattage;
    }

    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }
}
