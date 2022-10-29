package com.example.asm3.controller;

import com.example.asm3.bean.Cart;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CartEditControl", value = "/CartEditControl")
public class CartEditControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        // lấy giỏ hàng ra
        Cart c = (Cart) session.getAttribute("cart");

        // nếu giỏ hàng không null thì lặp với số lần là size của list trong giỏ hàng
        // mỗi vòng lấy các tham số với tên có phần đuôi tương ứng là số lần lặp của for
        // chính là tham số được tạo gắn thêm lần lặp trong vòng lặp khi edit tại giỏ hàng
        // sẽ lấy được id và số lượng cần chỉnh sửa của sản phẩm
        // rồi gọi hàm chỉnh sửa sản phẩm có id tương ứng ở trong list giỏ hàng
        if (c != null) {
            for (int i = 0; i < c.getCart().size(); i++) {

                String id = (String) request.getParameter("id" + i);
                String quantity = (String) request.getParameter("quantity" + i);

                System.out.println("id: " + id + " quantity: " + quantity);

                // gọi method chỉnh sửa lại sp trong list của giỏ hàng này
                c.editCart(id, quantity);

            }

        }

        // không cần gán lại giỏ hàng vào session vì c vẫn là đối tượng giỏ hàng trong session
        // c vẫn tham chiếu đến vùng nhớ trong session khi lấy từ session ra

        response.sendRedirect(response.encodeURL(request.getContextPath() + "/cart.jsp"));
    }
}
