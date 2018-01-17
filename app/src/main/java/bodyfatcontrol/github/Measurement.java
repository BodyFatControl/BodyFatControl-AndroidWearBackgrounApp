package bodyfatcontrol.github;

public class Measurement {
    private long date; // UTC Unix BUT in minutes
    private int HR;
    private double caloriesPerMinute;
    private double caloriesEERPerMinute;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getHR() {
        return HR;
    }

    public void setHR(int HR) {
        this.HR = HR;
    }

    public double getCaloriesPerMinute() {
        return caloriesPerMinute;
    }

    public void setCaloriesPerMinute(double caloriesPerMinute) {
        this.caloriesPerMinute = caloriesPerMinute;
    }

    public double getCaloriesEERPerMinute() {
        return caloriesEERPerMinute;
    }

    public void setCaloriesEERPerMinute(double caloriesEERPerMinute) {
        this.caloriesEERPerMinute = caloriesEERPerMinute;
    }
}