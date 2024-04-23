package model;

import main.Payable;
import model.Amount;

public class Client extends Person implements Payable {
	private int MEMBER_ID = 456;
	private Amount BALANCE = new Amount(50);
	
	public Client(String name) {
		super(name);
	}

	@Override
	public boolean pay(double totalAmount) {
		double saldoFinal = BALANCE.getValue() - totalAmount;
		
		if(saldoFinal >= 0){
			System.out.println("El cliente " + getName() + " no debe nada.");
			System.out.print("Le quedan: " + saldoFinal);
			return true;
		} else {
			System.out.print("Cliente " + getName() + " debe: " + saldoFinal);
			return false;
		}
	}
}
