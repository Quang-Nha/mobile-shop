package com.example.asm3.controller;

import com.example.asm3.bean.Account;
import com.example.asm3.bean.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AccountControl", value = "/AccountControl")
public class AccountControl extends HttpServlet {

    /**
     * chuyển sang trang yêu cầu theo tham số action
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // lấy tham số action
        String action = request.getParameter("action");

        // nếu action là login thì forward sang trang đăng nhập, register thì sang trang đăng kí
        // còn lại thì sang trang chủ
        if (action != null && action.equals("login")) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if (action != null && action.equals("register")) {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            response.sendRedirect(response.encodeURL(request.getContextPath() + "/home.jsp"));
        }

    }

    /**
     * xử lý đăng nhập, đăng kí
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        // lấy các tham số từ form
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatpassword");
        String username = request.getParameter("username");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        // nếu action null hoặc không là login và register thì quay lại trang login
        if (action == null || (!action.equals("login") && !action.equals("register"))) {
            response.sendRedirect(response.encodeURL(request.getContextPath() + "/login.jsp"));
            return;
        }

        // nếu action = login thì xử lý form đăng nhập
        if (action.equals("login")) {

            // tạo user từ value của các tham số trong form
            User user = new User(email, password);

            // lấy tham số xác định có lưu thông tin không
            String remember = request.getParameter("loginRememberChecked");

            // nếu tham số = 1 thì là có tích lưu
            // tạo session loginRememberChecked value = checked để checkbox lấy và lại check khi quay lại login
            // tạo session loginRemember lưu thông tin form gửi để form gán lại giá trị cho các ô khi quay lại login
            if (remember != null && remember.equals("1")) {
                session.setAttribute("loginRememberChecked", "checked");
                session.setAttribute("loginRemember", user);

            }
            //nếu tham số không = 1 tức là không tích lưu
            // tạo session loginRememberChecked value = "" để checkbox lấy và lại không check khi quay lại login
            // tạo session loginRemember không lưu thông tin form gửi để trong login không tìm thấy session
            // này và tạo các biến mặc định rỗng gán lại cho các ô của form
            else {
                session.setAttribute("loginRememberChecked", "");
                session.setAttribute("loginRemember", null);

            }

            // gọi hàm login() truyền user vào xem có trả về 1 user trong database không
            // nếu có tức trong database có bản ghi giống user thì đăng nhập thành công
            User useLogin = Account.login(user);
            if (useLogin != null) {

                // nếu đăng nhập thành công thì tạo session lưu thông tin đăng nhập
                session.setAttribute("userSession", useLogin);

                // quay lại trang quản lý sản phẩm
                response.sendRedirect(response.encodeURL(request.getContextPath() + "/ProductController"));

            }
            // không thì thông báo email, password không có trong database và quay lại login
            else {
                request.setAttribute("message", "email address or password not recognised");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        }
        // còn lại là xử lý đăng kí
        else {
            User user = new User(email, password, 0, username, address, phone);

            // lấy tham số xác định có lưu thông tin không xử lý tương tự login
            String remember = request.getParameter("registerRememberChecked");

            if (remember != null && remember.equals("1")) {
                session.setAttribute("registerRememberChecked", "checked");
                // session registerRemember để lưu thông tin đăng kí
                session.setAttribute("registerRemember", user);
            } else {
                session.setAttribute("registerRememberChecked", "");
                session.setAttribute("registerRemember", null);

            }

            // nếu pass nhắc lại không giống pass cũ thì gửi request thông báo và quay lại đăng kí
            if (!password.equals(repeatPassword)) {
                request.setAttribute("message", "Passwords do not match.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // nếu sđt hơn 10 số thì gửi request thông báo và quay lại đăng kí
            if (phone.length() > 10) {
                request.setAttribute("message", "phone number should not be more than 10");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // nếu email và pass không đúng định dạng lấy thông báo từ hàm xác thực và quay lại đăng kí
            if (!user.validate()) {
                // Password or email address has wrong format.
                request.setAttribute("message", user.getMessage());
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // nếu email đã tồn tại do khi tìm từ data base thì request thông báo email không dùng được
            if (Account.exists(email)) {
                request.setAttribute("message", "An account with this email address already exists");
                request.getRequestDispatcher("/register.jsp").forward(request, response);


            }
            // không còn lỗi nào nữa thì đăng kí thành công
            else {
                // nếu đăng kí thành công thì tạo session lưu thông tin đăng nhập
                session.setAttribute("userSession", user);

                // gọi hàm gán thông tin user mới tạo vào database
                Account.create(user);

                // quay lại trang quản lý sản phẩm
                response.sendRedirect(response.encodeURL(request.getContextPath() + "/ProductController"));

            }
        }
    }
}
