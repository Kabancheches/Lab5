package Controller.Commands;

import Model.Managers.CollectionManager;
import Model.Classes.Product;
import View.InputReader;

import java.util.ArrayList;
import java.util.List;

public class RemoveGreaterCommand implements Command {

    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public RemoveGreaterCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    private ArrayList<Product> productsFilteredGreaterThanPrice(float price) {
        List<Product> collectionCopy = List.copyOf(collectionManager.getCollection());
        ArrayList<Product> correctProducts = new ArrayList<>();
        for (Product product : collectionCopy) {
            if (product.getPrice() > price) {
                correctProducts.add(product);
            }
        }
        return correctProducts;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            Product product = inputReader.readProduct();
            ArrayList<Product> filteredProducts = productsFilteredGreaterThanPrice(product.getPrice());
            for (Product deletingProduct: filteredProducts) {
                collectionManager.removeById(deletingProduct.getId());
            }
            System.out.println("Удалено элементов у которых цена выше " + product.getPrice() + ": " + filteredProducts.size());
            filteredProducts.forEach(System.out::println);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String getName() {
        return "remove_greater";
    }
}