package Fabflix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchPopup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchPopup() {
        super();
    }
    
    private static boolean edit = false;
    
    public static void getPopup(HttpServletRequest request, HttpServletResponse response, ServletContext context, Integer id) throws IOException {
    	response.setContentType("text/html"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		try {
			Connection dbcon = Database.openConnection();
			
			// READ movieID
			Integer movieID;
			try {
				movieID = id;
			} catch (Exception e) {
				movieID = 0;
			}

			// Declare our statement
			Statement statement = dbcon.createStatement();
			String query = "SELECT DISTINCT * FROM movies m " + "WHERE m.id ='" + movieID + "'";
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {

				String title = rs.getString("title");
				Integer year = rs.getInt("year");
				String director = rs.getString("director");
				String bannerURL = rs.getString("banner_url");
				String trailerURL = rs.getString("trailer_url");

//				session.setAttribute("title", title + " ("+year+")");
				// header
//				out.println("<html><head><title>SearchPopup - "+title+"</title><style>"+Page.readStyle(context)+"</style></head><body><div class=\"content\">");
//				out.println("<div class=\"movie-detail\">");
				out.println("<div class=\"movie-detail\"  id=\""+movieID+"\">");

				// Movie Info
//				out.println("<H1>" + title + " ("+year+")");
//				out.println("</H1>");
								
				out.println(title + " ("+year+")<br /><br />");
				
				out.println("<a href=\"" + trailerURL + "\"><img src=\"" + bannerURL + "\" width=\"200\"></a>");
				
				out.println("<div class=\"info\"><ul>");
				out.println("<li>ID</li>\n<li>"+movieID+"</li>");
				out.println("</li></ul><ul>");
		
					out.println("<li>Trailer</li>\n<li>");
					out.println("<a href=\"" + trailerURL + "\">View</a>");
					out.println("</li></ul>");
				
				out.println("<ul><li>Year</li>\n<li>");
					ListResults.listByYearLink(out, year, 0);
				out.println("</li></ul>");
				
				out.println("<ul><li>Director</li>\n<li>");
					ListResults.listByDirectorLink(out, director, 0);
				out.println("</li></ul>");

				out.println("<ul><li>Genres</li>\n<li>");
				ListResults.listGenres(out, dbcon, 0, movieID, edit);
				out.println("</li>");
				out.println("</ul>");

				out.println("<ul><li>Stars</li>\n<li>");
				ListResults.listStarsIMG(out, dbcon, 0, movieID, edit);
				out.println("</li></ul>");
				
				out.println("</div>");
			out.println("</div>");
			
			} else {
				session.setAttribute("title", "FabFlix -- Movie Not Found");
				out.println(Page.header(context, session));
				out.println("<H1>Movie Not Found</H1>");

				if (session.getAttribute("movieError") != null) {
					out.println("<p class=\"error\">" + session.getAttribute("movieError") + "</p>");
					session.removeAttribute("movieError");
				}
				if (session.getAttribute("movieSuccess") != null){
					out.println("<p class=\"success\">" + session.getAttribute("movieSuccess") + "</p>");
					session.removeAttribute("movieSuccess");
				}
			}

			// Footer
//			Page.footer(out);

			rs.close();
			statement.close();
			dbcon.close();
			
		} catch (SQLException ex) {
			out.println(Page.header(context, session));
			while (ex != null) {
				out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
			out.println("</DIV></BODY></HTML>");
		} // end catch SQLException
		catch (java.lang.Exception ex) {
			out.println(Page.header(context, session));
			out.println("<P>SQL error in doGet: " + ex.getMessage() + "<br>"
					+ ex.toString() + "</P></DIV></BODY></HTML>");
			return;
		}
//		out.close();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		ServletContext context = getServletContext();
		HttpSession session = request.getSession();

		try {
			Connection dbcon = Database.openConnection();
			
			// READ movieID
			Integer movieID;
			try {
				movieID = Integer.valueOf(request.getParameter("id"));
			} catch (Exception e) {
				movieID = 0;
			}

			// Declare our statement
			Statement statement = dbcon.createStatement();
			String query = "SELECT DISTINCT * FROM movies m " + "WHERE m.id ='" + movieID + "'";
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {

				String title = rs.getString("title");
				Integer year = rs.getInt("year");
				String director = rs.getString("director");
				String bannerURL = rs.getString("banner_url");
				String trailerURL = rs.getString("trailer_url");

				session.setAttribute("title", title + " ("+year+")");
				// header
				out.println("<html><head><title>SearchPopup - "+title+"</title><style>"+Page.readStyle(context)+"</style></head><body><div class=\"content\">");
				out.println("<div class=\"movie-detail\">");

				// Movie Info
				out.println("<H1>" + title + " ("+year+")");
				
				out.println("</H1>");
								
				out.println("<a href=\"" + trailerURL + "\"><img src=\"" + bannerURL + "\" width=\"200\"></a>");
				
				out.println("<div class=\"info\"><ul>");
				out.println("<li>ID</li>\n<li>"+movieID+"</li>");
				out.println("</li></ul><ul>");
		
					out.println("<li>Trailer</li>\n<li>");
					out.println("<a href=\"" + trailerURL + "\">View</a>");
					out.println("</li></ul>");
				
				out.println("<ul><li>Year</li>\n<li>");
					ListResults.listByYearLink(out, year, 0);
				out.println("</li></ul>");
				
				out.println("<ul><li>Director</li>\n<li>");
					ListResults.listByDirectorLink(out, director, 0);
				out.println("</li></ul>");

				out.println("<ul><li>Genres</li>\n<li>");
				ListResults.listGenres(out, dbcon, 0, movieID, edit);
				out.println("</li>");
				out.println("</ul>");

				out.println("<ul><li>Stars</li>\n<li>");
				ListResults.listStarsIMG(out, dbcon, 0, movieID, edit);
				out.println("</li></ul>");
				
				out.println("</div>");
			out.println("</div>");
			
			} else {
				session.setAttribute("title", "FabFlix -- Movie Not Found");
				out.println(Page.header(context, session));
				out.println("<H1>Movie Not Found</H1>");

				if (session.getAttribute("movieError") != null) {
					out.println("<p class=\"error\">" + session.getAttribute("movieError") + "</p>");
					session.removeAttribute("movieError");
				}
				if (session.getAttribute("movieSuccess") != null){
					out.println("<p class=\"success\">" + session.getAttribute("movieSuccess") + "</p>");
					session.removeAttribute("movieSuccess");
				}
			}

			// Footer
//			Page.footer(out);

			rs.close();
			statement.close();
			dbcon.close();
			
		} catch (SQLException ex) {
			out.println(Page.header(context, session));
			while (ex != null) {
				out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
			out.println("</DIV></BODY></HTML>");
		} // end catch SQLException
		catch (java.lang.Exception ex) {
			out.println(Page.header(context, session));
			out.println("<P>SQL error in doGet: " + ex.getMessage() + "<br>"
					+ ex.toString() + "</P></DIV></BODY></HTML>");
			return;
		}
		out.close();
	}

}
