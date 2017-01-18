package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {
	
	private DataSource dataSource;

	public StudentDBUtil(DataSource theDataSource) {
		this.dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		
		// create empty list
		List<Student> students = new ArrayList<>();
		
		// create JDBC objects
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRS = null;
		
		try {
			// get connection
			myConn = dataSource.getConnection();
			
			// create sql statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			myStmt = myConn.createStatement();

			// execute query
			myRS = myStmt.executeQuery(sql);

			// process result set
			while (myRS.next()){
				//retrieve data from ResultSet row
				int id = myRS.getInt("id");
				String firstName = myRS.getString("first_name");
				String lastName = myRS.getString("last_name");
				String email = myRS.getString("email");
				
				// create new Student object
				Student currentStudent = new Student(id, firstName, lastName, email);
				
				// add Student object to list
				students.add(currentStudent);
			}
			// return list
			return students;
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, myRS);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRS) {
		try {
			if (myRS != null) {
				myRS.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();	// doesn't really close ... returns it to connection pool
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student newStudent) throws Exception {
		
		// create JDBC objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get connection
			myConn = dataSource.getConnection();
			
			// create PreparedStatement for INSERT
			String sql = "INSERT INTO student "
					+ "(first_name, last_name, email) "
					+ "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set param values
			myStmt.setString(1, newStudent.getFirstName());
			myStmt.setString(2, newStudent.getLastName());
			myStmt.setString(3, newStudent.getEmail());
			
			// execute SQL INSERT
			myStmt.execute();
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentID) 
			throws Exception {
		
		// create empty Student and int studentID
		Student theStudent = null;
		int studentID;
		
		// create JDBC objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRS = null;
		
		try {
			// convert theStudentID to int
			studentID = Integer.parseInt(theStudentID);
			
			// get connection
			myConn = dataSource.getConnection();
			
			// create PreparedStatement SELECT theStudent
			String sql = "SELECT * FROM student WHERE id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, studentID);
			
			// execute QUERY
			myRS = myStmt.executeQuery();
			
			// retrieve data from ResultSet and assign to empty Student
			if (myRS.next()) {
				String firstName = myRS.getString("first_name");
				String lastName = myRS.getString("last_name");
				String email = myRS.getString("email");
				
				theStudent = new Student(studentID, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentID);
			}
			
			// return Student
			return theStudent;
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, myRS);
		}
	}

	public void updatedStudent(Student updatedStudent) 
			throws Exception {
		
		// create JDBC objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get connection
			myConn = dataSource.getConnection();
			
			// create PreparedStatement
			String sql = "UPDATE student "
					+ "SET first_name=?, last_name=?, email=? "
					+ "WHERE id=?";
			
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, updatedStudent.getFirstName());
			myStmt.setString(2, updatedStudent.getLastName());
			myStmt.setString(3, updatedStudent.getEmail());
			myStmt.setInt(4, updatedStudent.getId());
					
			// execute UPDATE
			myStmt.execute();
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentID) 
			throws Exception {
		
		//create empty int student id
		int studentID;
		
		// create JDBC objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// convert studentID to int
			studentID = Integer.parseInt(theStudentID);
			
			// get connection
			myConn = dataSource.getConnection();
			
			// create SQL and PreparedStatement to DELETE student
			String sql = "DELETE FROM student WHERE id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, studentID);
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, null);
		}
	}
}
