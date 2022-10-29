package com.example.asm3.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogoutControl", value = "/LogoutControl")
public class LogoutControl extends HttpServlet {

    /**
     * xóa session lưu thông tin đăng nhập để thoát đăng nhập
     * rồi quay lại trang quản lý sản phẩm để nó chuyển lại trang trước khi hủy đăng nhập
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        session.removeAttribute("userSession");

        response.sendRedirect(response.encodeURL(request.getContextPath() + "/ProductController"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
