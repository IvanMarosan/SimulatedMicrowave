public class Preset {
    private String name;
    private int wattage;
    private int time;

    public Preset(String name, int wattage, int time) {
        this.name = name;
        this.wattage = wattage;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
