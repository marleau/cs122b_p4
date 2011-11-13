package Fabflix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LiveSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LiveSearch() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); // Response mime type
		PrintWriter out = response.getWriter();
		ServletContext context = getServletContext();
		HttpSession session = request.getSession();
		
		try {
			Connection dbcon = Database.openConnection();

			String arg = request.getParameter("arg");// search string
			Integer page = 1;
			Integer resultsPerPage = 100;
			
			try {
				Pattern.compile(arg);
			} catch (PatternSyntaxException exception) {
				arg = "";
			}
			String sortBy = "ORDER BY title";	
			
			String cleanArg = Database.cleanSQL(arg);//CLEAN FOR SQL
			
			// Declare our statement
			Statement statement = dbcon.createStatement();
			Statement fullStatement = dbcon.createStatement();
			String query;
			
			query = "SELECT DISTINCT m.id,title,year,director,banner_url FROM movies m WHERE title REGEXP '" + cleanArg + "' ORDER BY title;";
			
			ResultSet searchResults = statement.executeQuery(query);
			
			out.println("<ul class=\"livePopup\">");
			while (searchResults.next()) {// For each movie, DISPLAY INFORMATION
				Integer movieID;
				try {
					movieID = Integer.valueOf(searchResults.getString("id"));
				} catch (Exception e) {
					movieID = 0;
				}
				String title = searchResults.getString("title");
				Integer year = searchResults.getInt("year");
				
				out.println("<li>");
				out.println("<a href=\"MovieDetails?id=" + movieID + "\" onmouseover=\"showPopup("+movieID+")\" onmouseout=\"hidePopup("+movieID+")\">" + title + " (" + year + ")</a><br/>");
				SearchPopup.getPopup(request, response, context, movieID);
				out.println("</li>");
			}
			out.println("</ul>");
		} catch (Exception e) {}
	}

}
