package project2;

import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
class Patient {
	
	private Scanner sc=new Scanner(System.in);
	private Connection con=Factory.connectionProvider();
	
	public void addPatient(String d_name,String date,int d_id) throws SQLException{
		
		System.out.println("enter patient name");
		String p_name=sc.nextLine();
		System.out.println("enter patient age");
		int age=sc.nextInt();
		System.out.println("enter patient gender");
		String gen=sc.next();
		System.out.println("enter patient address");
		String adrs=sc.next();
		int rid=0;ResultSet rs1;PreparedStatement psmt1;
	
		boolean b=true;
		while(b) {
			rid=(int)(Math.random()*10000000);		
			psmt1=con.prepareStatement("select id from hospital.patients where ref_id=?");
			psmt1.setInt(1, rid);
			rs1=psmt1.executeQuery();
			if(rs1.next()==true)	b=true;
			else b=false;
		}
		
		PreparedStatement psmt2=con.prepareStatement("insert into hospital.patients(name,age,gender,address,ref_id,reg_date) values(?,?,?,?,?,?)");
		psmt2.setString(1, p_name);
		psmt2.setInt(2, age);
		psmt2.setString(3,gen);
		psmt2.setString(4,adrs);
		psmt2.setInt(5,rid);
		Statement stmt=con.createStatement();
		ResultSet rs4=stmt.executeQuery("select current_date");
		rs4.next();
		psmt2.setDate(6,rs4.getDate(1));
		psmt2.executeUpdate();
		
		PreparedStatement psmt3=con.prepareStatement("select id from hospital.patients where ref_id=?");
		psmt3.setInt(1,rid);
		ResultSet rs2=psmt3.executeQuery();
		int p_id=-1;
		if(rs2.next())	p_id=rs2.getInt("id");
		
		PreparedStatement psmt4=con.prepareStatement("insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)");
		psmt4.setInt(1, p_id);
		psmt4.setInt(2, d_id);
		psmt4.setString(3, date);
		
		psmt4.executeUpdate();
		System.out.println("appointment fixed...");
		
		PreparedStatement psmt5=con.prepareStatement("select id from hospital.appointments where patient_id=? && doctor_id=? && appointment_date=?");
		psmt5.setInt(1,p_id);
		psmt5.setInt(2,d_id);
		psmt5.setString(3,date);
		
		ResultSet rs3=psmt5.executeQuery();
		if(rs3.next())	System.out.println("your appointment id is :"+rs3.getInt("id"));
		
		con.close();
	}
	public void viewAllPatient() throws SQLException {
		Statement stmt=con.createStatement();
		
		ResultSet rs=stmt.executeQuery("select * from hospital.patients;");
		
		System.out.println("+------------+---------------------+-----------+-----+--------+-------------+------------+");
		System.out.println("| patient id |     patient name    | reg. date | age | gender |   address   |reference id|");
		System.out.println("+------------+---------------------+-----------+-----+--------+-------------+------------+");
		while(rs.next()) {
			System.out.printf("|%-12s|%-21s|%-11s|%-5s|%-8s|%-13s|%-12s|\n",rs.getInt("id"),rs.getString("name"),rs.getDate("reg_date"),rs.getInt("age"),rs.getString("gender"),rs.getString("address"),rs.getInt("ref_id"));
			System.out.println("+------------+---------------------+-----------+-----+--------+-------------+------------+");
		}
	}
	public  void countAppointmentsByDateRange() throws SQLException {
		System.out.println("enter the from date");
		System.out.println("yyyy-mm-dd");
		String start=sc.next();
		System.out.println("to date");
		System.out.println("yyyy-mm-dd");
		String end=sc.next();
		PreparedStatement psmt=con.prepareStatement("select * from hospital.appointments where appointment_date between ? and ?");
		psmt.setString(1,start);
		psmt.setString(2, end);
		ResultSet rs=psmt.executeQuery();
		int count=0;
		while(rs.next()) count++;
		
		System.out.println("total appointment for the date range "+start+" and "+end+" : "+count);
	
	}
	public void checkPatient() throws SQLException{
		System.out.println("enter the appointment id");
		int id=sc.nextInt();
		Statement stmt1=con.createStatement();
		ResultSet rs1=stmt1.executeQuery("select * from appointments where id="+id);
		
		if(rs1.next()) {
			int p_id=rs1.getInt("patient_id");
			int d_id=rs1.getInt("doctor_id");
			Statement stmt2=con.createStatement();
			ResultSet rs2=stmt2.executeQuery("select * from patients where id="+p_id);
			Statement stmt3=con.createStatement();
			ResultSet rs3=stmt3.executeQuery("select * from doctors where id="+d_id);
				System.out.println("+------------------+-----------+-----+--------+---------------+-------------+");
				System.out.println("|   patient name   | reg. date | age | gender |appoinment date| doctor name |");
				System.out.println("+------------------+-----------+-----+--------+---------------+-------------+");
			if(rs2.next()&&rs3.next())
				System.out.printf("|%-18s|%-11s|%-5s|%-8s|%-15s|%-13s|\n",rs2.getString("name"),rs2.getDate("reg_date"),rs2.getInt("age"),rs2.getString("gender"),rs1.getString("appointment_date"),rs3.getString("name"));
			System.out.println("+------------------+-----------+-----+--------+---------------+-------------+");
			System.out.println("would you like to cancel the appointment? y/n");
			sc.nextLine();
			String key=sc.nextLine();
			if(key.equals("y")) {
				PreparedStatement psmt2=con.prepareStatement("update patients set remark = 'cancelled' where id="+p_id);
				psmt2.executeUpdate();
				PreparedStatement psmt1=con.prepareStatement("delete from appointments where id="+id);
				psmt1.executeUpdate();
				System.out.println("appointment cancelled for the appointment id : "+id);
			}
			System.out.println("thank you...");
		}
		else	System.out.println("!!!no appointment fixed for the id : "+id);
			
	}
}
