package main;

import model.Amount;
import model.Product;
import model.Sale;
import model.Client;
import model.Employee;

import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 
import java.util.ArrayList;

public class Shop {
	private Amount cash = new Amount(100);
	private ArrayList<Product> inventory;
	private int numberProducts;	
	private int numberSales;
	private ArrayList<Sale> sales;
	final private int num = 10;
	final static double TAX_RATE = 1.04;
	
	Employee employee = new Employee("Martina");
	
	public Shop() {
		inventory = new ArrayList<Product>();
		sales = new ArrayList<Sale>();
	}

	public static void main(String[] args) {
		Shop shop = new Shop();
		Employee employee = new Employee(null);

		shop.loadInventory();
		System.out.println();
		shop.initSession();

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver venta total");
			System.out.println("9) Eliminar producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;
			case 2:
				shop.addProduct();
				break;
			case 3:
				shop.addStock();
				break;
			case 4:
				shop.setExpired();
				break;
			case 5:
				shop.showInventory();
				break;
			case 6:
				shop.sale();
				break;
			case 7:
				shop.showSales();
				break;
			case 8:
				shop.showTotalSales();
				break;
			case 9:
				shop.deleteProduct();
				break;
			case 10:
				System.out.println("Saliendo...");
				exit = true;
				break;
			}
		} while (!exit);




	}

	public void initSession() {
		boolean logged = false;
		Scanner input = new Scanner(System.in);
		Employee employee = new Employee("Martina");

		do {
			System.out.print("Introduce número de empleado: ");
			int user = input.nextInt();
			input.nextLine();
			System.out.print("Introduce contraseña de empleado: ");
			String password = input.nextLine();
			
			System.out.println();
			logged = employee.login(user, password);
		} while(!logged);

	}


	/**
	 * load initial inventory to shop
	 */
	public void loadInventory() {
		try {
			File f = new File("./files/inputInventory.txt");

			BufferedReader r = new BufferedReader(new FileReader(f));

			String line;
			while ((line = r.readLine()) != null) {
				String[] parts = line.split(";");

				String name = null;
				double wholesalerPrice = 0.0;
				int stock = 0;

				for (String part : parts) {
					// Dividir cada parte en clave y valor utilizando el delimitador ":"
					String[] keyValue = part.split(":");

					String key = keyValue[0].trim();
					String value = keyValue[1].trim();

					switch (key) {
					case "Product":
						name = value;
						break;
					case "Wholesaler Price":
						wholesalerPrice = Double.parseDouble(value);
						break;
					case "Stock":
						stock = Integer.parseInt(value);
						break;
					default:
						break;
					}
				}
				if (name != null) {
					Product product = new Product(name, wholesalerPrice, true, stock);
					addProduct(product);
				}
			} 

			System.out.println("Inventario cargado desde el fichero inputInventory.txt");
			System.out.println();
			Scanner mostrarInventario = new Scanner(f);
			while (mostrarInventario.hasNextLine()) {
				String inventario = mostrarInventario.nextLine();
				System.out.println(inventario); 
			}
			r.close();


		} catch (IOException e) {
			System.out.println("Error al cargar el inventario");
			e.printStackTrace();
		}
	}

	/**
	 * show current total cash
	 */
	private void showCash() {
		System.out.println("Dinero actual: " + cash.getValue() + cash.getCurrency());
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	public void addProduct() {	
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = scanner.nextLine();
		System.out.print("Precio mayorista: ");
		double wholesalerPrice = scanner.nextDouble();
		System.out.print("Stock: ");
		int stock = scanner.nextInt();

		addProduct(new Product(name, wholesalerPrice, true, stock));
	}

	/**
	 * add stock for a specific product
	 */
	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			// ask for stock
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();
			// update stock product
			product.setStock(product.getStock() + stock);
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			product.expire();
			System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
		}
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		for (Product product : inventory) {
			if (product != null) {
				if(product.getStock() == 0) {
					product.setAvailable(false);
				}
				System.out.println(product.toString());
			}
		}
	}

	/**
	 * make a sale of products to a client
	 */
	public void sale() {
		int productCount = 0;

		ArrayList<Product> productSales = new ArrayList<Product>();

		// ask for client name
		Scanner scanner = new Scanner(System.in);
		System.out.println("Realizar venta, escribir nombre cliente");
		String nameClient = scanner.nextLine();

		Client client = new Client(nameClient);

		// sale product until input name is not 0
		double totalAmount = 0.0;
		String nameProduct = "";
		while (!nameProduct.equals("0")) {
			System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
			nameProduct = scanner.nextLine();

			if (nameProduct.equals("0")) {
				break;
			}
			Product product = findProduct(nameProduct);

			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				productAvailable = true;
				totalAmount += product.getPublicPrice();
				product.setStock(product.getStock() - 1);
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				productSales.add(product);
				productCount++;
				System.out.println("Producto añadido con éxito");

			}

			if (!productAvailable) {
				System.out.println("Producto no encontrado o sin stock");
			}
		}

		LocalDateTime date = LocalDateTime.now();

		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		String salesDate = date.format(format);
		System.out.println();
		System.out.println("Hora de venta: " + salesDate);
		Sale currentSale = new Sale(nameClient, productSales, totalAmount, date);

		System.out.println(currentSale.toString());
		addSales(currentSale);

		// show cost total
		totalAmount = totalAmount * TAX_RATE;
		cash.setValue(cash.getValue() + totalAmount); 
		System.out.println("Venta realizada con éxito, total: " + totalAmount + cash.getCurrency());
			
			boolean pay = client.pay(totalAmount);
				
				if(!pay) {
					System.out.print(cash.getCurrency());
				} else {
					System.out.print(cash.getCurrency() + " restantes.");
				}
	}

	/**
	 * show all sales
	 */
	public void showSales() {
		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale.toString());
			}
		}

		Scanner sc = new Scanner(System.in);
		System.out.println("¿Guardar la información de venta en un fichero?(S / N)");
		String option = sc.next();

		if (option.equalsIgnoreCase("S")) {
			try {
				// Obtener la fecha actual
				LocalDateTime currentDate = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateString = currentDate.format(formatter);

				String fileName = "./files/sales_" + dateString + ".txt";

				File file = new File(fileName);

				FileWriter writer = new FileWriter(file);
				PrintWriter printWriter = new PrintWriter(writer);

				int saleNumber = 1;
				for (Sale sale : sales) {
					if (sale != null) {
						printWriter.println(saleNumber + ";Client=" + sale.getClient() + ";Date=" + sale.getDate() + ";");
						String products = "";
						for (Product product : sale.getProducts()) {
							products += product.getName() + "," + product.getPublicPrice() + cash.getCurrency();
						}
						printWriter.println(saleNumber + ";Products=" + products);
						printWriter.println(saleNumber + ";Amount=" + sale.getAmount() + cash.getCurrency() + ";");
						saleNumber++;
					}
				}

				printWriter.close();

				System.out.println("La información de ventas se ha guardado correctamente en el archivo: " + fileName);
			} catch (IOException e) {
				System.out.println("Error al guardar la información de ventas en el archivo.");
				e.printStackTrace();
			}
		} else {
			System.out.println("No se ha guardado la información de ventas en un archivo.");
		}
	}


	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		inventory.add(product);
		numberProducts++;
	}


	/**
	 * add a sale to sales history
	 */
	public void addSales(Sale sale) {
		sales.add(sale);
		numberSales++;
	}

	/**
	 * check if sales history is full or not
	 */
	public boolean isSalesEmpty() {
		if (numberSales == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}
		return null;
	}

	/**
	 * shows the total sales amount 
	 */
	public void showTotalSales() {
		System.out.println("Total de ventas:");
		double total = 0;
		for (Sale sale : sales) {
			if (sale != null) {
				total+= sale.getAmount()*TAX_RATE;
			}
		}
		System.out.println(total + cash.getCurrency());
	}

	/**
	 * deletes an existing product
	 */
	public void deleteProduct() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Introduce el nombre del producto:");
		String name = scanner.nextLine();

		Product product = findProduct(name);

		boolean productAvailable = false;

		if (product != null && product.isAvailable()) {
			productAvailable = true;
			// if no more stock, set as not available to delete
			if (product.getStock() == 0) {
				product.setAvailable(false);
			}
			inventory.remove(product);
			System.out.println("Producto eliminado con éxito");
		}

		if (!productAvailable) {
			System.out.println("Producto no encontrado o sin stock");
		}
	}

}