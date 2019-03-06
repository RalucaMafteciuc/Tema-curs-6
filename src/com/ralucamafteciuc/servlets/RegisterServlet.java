package com.ralucamafteciuc.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ralucamafteciuc.database.DBConnection;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String error = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		session.removeAttribute("UsernameError");	
		session.removeAttribute("EmailError");		
		session.removeAttribute("PasswordError");

		if(session.getAttribute("Username") == null) {
			RequestDispatcher view = request.getRequestDispatcher("register.jsp");
			view.forward(request, response);
		} else
			response.sendRedirect("/Curs6-WebApp/home");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		DBConnection dbConnection = new DBConnection();
		HttpSession session = request.getSession();
		
		int hasEntered = 0;
		
		if (username == null || username.trim().isEmpty()) {
			session.setAttribute("UsernameError", "Username is missing");
		} else
			try {
				if(dbConnection.checkIfUserExists("users", username)){
					session.setAttribute("UsernameError", "Username already exists");
				} else {
					session.removeAttribute("UsernameError");
					hasEntered++;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		if (email == null || email.trim().isEmpty()) {
			session.setAttribute("EmailError", "Email is missing");
		} else {
			session.removeAttribute("EmailError");
			hasEntered++;
		}		
		
		if (password == null || password.trim().isEmpty()) {
			session.setAttribute("PasswordError", "Password is missing");
		} else {
			session.removeAttribute("PasswordError");
			hasEntered++;
		}
						
		if(hasEntered == 3) {
			try {
				dbConnection.insertExample(username, password, email, "users");
				session.setAttribute("Username", username);
				session.setAttribute("Email", email);
				response.sendRedirect("/Curs6-WebApp/home");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {			
			RequestDispatcher view = request.getRequestDispatcher("register.jsp");
			view.forward(request, response);
		}

	}
}