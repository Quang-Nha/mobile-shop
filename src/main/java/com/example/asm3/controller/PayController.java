package com.example.asm3.controller;

import com.example.asm3.bean.Cart;
import com.example.asm3.bean.User;
import com.example.asm3.dao.OrderDAO;
import com.example.asm3.model.Orders;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "PayController", value = "/PayController")
public class PayController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        // lấy các session chứa thông tin giỏ hàng, người dùng
        // lấy tham số mã giảm giá discount từ form gửi từ cart.jsp
        Cart c = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("userSession");
        String discount = request.getParameter("discount");

        // nếu cart chưa có, có rồi nhưng bị xóa sản phẩm hết trong list size = 0
        //hoặc chưa có userSession là chưa đăng nhập quay lại trang cart
        //với tham số thông báo xác nhận đơn hàng thất bại
        if (c == null || user == null || c.getCart().size() == 0) {
            response.sendRedirect(response.encodeUrl(request.getContextPath() + "/cart.jsp?action=notcomplete"));
            return;
        }

        // lấy thời gian hiện tại
        Date date = new Date();

        // tạo 1 đơn hàng gán với các thông tin lấy được ở trên
        Orders o = new Orders(1, date, user.getAddress(), user.getEmail(), discount);

        // gọi method insertOrder để gán thông tin đơn hàng vào database
        OrderDAO.insertOrder(o, c);

        // gán xong thì xóa session đơn hàng hiện tại
        session.removeAttribute("cart");

        // quay lại trang giỏ hàng
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/cart.jsp?action=complete"));
    }
}
