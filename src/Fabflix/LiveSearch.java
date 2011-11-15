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
		
		try {
			Connection dbcon = Database.openConnection();

			String arg = request.getParameter("arg");
			
//			try {
//				Pattern.compile(arg);
//			} catch (PatternSyntaxException exception) {
//				arg = "";
//			}
			
			String[] args = arg.split(" ");
			
//			for (String s : args) {
//				System.out.println("\t"+s);
//			}
			
			String cleanArgs = "";
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].trim();
				if (args[i].startsWith("-")) {
					cleanArgs += args[i];
				} else {
					cleanArgs += "+"+args[i];
				}
				if (i == args.length -1) {
					cleanArgs += "*";
				} else {
					cleanArgs += " ";
				}
			}
			System.out.println(cleanArgs);
			cleanArgs = Database.cleanSQL(cleanArgs);
			
//			String cleanArg = Database.cleanSQL(arg);
			
			Statement statement = dbcon.createStatement();
			String query;
			
//			query = "SELECT DISTINCT m.id,title,year,director,banner_url FROM movies m WHERE title REGEXP '" + cleanArg + "' ORDER BY title;";
			query = "SELECT * FROM ft WHERE MATCH (title) AGAINST ('"+cleanArgs+"' IN BOOLEAN MODE) ORDER BY title;";
			
			ResultSet searchResults = statement.executeQuery(query);
			
			if (searchResults.next()) {
				out.println("<ul class=\"livePopup\">");
					Integer movieID;
					try {
						movieID = Integer.valueOf(searchResults.getString("movie_id"));
					} catch (Exception e) {
						movieID = 0;
					}
					String title = searchResults.getString("title");
					Integer year = searchResults.getInt("year");
					
					out.println("<li>");
					out.println("<a href=\"MovieDetails?id=" + movieID + "\" onmouseover=\"showPopup("+movieID+")\" onmouseout=\"hidePopup("+movieID+")\">" + title + " (" + year + ")</a>");
					SearchPopup.getPopup(request, response, context, movieID);
					out.println("</li>");
					
					while (searchResults.next()) {// For each movie, DISPLAY INFORMATION
						try {
							movieID = Integer.valueOf(searchResults.getString("movie_id"));
						} catch (Exception e) {
							movieID = 0;
						}
						title = searchResults.getString("title");
						year = searchResults.getInt("year");
						
						out.println("<li>");
						out.println("<a href=\"MovieDetails?id=" + movieID + "\" onmouseover=\"showPopup("+movieID+")\" onmouseout=\"hidePopup("+movieID+")\">" + title + " (" + year + ")</a>");
						SearchPopup.getPopup(request, response, context, movieID);
						out.println("</li>");
					}
					out.println("</ul>");
				System.out.println("Has results.");
			} else {
				out.println("");
				System.out.println("No results.");
			}
			
			dbcon.close();
		} catch (Exception e) {}
	}

}
