package com.example.asm3.controller;

import com.example.asm3.bean.Cart;
import com.example.asm3.dao.ListProductDAO;
import com.example.asm3.model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AddToCartController", value = "/AddToCartController")
public class AddToCartController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        // các tham số nhận về từ trang gửi yêu cầu
        String idd = request.getParameter("id");
        String numberProduct = request.getParameter("quantity");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(response.encodeUrl(request.getContextPath() + "/cart.jsp"));
            return;
        }

        // khi có 1 trang đến yêu cầu thêm hoặc xóa 1 sản phẩm vào giỏ hàng
        if (action.equalsIgnoreCase("add")) {

            // nếu chưa có giỏ hàng thì tạo mới
            if (session.getAttribute("cart") == null) {
                session.setAttribute("cart", new Cart());
            }

            // lấy 1 sản phẩm từ datanase và gán vào lớp Product
            Product p = ListProductDAO.getProduct(idd);

            // lấy giỏ hàng ra từ session và thêm sản phẩm này vào list của nó
            Cart c = (Cart) session.getAttribute("cart");

            // chuyển tham số số lượng của môt sản phẩm sang int
            int number = Integer.parseInt(numberProduct);

            // thêm sản phẩm vào list với số lượng của nó
            c.add(p, number);

            // không cần gán lại giỏ hàng vào session vì c vẫn là đối tượng giỏ hàng trong session
            // c vẫn tham chiếu đến vùng nhớ trong session khi lấy từ session ra

        }
        // nếu yêu cầu là xóa thì gọi hàm xóa theo ID trong giỏ hàng
        else if (action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("deletefromcart")) {
            int id = Integer.parseInt(idd);
            Cart c = (Cart) session.getAttribute("cart");
            c.remove(id);

            // không cần gán lại giỏ hàng vào session như trường hợp add

        }

        // nếu là lệnh xóa từ trang cart thì quay lại trang đó với action = edit để nó hiển thị lại trạng thái edit
        // nếu là lệnh xóa hoặc add từ trang thông tin sản phẩm thì cũng quay lại trang đó, gửi lại param id đã nhận
        // để nó hiển thị lại sản phẩm
        if (action.equalsIgnoreCase("deletefromcart")) {
            response.sendRedirect(response.encodeURL(request.getContextPath() + "/cart.jsp?action=edit"));
        } else {
            // quay lại trang ListController với tham số action mới = info và info là id sản phẩm = idd
            response.sendRedirect(
                    response.encodeURL(request.getContextPath() + "/infoProduct.jsp?info=" + idd));
        }

    }
}
