package dreams;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String user_name = request.getParameter("userName");
		String user_real_name = request.getParameter("realName");
		String user_password = request.getParameter("pw");
		String error = "";
		String message = "";
		
		try {
			String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
			Connection connect = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
			String query = "SELECT * FROM Users WHERE userName='" + user_name + "'";
			PreparedStatement statement = connect.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				error = "User name already exists.";
			} else {
				query = "INSERT INTO Users (userName, userRealName, userRole, userPassword) VALUES ('" + user_name + "', '" + user_real_name + "', 0, '" + user_password + "')";
				statement = connect.prepareStatement(query);
				statement.executeUpdate();
				
				message = "Hello, " + user_real_name + ".  You are logged in as " + user_name + ".";
				
				request.getSession().setAttribute("userName", user_name);
				request.getSession().setAttribute("userRealName", user_real_name);
				request.getSession().setAttribute("userMessage", message);
				request.getSession().setAttribute("userRole", "0");
				
				String query_userid = "SELECT userID FROM Users WHERE userName ='" + user_name + "'";
				PreparedStatement statement_userid = connect.prepareStatement(query_userid);
				ResultSet result_userid = statement_userid.executeQuery();
				if (result_userid.next()){
					request.getSession().setAttribute("userID", result_userid.getString(1));
				}
				
			}
			connect.close();
			generateOutput(request, response, error);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateOutput(HttpServletRequest request, HttpServletResponse response, String error) {
		JSONObject register_package = new JSONObject();
		
		try {
			register_package.put("registerError", error);
			
			response.setContentType("application/json");
			response.getWriter().print(register_package);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
