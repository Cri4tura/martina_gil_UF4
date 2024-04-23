package model;

import main.Logable;

public class Employee extends Person implements Logable{
	private int employeeId;
	private final int USER = 123;
	private final String PASSWORD= "test";
	
	public Employee(String name) {
		super(name);
		this.employeeId = USER;
	}
	

	@Override
	public boolean login(int user, String password){
		if(user == USER && password.equals(PASSWORD)){
			System.out.print("Login correcto.\n\n");
			return true;
		} else {
			System.out.print("Login incorrecto.\n\n");
			return false;
		}
	}	
}
