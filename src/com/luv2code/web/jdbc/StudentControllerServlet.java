package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

	// good place for you put your own custom initialization work
	// work you'd normally do in a constructor, when you work with
	// servlets, go in an init() method
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create our student db util ... and pass in the conn pool/ datasource
		try { 
			studentDBUtil = new StudentDBUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			// route to the appropriate method
			switch (theCommand) {
			
			case "LIST":
				listStudents(request, response);
				break;

			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
			
			default:
				listStudents(request, response);
			}
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {

        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
                    
            // route to the appropriate method
            switch (theCommand) {
                            
            case "ADD":
                addStudent(request, response);
                break;
                                
            default:
                listStudents(request, response);
            }
                
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }		
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// read the student id from form data
		String theStudentID = request.getParameter("studentID");
		
		// delete student from database
		studentDBUtil.deleteStudent(theStudentID);
		
		// send back to main page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// read the student info from the form data
		int id = Integer.parseInt(request.getParameter("studentID"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create new student object
		Student updatedStudent = new Student(id, firstName, lastName, email);
		
		// perform update on database
		studentDBUtil.updatedStudent(updatedStudent);
		
		// send back to main page (the student list)
		listStudents(request, response);	
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// read student id from the form data
		String theStudentID = request.getParameter("studentID");
		
		// get student form database (via DAO)
		Student theStudent = studentDBUtil.getStudent(theStudentID);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to .jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// read student info from the form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new Student object
		Student newStudent = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentDBUtil.addStudent(newStudent);
		
        // SEND AS REDIRECT to avoid multiple-browser reload issue
        response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// get students from DAO
		List<Student> students = studentDBUtil.getStudents();
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		//send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
}
