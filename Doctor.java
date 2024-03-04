package project2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
	
	private Scanner sc=new Scanner(System.in);
	private Connection con=Factory.connectionProvider();
	
	public void viewDoctor() throws SQLException{
		
		Statement stmt=con.createStatement();
		
		ResultSet rs=stmt.executeQuery("select * from hospital.doctors;");
		
		System.out.println("+-----------+--------------------+----------------+-----+------------+");
		System.out.println("| doctor id |     doctor name    | specialization | exp |   mobile   |");
		System.out.println("+-----------+--------------------+----------------+-----+------------+");
		while(rs.next()) {
		
			System.out.printf("|%-11s|%-20s|%-16s|%-5s|%-12s|\n",rs.getInt("id"),rs.getString("name"),rs.getString("specialization"),rs.getString("experience"),rs.getLong("mobile"));
			System.out.println("+-----------+--------------------+----------------+-----+------------+");
		}
	}
	public int checkDoctorAvailability() throws SQLException{
		Patient patient=new Patient();
		System.out.println("enter doctor name");
		String name=sc.nextLine();
		PreparedStatement psmt=con.prepareStatement("select * from hospital.doctors where doctors.name=? ");
		psmt.setString(1, name);
		ResultSet rs=psmt.executeQuery();
		
		if(rs.next()==true) {
			int d_id1=rs.getInt("id");
			String date="";
			System.out.println("1.check appointment for today's date");
			System.out.println("2.check appointment date manually");
			int option=sc.nextInt();
			switch(option) {
			case 1:	Statement st1=con.createStatement();
					ResultSet rs2=st1.executeQuery("select current_date");
					DateFormat dtft=new SimpleDateFormat("yyyy-MM-dd");
					if(rs2.next())	date=dtft.format(rs2.getDate(1));
					System.out.println(date);
					break;
					
			case 2:	System.out.println("enter date yyyy-mm-dd");
					date=sc.next();break;
			default: return -2;		
			}
			PreparedStatement psmt1=con.prepareStatement("select * from hospital.appointments where appointments.appointment_date=? and appointments.doctor_id=?");
			psmt1.setString(1,date);
			psmt1.setInt(2,d_id1);
			ResultSet rs1=psmt1.executeQuery();
			if(rs1.next())	return 0;
			else {
				System.out.println("doctor availabel...");
				System.out.println("would you like to fix an appointment? y/n");
				char c=sc.next().charAt(0);
				if(c=='y'||c=='Y')	patient.addPatient(name, date, d_id1);
				else	System.out.println("thank you...");
				return 1;
			}
		}
		else return -1;
	}
}
