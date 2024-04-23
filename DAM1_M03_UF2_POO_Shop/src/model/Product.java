package model;

public class Product {
	private int id;
    private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts;
    
    static double EXPIRATION_RATE=0.60;
    
	public Product(String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.id = totalProducts+1;
		this.name = name;
		this.wholesalerPrice = new Amount(wholesalerPrice);
		this.publicPrice = new Amount(wholesalerPrice*2);
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPublicPrice() {
		return publicPrice.getValue();
	}

	public void setPublicPrice(double publicPrice) {
		this.publicPrice.setValue(publicPrice);
	}

	public double getWholesalerPrice() {
		return wholesalerPrice.getValue();
	}

	public void setWholesalerPrice(double wholesalerPrice) {
		this.wholesalerPrice.setValue(wholesalerPrice);
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}
	
	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice()*EXPIRATION_RATE);
	}

	@Override
	public String toString() {
		return "Product [name=" + this.name + ", publicPrice=" + this.publicPrice.getValue() + publicPrice.getCurrency() + ", wholesalerPrice=" + this.wholesalerPrice.getValue() + publicPrice.getCurrency() +
		", stock=" + this.stock + "]\n";
	}
}