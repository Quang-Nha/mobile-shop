package com.example.asm3.controller;

import com.example.asm3.bean.Cart;
import com.example.asm3.dao.ListProductDAO;
import com.example.asm3.model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductController", value = "/ProductController")
public class ProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        // lấy các tham số tên trang và tìm kiếm
        String page = request.getParameter("page");
        String search = request.getParameter("search");
        // phần gắn vào đuôi link chuyển sang trang xem chi tiết sản phẩm
        String infoId = "";

        // lấy session lưu trạng thái lưu thông tin trang truy cập gần nhất trừ đăng nhập, đăng kí, logout
        // nó là chuỗi phần tử phân tách bởi dấu "," với phần tử đầu tên chỉ thị tên trang,
        // các phần tử kế tiếp là các thông tin cần để hiển thị lại trang
        // khi lấy giá trị tách các các phần tử ra tạo thành 1 mảng thông qua dấu "," nếu giá trị ko null
        // vì session có thể null
        String saveStateString = (String) session.getAttribute("saveState");
        String[] saveState = null;
        // nếu session ko null tách kết quả tạo thành mảng các phần tử thông qua dấu ","
        if (saveStateString != null) {
            saveState = saveStateString.split(",");
        }

        /* nếu action == null tức là vừa đăng xuất hoặc đăng nhập xong và phải có session lưu thông tin
            trang truy cập gần nhất trừ đăng nhập, đăng kí, logout, các trường hợp yêu cầu chuyển trang
            khác đều có gắn action nên chắc chắn action == null là sau khi đăng nhập, đăng kí, logout */
        // khi này chuyển trang theo trạng thái đã lưu lại trang truy cập gần nhất
        // đổi action lại bằng phần tử đầu tiên của mảng lưu trạng thái để xác định trang nào cần đến
        if (action == null && saveState != null) {
            action = saveState[0];

            // nếu session lưu trang có phần tử đầu tiên là home thì về trang chủ
            // đổi thông tin số trang và tìm kiếm theo giá trị của session đã lưu, giá trị này đã lấy
            // ra và tách thành mảng, cá thông tin cần lấy tính từ phần tử thứ 2
            if (action.equals("home")) {
                page = saveState[2];
                search = saveState[1];
            }

            // nếu session lưu trang có phần tử đầu tiên là info thì về thông tin chi tiết sản phẩm
            // đổi thông tin infoId được gắn vào đuôi link chuyển sang trang ìno là 1 tham số quy định
            // id sản phẩm cần xem, tham số này lấy từ phần tử thứ 2 của session lưu trạng thái
            if (action.equals("info")) {
                infoId = "?info=" + saveState[1];
            }
        }

        if (action == null || !action.equals("info") && !action.equals("cart")) {

            // lấy các tham số tên trang và tìm kiếm
//            String page = request.getParameter("page");
//            String search = request.getParameter("search");

            if (search == null) {
                search = "";// nếu không có thì gán là rỗng
            }
            if (page == null || page.trim().isEmpty()) {
                page = "1";// nếu không có thì gán là 1
            }

            int pageNum = Integer.parseInt(page);// đổi số trang sang int

            // lấy list các sản phẩm theo từ tìm kiếm, nếu tìm kiếm rỗng thì trả về tất cả bản ghi
            List<Product> list1 = ListProductDAO.search(search);

            // nếu độ dài list chứa sp cần hiển thị theo tìm kiếm % 6(mỗi trang 6 sản phẩm) != 0
            // thì số trang cần hiển thị là list.size / 6 + 1, còn chia ko dư thì số trang là list.size / 6
            int maxPage = 0;
            if (list1.size() % 6 == 0) {
                maxPage = list1.size() / 6;
            } else {
                maxPage = list1.size() / 6 + 1;
            }

            // truyền list các sản phẩm cần tìm và param số trang để lấy list các sản phẩm trong 1 trang
            List<Product> list2 = ListProductDAO.pagination(list1, pageNum, 6);

            // gửi các param trang, từ tìm kiếm hiện tại cho trang home để gán cho tìm kiếm
            // ,hiển thị màu trang và nó dùng để gửi lại từ tìm kiếm khi chuyển trang khác
            request.setAttribute("currentPage", pageNum);
            request.setAttribute("currentSearch", search);

            // gửi maxPage xem cần hiển thị bao nhiêu trang và list2 để hiển
            // thị sản phẩm của trang
            request.setAttribute("maxPage", maxPage);
            request.setAttribute("listPagination", list2);

            request.getRequestDispatcher("/home.jsp").forward(request, response);
            return;
        }

        if (action.equals("cart")) {
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        }

        if (action.equals("info")) {
            // lấy số lượng sản phẩm trong giỏ hàng ko thực hiện ở đây nữa mà thực hiện luôn ở infoProduct.jsp
            /* // lấy tham số info là id sản phẩm
            String idd = request.getParameter("info");

            // chuyển id này sang số int
            int id = Integer.parseInt(idd);

            // lấy session chứa giỏ hàng
            Cart c = (Cart) session.getAttribute("cart");

            // nếu chưa có giỏ hàng gửi tham số số lượng đã đặt hàng là 0
            if (c == null) {
                request.setAttribute("numberProduct", "0");
            } else {

                // lấy sản phẩm trong giỏ hàng, nếu chưa cho sẽ trả về sp có số lượng = 0
                Product p = c.getProduct(id);

                // đổi sl sản phẩm sang string
                String numberProduct = p.getNumber() + "";

                // gửi request chứa số lượng sp sang trang infoProduct
                request.setAttribute("numberProduct", numberProduct);
            }*/

            request.getRequestDispatcher("/infoProduct.jsp" + infoId).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
