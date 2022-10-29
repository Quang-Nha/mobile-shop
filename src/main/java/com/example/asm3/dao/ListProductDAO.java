package com.example.asm3.dao;

import com.example.asm3.context.DbContext;
import com.example.asm3.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListProductDAO {
    // tạo Connection lấy từ singleton DbContext
    private static final Connection CONNECTION = DbContext.getInstance().getConnection();

    /**
     * truy vẫn database trả về List<Product> theo từ tìm kiếm search, trang tìm kiếm()pageid
     * và số sản phẩm trong 1 trang(max)
     * truy vấn bảng products trả về những bản ghi có product_name giống với từ tìm kiếm search
     * không phân biệt hoa thường, nếu từ tìm kiếm rỗng thì trả về tất cả các bản ghi rồi add vào list
     */
    public static List<Product> search(String search) {
        // câu truy vấn lấy bản ghi có cột product_name đã chuyển về chữ thường chứa từ tìm kiếm(?)
        String sql = "SELECT * FROM products WHERE LOWER(product_name) LIKE ?";
        List<Product> list1 = new ArrayList<>();

        // nếu từ tìm kiếm rỗng thì trả về tất cả bản ghi
        if (search.trim().isEmpty()) {
            sql = "SELECT * FROM products";
        }

        ResultSet rs = null;

        // tạo PreparedStatement từ CONNECTION trong khối try để không phải gọi lệnh đóng
        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            // nếu từ tìm kiếm rỗng thì không cần set giá trị cho dấu ?, vì sql đã thay đổi thành không có dấu ?
            if (!search.trim().isEmpty()) {
                search = search.toLowerCase(Locale.ROOT);
                // không được thêm dấu ' ở 2 rìa như thế này '%..%' nếu không sẽ lỗi, lỗi đặc biệt cần bỏ qua dấu '
                statement.setString(1, '%' + search + '%');
            }

            // lấy kết quả các bản ghi vào ResultSet
            rs = statement.executeQuery();

            // lặp qua ResultSet và tạo Product theo các trường của bản ghi rồi add Product vào list kết quả
            while (rs.next()) {
                list1.add(new Product(rs.getInt(1)
                        , rs.getString(2)
                        , rs.getString(3)
                        , rs.getDouble(4)
                        , rs.getString(5)
                        , rs.getString(6)
                        , rs.getString(7)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // trả về list theo tìm kiếm
        return list1;

    }

    /**
     * trả về list sản phẩm trong trang cần tìm
     * @param max số sản phẩm trong 1 trang
     */
    public static List<Product> pagination(List<Product> list1, int pageid, int max) {
        List<Product> list2 = new ArrayList<>();

        // do chỉ số list tính từ 0 nên cần cho số trang/pageid giảm đi 1
        // trang 1 thì sản phẩm tính từ chỉ số 0 của list, trang 2 chỉ số là 1/pageid * max
        pageid -= 1;
        // cho số trang * số sản phẩm trong 1 trang để tính ra chỉ số sản phẩm bắt đầu trong list
        // nằm trong trang cần tìm
        int startIndex = pageid * max;

        // lặp từ chỉ số sản phẩm đầu tiên trong list nằm trong trang cần tìm đến + max/số sản phẩm trong 1 trang
        // là chỉ số sản phẩm cuối cùng trong list nằm trong trang cần tìm
        // gán các giá trị của list trong khoảng chỉ số này cho list chứa sản phẩm theo trang
        // là ra list cần tìm
        for (int i = startIndex; i < startIndex + max; i++) {

            // nếu chỉ số lớn bằng độ dài list tìm kiếm thì thoát nếu không sẽ lỗi chỉ số vượt quá
            if (i == list1.size()) {
                break;
            }
            list2.add(list1.get(i));
        }

        // trả về list phân trang
        return list2;
    }

    /**
     * lấy ra 1 sản phẩm theo ID
     */
    public static Product getProduct(String id) {
        String sql = "select * from products where product_id = ?";
        Product product = null;
        PreparedStatement stmt = null;
        try {
            stmt = CONNECTION.prepareStatement(sql);

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            rs.next();

            product = new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getString(5),
                    rs.getString(6), rs.getString(7));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }
}
