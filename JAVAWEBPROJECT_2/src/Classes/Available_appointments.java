package Classes;

import java.sql.Time;
import java.sql.Date;

public class Available_appointments {
	Date date;
    Time startSlotTime;
    Time endSlotTime;
    String doctorAMKA; 
    boolean  isAvailable;
    String doctorNAME;
    
    public Available_appointments(Date date,Time startSlotTime,Time endSlotTime,String doctorAMKA) {
        this.date = date;
        this.startSlotTime = startSlotTime;
        this.endSlotTime = endSlotTime;
        this.doctorAMKA = doctorAMKA;
    }
    public String addAvailableAppointment(){
    	String addAppointmentStatement = "insert into available_appointments (date,startSlotTime,endSlotTime,doctorAMKA,isAvailable) VALUES (?,?,?,?,?)";
		return(addAppointmentStatement);
    }
    
    public String getDoctorNAME() {
		return doctorNAME;
	}

	public void setDoctorNAME(String doctorNAME) {
		this.doctorNAME = doctorNAME;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getStartSlotTime() {
		return startSlotTime;
	}
	public void setStartSlotTime(Time startSlotTime) {
		this.startSlotTime = startSlotTime;
	}
	public Time getEndSlotTime() {
		return endSlotTime;
	}
	public void setEndSlotTime(Time endSlotTime) {
		this.endSlotTime = endSlotTime;
	}
	public String getDoctorAMKA() {
		return doctorAMKA;
	}
	public void setDoctorAMKA(String doctorAMKA) {
		this.doctorAMKA = doctorAMKA;
	}
	public boolean getisAvailable() {
        return isAvailable;
	}
	public void setisAvailable(boolean isAvailable) {
    	this.isAvailable = isAvailable; 
    }
    
}
