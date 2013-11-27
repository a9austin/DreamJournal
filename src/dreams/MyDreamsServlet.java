package dreams;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class MyDreams
 */
@WebServlet("/MyDreamsServlet")
public class MyDreamsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyDreamsServlet() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check if the user is logged in
		
		// Get all of the users dreams select from DB
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		
		// Figure out which books have been checked out
		try {
			
			// Uncomment when ready
			String userid = (String) request.getSession().getAttribute("userID");
			
			ArrayList<Dream> dreams = new ArrayList<Dream>();
			
			// For testing
			//String userid = "1";
			
			String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
			Connection conn = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
			// "select bookID from CheckedOut"; QUERY HERE
			String query = "SELECT * FROM Dreams WHERE userID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userid);
			ResultSet results = stmt.executeQuery();
			while(results.next()){ 
				Dream dream = new Dream(results.getString(3), results.getString(4), results.getString(1), results.getString(2), results.getString(5));
				dreams.add(dream);
			}
			
			request.setAttribute("dreams", dreams);
			
			conn.close();
			
			// Redirect
			request.getRequestDispatcher("/WEB-INF/views/MyDreams.jsp").forward(request, response);
		} 
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirect when new dream button is clicked to the new dream page
		System.out.println("Got in here doPost");
	}

}
