package com.example.asm3.dao;

import com.example.asm3.bean.Cart;
import com.example.asm3.context.DbContext;
import com.example.asm3.model.Orders;
import com.example.asm3.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {
    private static final Connection CONNECTION = DbContext.instance.getConnection();

    public static void insertOrder(Orders o, Cart cart) {
        // lấy ra số lượng bản ghi trong bảng gán cho biến orderId rồi +1 chính là id
        // mới sẽ thêm vào
        String sql = "SELECT COUNT(*) AS count FROM orders";
        int orderId = 0;
        // cast java.util.Date sang java.sql.Date
        java.util.Date date = o.getOrderDate();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                orderId = resultSet.getInt("count");
            }

            orderId += 1;

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // insert thông tin vào bảng orders
        sql = "INSERT INTO orders(user_mail, order_id, order_status, order_date, order_discount_code, order_address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            // set các tham số cần chèn lấy từ lớp Orders và biến tìm id mới = orderId
            statement.setString(1, o.getUserMail());
            statement.setInt(2, orderId);
            statement.setInt(3, o.getStatus());
            statement.setDate(4, sqlDate);
            statement.setString(5, o.getDiscount());
            statement.setString(6, o.getAddress());

            statement.executeUpdate();// thực hiện chèn vào bảng orders

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // insert thông tin vào bảng orders_detail
        // tạo vòng lặp để gán từng sản phẩm trong giỏ hàng 1 vào bảng với id của đơn
        // hàng bên trên
        // foreach để lấy từng sản phẩm trong list giỏi hàng rồi gán nó vào database
        for (Product product : cart.getCart()) {
            sql = "insert into orders_detail values (?, ?, ?, ?)";
            try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
                statement.setInt(1, orderId);
                statement.setInt(2, product.getId());
                statement.setInt(3, product.getNumber());
                statement.setInt(4, (int) (product.getNumber() * product.getPrice()));

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
