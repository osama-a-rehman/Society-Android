package osama.ned.society.Users;

public class SocietyUser extends User {

    private String imageUri;
    private String societyName;
    private String presidentName;
    private String contact;

    public SocietyUser(){

    }

    public SocietyUser(String email, String password, String userUID, String imageUri, String societyName, String presidentName, String contact) {
        this.email = email;
        this.password = password;
        this.userUID = userUID;

        this.imageUri = imageUri;
        this.societyName = societyName;
        this.presidentName = presidentName;
        this.contact = contact;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getSocietyName() {
        return societyName;
    }

    public String getPresidentName() {
        return presidentName;
    }

    public String getContact() {
        return contact;
    }

}
