package bodyfatcontrol.github;

public class UserProfile {
    private int id;
    private long date;
    private int userBirthYear;
    private int userGender;
    private int userHeight;
    private int userWeight;

    public UserProfile() {
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getUserBirthYear() {
        return userBirthYear;
    }

    public void setUserBirthYear(int userBirthYear) {
        this.userBirthYear = userBirthYear;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public int getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(int userHeight) {
        this.userHeight = userHeight;
    }

    public int getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }
}
