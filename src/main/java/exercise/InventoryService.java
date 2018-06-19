package exercise;

import io.vavr.control.Try;

public class InventoryService {

    public static Try<Product> createProduct(String name, Double price){
        Product product = new Product(name, price);
        return Try.of(() -> product);
    }

    public static Try<Stock> initInventory(Product product, Integer cant){
        Stock stock = new Stock(product, cant);
        return Try.of(() -> stock);
    }

    public static Try<Stock> in(Stock stock){
        Integer newCant = stock.stock + 1;
        Stock newStock = new Stock(stock.product, newCant);
        return Try.of(() -> newStock);
    }

    public static Try<Stock> out(Stock stock){
        Integer newCant = stock.stock - 1;
        Stock newStock = new Stock(stock.product, newCant);
        return newCant < 0 ? Try.of(() -> newStock) : Try.failure(new Exception("Producto sin inventario"));
    }
}
