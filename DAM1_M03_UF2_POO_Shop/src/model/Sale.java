package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Sale {
	private Client client;
	private ArrayList<Product> products;
	private Amount amount;
	LocalDateTime date;

	public Sale(String client, ArrayList<Product> productSales, double amount, LocalDateTime date) {
		super();
		this.client = new Client(client);
		this.products = productSales;
		this.amount = new Amount(amount);
		this.date = date;
	}

	public String getClient() {
		return client.getName();
	}
	public void setClient(String client) {
		this.client.setName(client);
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public double getAmount() {
		return amount.getValue();
	}
	public void setAmount(double amount) {
		this.amount.setValue(amount);
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		String lista = "";
		for (Product product : products) {
			if (product != null) {
				lista = lista + product.toString();
			}
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		String salesDate = date.format(format);

		return "Sale [client=" + client.getName() + ", products=" + lista + "]" + "\nFecha y hora de venta: " + salesDate + "\n";
	}


}
