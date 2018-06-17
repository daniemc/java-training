package co.com.s4n.training.java.exercises;

import exercise.InventoryService;
import exercise.Product;
import exercise.Stock;
import io.vavr.control.Try;
import org.junit.Test;
import static io.vavr.API.Success;

import static org.junit.Assert.*;

public class inventoryServiceTests {

    @Test
    public void createProductTest(){
        Product product = new Product("Hammer", 2500.0);

        assertTrue(product instanceof Product);
        assertEquals(product.name, "Hammer");

    }

    @Test
    public void createStockTest(){
        Product product = new Product("Hammer", 2500.0);
        Stock stock = new Stock(product, 10);

        assertTrue(product instanceof Product);
        assertTrue(stock instanceof Stock);
        assertEquals(product.name, "Hammer");
        assertEquals(stock.stock, new Integer(10));

    }

    @Test
    public void createInventory(){
        Product product = new Product("Hammer", 2500.0);
        Try<Stock> inventory = InventoryService.initInventory(product, 10)
                .flatMap(inventoryCreation -> InventoryService.in(inventoryCreation)
                    .flatMap(inventoryIn1 -> InventoryService.in(inventoryIn1)
                        .flatMap(inventoryIn2 -> InventoryService.in(inventoryIn2))));

        inventory.onSuccess(resp -> assertEquals(new Integer(13), resp.stock));
    }

    @Test
    public void createOutventory(){
        Product product = new Product("Hammer", 2500.0);
        Try<Stock> inventory = InventoryService.initInventory(product, 10)
                .flatMap(inventoryCreation -> InventoryService.out(inventoryCreation)
                        .flatMap(inventoryIn1 -> InventoryService.out(inventoryIn1)
                                .flatMap(inventoryIn2 -> InventoryService.in(inventoryIn2))));

        inventory.onSuccess(resp -> assertEquals(new Integer(9), resp.stock));
    }

    @Test
    public void zeroCantFailure(){
        Product product = new Product("Hammer", 2500.0);
        Try<Stock> inventory = InventoryService.initInventory(product, 1)
                .flatMap(inventoryCreation -> InventoryService.out(inventoryCreation)
                        .flatMap(inventoryIn1 -> InventoryService.out(inventoryIn1)));

        assertTrue(inventory.isFailure());
    }

    @Test
    public void zeroCantFailureRecover(){
        Product defaultProduct = new Product("", 0.0);
        Stock defaultStock = new Stock(defaultProduct, 0);

        Product product = new Product("Hammer", 2500.0);
        Try<Stock> inventory = InventoryService.initInventory(product, 1)
                .flatMap(inventoryCreation -> InventoryService.out(inventoryCreation)
                        .flatMap(inventoryIn1 -> InventoryService.out(inventoryIn1)
                                .recoverWith(Exception.class, Try.of(() -> defaultStock))
                                    .flatMap(inventoryIn2 -> InventoryService.in(inventoryIn2))));

        inventory.onSuccess(resp -> assertEquals(new Integer(0), resp.stock));
    }
}
