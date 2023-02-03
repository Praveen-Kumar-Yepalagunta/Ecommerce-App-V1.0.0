 package web;

 
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
import model.Product;
import dao.ProductDAO;


/**
 * ControllerServlet.java
 * This servlet acts as a page controller for the application, handling all
 * requests from the user.
 */

@WebServlet("/")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDAO ProductDaoVar;
	
	public void init() {
		ProductDaoVar = new ProductDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertUser(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Product> listUser = ProductDaoVar.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("productsList.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("productsForm.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Product existingUser = ProductDaoVar.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("productsForm.jsp");
		request.setAttribute("product", existingUser);
		dispatcher.forward(request, response);

	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		Product newUser = new Product(name, description, price, quantity);
		ProductDaoVar.insertUser(newUser);
		response.sendRedirect("list");
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		Product book = new Product(id, name, description, price, quantity);
		ProductDaoVar.updateUser(book);
		response.sendRedirect("list");
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		ProductDaoVar.deleteUser(id);
		response.sendRedirect("list");

	}

}