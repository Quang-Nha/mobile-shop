package com.example.asm3.bean;

import com.example.asm3.context.DbContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    // lấy CONNECTION từ singleton DbContext
    private static final Connection CONNECTION = DbContext.getInstance().getConnection();

    /**
     * trả về {@link User} nếu mail và pass của user truyền vào giống của nó
     */
    public static User login(User user) {
        String sql = "SELECT * FROM account WHERE user_mail=? AND password=?";
        ResultSet resultSet = null;

        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getString("user_mail")
                        ,resultSet.getString("password")
                        ,resultSet.getInt("account_role")
                        ,resultSet.getString("user_name")
                        ,resultSet.getString("user_address")
                        ,resultSet.getString("user_phone"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * kiểm tra xem có bản ghi nào có trường user_mail = mail truyền vào không
     */
    public static boolean exists(String mail) {
        String sql = "SELECT COUNT(*) AS count From account WHERE user_mail=?";
        ResultSet rs = null;

        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            statement.setString(1, mail);

            rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
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

        return false;
    }

    /**
     * thêm 1 bản ghi User vào bảng account
     */
    public synchronized static void create(User user) {
        String sql = "INSERT INTO account() values (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = CONNECTION.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setInt(3, 1);
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getPhone());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
