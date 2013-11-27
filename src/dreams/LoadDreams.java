package dreams;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class LoadDreams
 */
@WebServlet("/LoadDreams")
public class LoadDreams extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadDreams() {
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
	    String search = request.getParameter("searchRequest");
		
		if (search.compareTo("search") == 0 && request.getParameter("searchTerm") != null) {
			try {
				String term = request.getParameter("searchTerm");
				ArrayList<Integer> dream_ids = new ArrayList<Integer>();
				ArrayList<String> dream_titles = new ArrayList<String>();
				
				String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
				Connection connect = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
				String query = "SELECT * FROM Dreams WHERE dreamPrivacy=0 AND dreamTitle LIKE '%" + term + "%' OR dreamTags LIKE '%" + term + "%'";
				PreparedStatement statement = connect.prepareStatement(query);
				ResultSet result = statement.executeQuery();
				
				while (result.next()) {
					dream_ids.add(Integer.parseInt(result.getString(1)));
					dream_titles.add(result.getString(3));
				}
				
				generateOutput(request, response, dream_ids, dream_titles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				ArrayList<Integer> dream_ids = new ArrayList<Integer>();
				ArrayList<String> dream_titles = new ArrayList<String>();
				
				String url = "jdbc:mysql://atr.eng.utah.edu/ps10_mieu";
				Connection connect = (Connection) DriverManager.getConnection (url, "mieu", "00660840");
				String query = "SELECT * FROM Dreams WHERE dreamPrivacy=0";
				PreparedStatement statement = connect.prepareStatement(query);
				ResultSet result = statement.executeQuery();
				
				while (result.next()) {
					dream_ids.add(Integer.parseInt(result.getString(1)));
					dream_titles.add(result.getString(3));
				}
				
				generateOutput(request, response, dream_ids, dream_titles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	private void generateOutput(HttpServletRequest request, HttpServletResponse response, ArrayList<Integer> ids, ArrayList<String> titles) {
		JSONObject dream_package = new JSONObject();
		
		try {
			JSONArray idList = new JSONArray();
			JSONArray titleList = new JSONArray();
			
			for (int id : ids) {
				idList.put(id);
			}
			for (String title : titles) {
				titleList.put(title);
			}
			
			dream_package.put("dreamIDs", idList);
			dream_package.put("dreamTitles", titleList);
			
			response.setContentType("application/json");
			response.getWriter().print(dream_package);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
