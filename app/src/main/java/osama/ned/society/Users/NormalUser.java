package osama.ned.society.Users;

public class NormalUser extends User {
    private String name;
    private String universityName;
    private String deptName;
    private String yearOfStudy;
    private String rollNo;

    public NormalUser(){

    }

    public NormalUser(String email, String password, String userUID, String name, String universityName, String deptName, String yearOfStudy, String rollNo) {
        super(email, password, userUID);
        this.name = name;
        this.universityName = universityName;
        this.deptName = deptName;
        this.yearOfStudy = yearOfStudy;
        this.rollNo = rollNo;
    }

    @Override
    public String getUserUID() {
        return super.getUserUID();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    public String getDeptName() {
        return deptName;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getUniversityName() {
        return universityName;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }
}
