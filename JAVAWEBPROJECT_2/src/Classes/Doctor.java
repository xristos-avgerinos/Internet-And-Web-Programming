package Classes;

import java.util.ArrayList;

public class Doctor extends Users {


    private final String specialty;
    private int rate=0;//Ratings will be increasing/decreasing by patients rates

    /*Constructor*/
    public Doctor(String username, String password, String name, String surname,String specialty,String amka) {
        super(username, password, name, surname,amka);
        this.specialty=specialty;

    }
    public String registration(){
    	String insertDoctorStatement = "INSERT INTO DOCTOR (doctorAMKA, username, hashedpassword, name, surname, specialty, ADMIN_userid, salt) VALUES (?,?,?,?,?,?,?,?)";
		return(insertDoctorStatement);
    }
    /*Getters & Setters*/
    public String getSpecialty() {
        return specialty;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
    	this.rate = rate; 
    	}



    public void showAvailability(String date){
        System.out.println("Showing availability for "+date);
    }
    public void showAppointmentsByDay(String day){
        System.out.println("Showing appointments of "+day);
    }
    public void showAppointmentsByWeek(){
        System.out.println("Showing appointment for next 7 days ");
    }
    public void confirmAppointment(){
        System.out.println("Doctor " +this.getName()+" Confirming Appointment");
    }
    public void  cancelAppointment(String date,String time){
        System.out.println("Canceling appointment for "+date+" at "+time );
    }
}
