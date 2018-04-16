package osama.ned.society.Users;

public class User {

    protected String email;
    protected String password;
    protected String userUID;

    public User(){

    }

    public User(String email, String password, String userUID) {
        this.email = email;
        this.password = password;
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserUID() {
        return userUID;
    }
}
