package Classes;

import java.sql.Time;
import java.sql.Date;


public class Confirmed_appointments extends Available_appointments{

	String patientAMKA;
	String patientNAME;
	
	
	public Confirmed_appointments(Date date, Time startSlotTime, Time endSlotTime, String patientAMKA, String doctorAMKA) {
		super(date, startSlotTime, endSlotTime, doctorAMKA);
		this.patientAMKA = patientAMKA;
	}
	public String addConfirmedAppointment(){
    	String addAppointmentStatement = "insert into confirmed_appointments (date,startSlotTime,endSlotTime,patientAMKA,doctorAMKA) VALUES (?,?,?,?,?)";
		return(addAppointmentStatement);
    }
	public String getPatientAMKA() {
		return patientAMKA;
	}
	public void setPatientAMKA(String patientAMKA) {
		this.patientAMKA = patientAMKA;
	}
	public String getPatientNAME() {
		return patientNAME;
	}
	public void setPatientNAME(String patientNAME) {
		this.patientNAME = patientNAME;
	}

}
