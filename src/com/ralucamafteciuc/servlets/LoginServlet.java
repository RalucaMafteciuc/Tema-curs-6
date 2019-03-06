package com.ralucamafteciuc.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ralucamafteciuc.database.DBConnection;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		session.removeAttribute("LoginError");	
		
		if(session.getAttribute("Username") == null) {
			RequestDispatcher view = request.getRequestDispatcher("login.jsp");
			view.forward(request, response);
		} else
			response.sendRedirect("/Curs6-WebApp/home");				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		DBConnection dbConnection = new DBConnection();	
		HttpSession session = request.getSession();
		
		try {
			if(dbConnection.checkCredentialsForLogin(username, password, "users")) {
				session.removeAttribute("LoginError");
				session.setAttribute("Username", username);					
				response.sendRedirect("/Curs6-WebApp/home");	
			} else {
				session.setAttribute("LoginError", "Try again!");
				RequestDispatcher view = request.getRequestDispatcher("login.jsp");
				view.forward(request, response);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
