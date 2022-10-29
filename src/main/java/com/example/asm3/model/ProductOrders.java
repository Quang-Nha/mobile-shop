package com.example.asm3.model;

public class ProductOrders {

	private int orderId;
	private int productID;
	private int amountProduct;
	private String nameProduct;

	public ProductOrders(int orderId, int productID, int amountProduct, String nameProduct) {
		super();
		this.orderId = orderId;
		this.productID = productID;
		this.amountProduct = amountProduct;
		this.nameProduct = nameProduct;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getAmountProduct() {
		return amountProduct;
	}

	public void setAmountProduct(int amountProduct) {
		this.amountProduct = amountProduct;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

}
