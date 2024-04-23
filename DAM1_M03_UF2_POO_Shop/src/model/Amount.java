package model;

public class Amount {
	private double value;
	private final String currency;
	
	public Amount(double value) {
		this.value = value;
		this.currency = "€"; 
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}

}
