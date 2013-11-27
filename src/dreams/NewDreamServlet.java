package dreams;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class NewDream
 */
@WebServlet("/NewDreamServlet")
public class NewDreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewDreamServlet() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/NewDream.jsp").forward(request, response);
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
			// Used for testing purposes

			
 			String userid = (String) request.getSession().getAttribute("userID");
			// Current Date
			String date = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()).toString();

			String title = request.getParameter("title");
			String descr = request.getParameter("descr");
			String tags = request.getParameter("tags");
			String privacy = request.getParameter("privacy");
			
			// Make sure everything is filled out
			if (title.equals("") || descr.equals("") || tags.equals("") || privacy.equals("")){
				request.setAttribute("error", "Please fill out all fields");
				request.getRequestDispatcher("/WEB-INF/views/NewDream.jsp").forward(request, response);
				
			}else{
			
				String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
				Connection conn = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
				String query = "INSERT INTO Dreams (`userID`, `dreamTitle`, `dreamDescr`, `dreamDate`, `dreamTags`, `dreamPrivacy`) " +
				"VALUES (?,?,?,?,?,?)";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, userid);
				stmt.setString(2, title);
				stmt.setString(3, descr);
				stmt.setString(4, date);
				stmt.setString(5, tags);
				stmt.setString(6, privacy);
				stmt.executeUpdate();
				conn.close();
				// Redirect
				response.sendRedirect("/ps10-austint/MyDreamsServlet");
			}

		} 
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
	}

}
