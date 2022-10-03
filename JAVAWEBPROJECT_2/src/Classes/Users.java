package Classes;

public class Users {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String amka;
    private String salt;

    private static int usersCounter=1;

    public void login(){
        System.out.println("Logging in");
    }

    public void logout(){
        System.out.println("Logging out");
    }


    /*Getters & Setters*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public static int getUsersId() {
    
        return usersCounter++;
    }
    
    public String getAMKA() {
        return amka;
    }

    public void setAMKA(String amka) {
        this.amka = amka;
    }
    /*Constructor*/

    public Users(String username, String password, String name, String surname,String amka) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.amka = amka;
    }
    public Users(String username,String password) {
    	this.username=username;
    	this.password=password;
    }
    


}
