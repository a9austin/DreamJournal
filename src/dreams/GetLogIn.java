	package dreams;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class GetLogIn
 */
@WebServlet("/GetLogIn")
public class GetLogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLogIn() {
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
		String user_id = (String) request.getSession().getAttribute("userID");
		String user_message = "";
		if (request.getSession().getAttribute("userMessage") != null) {
			user_message = (String) request.getSession().getAttribute("userMessage");
		}
		generateOutput(request, response, user_id, user_message);
	}

	private void generateOutput(HttpServletRequest request, HttpServletResponse response, String id, String message) {
		JSONObject get_login_package = new JSONObject();
		
		try {
			get_login_package.put("userID", id);
			get_login_package.put("userMessage", message);
			
			response.setContentType("application/json");
			response.getWriter().print(get_login_package);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
