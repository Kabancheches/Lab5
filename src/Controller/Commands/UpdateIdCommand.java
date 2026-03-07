package Controller.Commands;

import Model.Managers.CollectionManager;
import Model.Classes.Product;
import View.InputReader;


public class UpdateIdCommand implements Command {
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public UpdateIdCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("Использование: update <id>, где <id> - это id объекта класса Product, который вы хотите обновить.");
            return false;
        }

        try {
            long id = Long.parseLong(args[1]);
            System.out.println("Обновление продукта с ID: " + id);

            Product existingProduct = collectionManager.getProductById(id);
            if (existingProduct == null) {
                System.err.println("Продукт с ID " + id + " не найден.");
                return false;
            }

            Product newProduct = inputReader.readProduct();
            newProduct.setId(id);
            newProduct.setCreationDate(existingProduct.getCreationDate());
            collectionManager.removeById(id);
            collectionManager.addProduct(newProduct);
            return true;

        } catch (NumberFormatException e) {
            System.err.println("Некорректный ID. Должно быть число.");
            return false;
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Обновить значение элемента коллекции по его id";
    }

    @Override
    public String getName() {
        return "update";
    }
}