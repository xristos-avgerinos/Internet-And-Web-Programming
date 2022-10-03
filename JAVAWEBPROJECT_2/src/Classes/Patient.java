package Classes;

public class Patient extends Users {

    private StringBuilder Disease=new StringBuilder();
    /*Constructor*/
    public Patient(String username, String password, String name, String surname, String amka) {
        super(username, password, name, surname,amka);
        

    }
    

    /*Getters & Setters*/

    public String getDisease() {
        return Disease.toString();
    }

    public void setDisease(String disease) {

        Disease.append(", "+disease);
    }

    public String registration(){
    	String insertPatientStatement = "INSERT INTO PATIENT (patientAMKA, username, hashedpassword, name, surname, salt) VALUES (?,?,?,?,?,?)";
		return(insertPatientStatement);
    }
    public void searchAppointmentByName(String doctorsName){
        System.out.println("Searching Appointment by name: "+doctorsName);
    }
    public void searchAppointmentBySpec(String medSpecialization){
        System.out.println("Searching Appointment by specialty:"+medSpecialization);
    }
    public void requestAppointment(String day,String time){
        System.out.println("Patient "+getName()+" Requesting Appointment for "+day+" "+time);
    }
    public void showAppointments(){
        System.out.println("Showing Appointments");
    }
    public void showHistory(){
        System.out.println("Show History of appointments ");
    }
    public void  cancelAppointment(String date){
        System.out.println("Canceling Appointment");
    }
    public void deregistration(){
        System.out.println("Deregistering");
    }

}
