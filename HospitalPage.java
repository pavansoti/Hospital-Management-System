	package project2;

import java.sql.SQLException;
import java.util.Scanner;

public class HospitalPage {

	public static void main(String[] args) throws SQLException {
		Scanner sc=new Scanner(System.in);
		Doctor doctor=new Doctor();
		Patient patient=new Patient();
		
		System.out.println("welcome of my hospital");
		System.out.println("press any key to continue");
		String key=sc.next();
		
		if(key.length()>0) {
			System.out.println("choose option");
			System.out.println("1.view all doctor");
			System.out.println("2.check doctor availability");
			System.out.println("3.check patient appointment");
			System.out.println("4.view all patients details");
			System.out.println("5.count appointments by date range");
			int option=sc.nextInt();
			switch(option) {
			
			case 1:	doctor.viewDoctor();break;
			case 2:	int value1=doctor.checkDoctorAvailability();
					if(value1==-1) System.out.println("!!!please enter valid doctor name");
					else if(value1==0)	System.out.println("!!!doctor not availabel");
					else if(value1==-2)	System.out.println("!!!enter valid option");
					break;
			case 3:	patient.checkPatient(); break;
			case 4: patient.viewAllPatient();break;
			case 5: patient.countAppointmentsByDateRange();break;
			default: System.out.println("!!!please choose correct option");
			}		
		}
	}
}
