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
 * Servlet implementation class LogIn
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
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
		
		String name = request.getParameter("name");
		String pw = request.getParameter("password");
		String user_id = "";
		String user_name = "";
		String user_role = "";
		
		try {
			String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
			Connection connect = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
			String query = "SELECT * FROM Users WHERE userName='" + name + "' AND userPassword='" + pw + "'";
			PreparedStatement statement = connect.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				user_id = result.getString(1);
				user_name = result.getString(3);
				user_role = result.getString(4);
				String message = user_name;
				String error = "";
				
				request.getSession().setAttribute("userID", user_id);
				request.getSession().setAttribute("userRealName", user_name);
				request.getSession().setAttribute("userRole", user_role);
				request.getSession().setAttribute("userMessage", message);
				
				
				generateOutput(request, response, error);
			} else {
				String error = "User name and/or password is incorrect.  Please try again.";
				
				generateOutput(request, response, error);
			}
			
			connect.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateOutput(HttpServletRequest request, HttpServletResponse response, String error) {
		JSONObject login_package = new JSONObject();
		
		try {
			login_package.put("loginError", error);
			
			response.setContentType("application/json");
			response.getWriter().print(login_package);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
