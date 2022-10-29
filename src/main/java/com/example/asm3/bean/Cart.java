package com.example.asm3.bean;

import com.example.asm3.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> cart;

    public Cart() {
        this.cart = new ArrayList<>();
    }

    public List<Product> getCart() {
        return cart;
    }

    /**
     * thêm 1 sản phẩm vào cart với số lượng number
     * nếu trong cart đã có sản phẩm id giống sản phẩm thêm vào thì cộng số lượng
     * sản phẩm đó thêm number
     * hàm contains của cart dựa trên hàm equals của Product để xem id Product thêm vào
     * có giống trong cart không
     *
     * @param product
     * @param number
     */
    public void add(Product product, int number) {
        if (cart.contains(product)) {
            int index = cart.indexOf(product);

            Product product1 = cart.get(index);

            product1.addNumber(number);
        } else {
            product.setNumber(number);
            cart.add(product);
        }

    }

    /**
     * xóa 1 sản phẩm trong list
     */
    public void remove(int id) {
        for (Product product : cart) {
            if (product.getId() == id) {
                cart.remove(product);
                return;
            }
        }
    }

    /**
     * tính tổng tiền trong list là tổng tiền của các sản phẩm
     */
    public double getAmount() {
        double sum = 0;
        for (Product product : cart) {
            sum += product.getPrice() * product.getNumber();
        }
        return sum;
    }

    /**
     * lấy ra 1 sản phẩm trong giỏ hàng giống id truyền vào
     */
    public Product getProduct(int id) {
        for (Product product : cart) {
            if (product.getId() == id) {
                return product;
            }
        }

        // nếu chưa có sp trong list thì tạo 1 list mới có sô lượng sp tạo sẵn = 0
        return new Product();
    }

    /**
     * chỉnh sửa lại sản phẩm theo số lượng và id đưa vào
     * chỉnh lại sp trong list này là chỉnh sửa theo giỏ hàng gọi đến method này
     */
    public void editCart(String id, String quantity) {

        // đổi các tham số sang int
        int idd = Integer.parseInt(id);
        int newQuantity = Integer.parseInt(quantity);

        // nếu có sản phẩm cùng id với id đưa vào thì đổi số lượng sp đó = số lượng mới
        for (Product product : cart) {
            if (idd == product.getId()) {
                product.setNumber(newQuantity);
            }
        }
    }


}
