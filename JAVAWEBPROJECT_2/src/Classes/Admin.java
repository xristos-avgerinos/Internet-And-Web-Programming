package Classes;

public class Admin extends  Users {
	public int userid;
    public Admin(String username, String password) {
        super(username,password);
        this.userid=Users.getUsersId();  
    }

    public Doctor createDoctor(String username, String password, String name, String surname,String specialty,String amka){

        Doctor doctor=new Doctor(username, password, name, surname, specialty,amka);
        return doctor;
    }
    public String registration(){
    	String insertAdminStatement = "INSERT INTO ADMIN ( username, hashedpassword, salt) VALUES (?,?,?)";
		return(insertAdminStatement);
    }
}
