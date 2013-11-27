package dreams;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Dream
 */
@WebServlet("/DreamServlet")
public class DreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DreamServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {			
			String dreamid = request.getParameter("dreamID");
			
			// UNCOMMENT WHEN LOGIN ADDED
			String userid = (String) request.getSession().getAttribute("userID");
			//String userid = "1";
			String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
			Connection conn = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
			// SELECT THE DREAM
			String query = "SELECT * FROM Dreams WHERE dreamID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, dreamid);
			ResultSet results = stmt.executeQuery();
			
			if(results.next()){ 
				// SELECT USERNAME
				String username = "";
				String query_username = "SELECT userName FROM Users WHERE userID = ?";
				PreparedStatement stmt_username = conn.prepareStatement(query_username);
				stmt_username.setString(1, results.getString(2));
				ResultSet result_username = stmt_username.executeQuery();
				if (result_username.next()){
					username = result_username.getString(1);
				}
				
				Dream dream = new Dream(results.getString(3), results.getString(4), results.getString(1), results.getString(2), results.getString(5));
				dream.setUsername(username);
				request.setAttribute("dream", dream);

			}
			
			// SELECT THE COMMENTS
			ArrayList<Comment> comments = new ArrayList<Comment>();
			query = "SELECT * FROM Comments WHERE dreamID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, dreamid);
			results = stmt.executeQuery();
			
			while(results.next()){
				String c_userid = results.getString(3);
				
				// Make another query to get the username
				String username = "";
				String query_username = "SELECT userName FROM Users WHERE userID = ?";
				PreparedStatement stmt_username = conn.prepareStatement(query_username);
				stmt_username.setString(1, c_userid);
				ResultSet result_username = stmt_username.executeQuery();
				if (result_username.next()){
					username = result_username.getString(1);
				}
					
				String comment_id = results.getString(1);
				String dream_id = results.getString(2);
				String comment = results.getString(4);
				String date = results.getString(5);
				Comment comm = new Comment(comment_id, dream_id, username, comment, date);
				comments.add(comm);
			}
			
			request.setAttribute("comments", comments);

			// QUERY IF ADMIN OR NOT
			String query_admin = "SELECT userRole FROM Users WHERE userID = ?"; 
			PreparedStatement stmt_admin = conn.prepareStatement(query_admin);
			stmt_admin.setString(1, userid);
			ResultSet result_admin = stmt_admin.executeQuery();
			if (result_admin.next()){
				String role = result_admin.getString(1);
				request.setAttribute("role", role);
			}
			
			
			conn.close();
			
			// Redirect
			request.getRequestDispatcher("/WEB-INF/views/Dream.jsp").forward(request, response);
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
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			String func = request.getParameter("func");
			
			String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
			Connection conn = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
			if (func.equals("addComment")){
				// Used for testing purposes
					
				String userid = (String) request.getSession().getAttribute("userID");
				// Current Date
				String date = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()).toString();
				String comment = request.getParameter("comment");
				String dreamid = request.getParameter("dreamID");
				
				String query = "INSERT INTO Comments (`dreamID`, `userID`, `commentMessage`, `commentDate`) " +
				"VALUES (?,?,?,?)";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, dreamid);
				stmt.setString(2, userid);
				stmt.setString(3, comment);
				stmt.setString(4, date);
				stmt.executeUpdate();
			}
			else if (func.equals("deleteComment")){
				String commentid = request.getParameter("commentID");
				String query_delete = "DELETE FROM Comments WHERE commentID = ?";
				PreparedStatement stmt_delete = conn.prepareStatement(query_delete);
				stmt_delete.setString(1, commentid);
				stmt_delete.executeUpdate();
			}
			
			conn.close();
			
			doGet(request,response);
		} 
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
		
	}

}
