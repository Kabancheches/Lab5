package Main;
import Controller.CommandManager;
import Controller.Commands.*;
import Model.Classes.Product;
import Model.Managers.CollectionManager;
import Model.Managers.FileManager;
import View.ConsoleUI;
import View.InputReader;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        InputReader inputReader = new InputReader();
        CollectionManager collectionManager = new CollectionManager(new PriorityQueue<>());
        FileManager fileManager = null;

        ui.printSeparator();
        System.out.println("Программа управления коллекцией продуктов");
        ui.printSeparator();

        String fileName = null;
        if (args.length >= 1) {
            fileName = args[0];
            ui.printInfo("Используется файл из аргументов: " + fileName);
            try {
                fileManager = new FileManager(fileName);
                PriorityQueue<Product> products = fileManager.readCollection();
                collectionManager = new CollectionManager(products);
                ui.printInfo("Введите 'help' для получения справки.");
                ui.printSuccess("Коллекция загружена из файла: " + fileName);
            } catch (IllegalArgumentException e) {
                ui.printInfo("Введите 'help' для получения справки.");
                ui.printError("Ошибка при загрузке данных из файла: " + e.getMessage());
                collectionManager = new CollectionManager();
            }
        } else {
            while (fileName == null || fileName.isBlank()) {
                try {
                    fileName = inputReader.readString("Введите путь до файла для загрузки коллекции:",false).trim();
                    if (fileName.isBlank()) {
                        ui.printError("Имя файла не может быть пустым.");
                    } else {
                        File file = new File(fileName);
                        if (file.exists()) {
                            try {
                                fileManager = new FileManager(fileName);
                                PriorityQueue<Product> products = fileManager.readCollection();
                                collectionManager = new CollectionManager(products);
                                ui.printSuccess("Коллекция загружена из файла: " + fileName);
                                ui.printInfo("Введите 'help' для получения справки.");
                            } catch (IllegalArgumentException e) {
                                ui.printError("Ошибка при загрузке данных из файла: " + e.getMessage());
                                collectionManager = new CollectionManager();
                                ui.printInfo("Введите 'help' для получения справки.");
                            }
                            break;
                        } else {
                            ui.printError("Такого файла не существует.");
                            String yesNo = null;
                            while (yesNo == null) {
                                yesNo = inputReader.readString("Хотите создать файл с этим путём? (y/n - выход)", false);
                                if (yesNo.equals("y")) {
                                    try {
                                        file.createNewFile();
                                        ui.printSuccess("Файл создан.");
                                        fileManager = new FileManager(fileName);
                                        ui.printInfo("Введите 'help' для получения справки.");
                                    } catch (IOException e) {
                                        ui.printError(e.getMessage());
                                    }
                                } else if (yesNo.equals("n")) {
                                    System.exit(1);
                                    break;
                                } else {yesNo = null;}
                            }
                        }
                    }

                } catch (IOException e) {
                    System.err.println("Ошибка: " + e);
                }
            }
        }

        CommandManager commandManager = new CommandManager();
        ExitCommand exitCommand = new ExitCommand();



        registerCommands(commandManager, collectionManager, fileManager,
                inputReader, exitCommand);

        runInteractiveMode(commandManager, inputReader, exitCommand);
    }


    private static void registerCommands(CommandManager manager,
                                         CollectionManager collectionManager,
                                         FileManager fileManager,
                                         InputReader inputReader,
                                         ExitCommand exitCommand) {
        manager.registerCommand("help", new HelpCommand(manager));
        manager.registerCommand("info", new InfoCommand(collectionManager));
        manager.registerCommand("show", new ShowCommand(collectionManager));
        manager.registerCommand("add", new AddCommand(collectionManager, inputReader));
        manager.registerCommand("update", new UpdateIdCommand(collectionManager, inputReader));
        manager.registerCommand("remove_by_id", new RemoveByIdCommand(collectionManager));
        manager.registerCommand("clear", new ClearCommand(collectionManager));
        manager.registerCommand("save", new SaveCommand(collectionManager, fileManager));
        manager.registerCommand("execute_script", new ExecuteScriptCommand(manager));
        manager.registerCommand("exit", exitCommand);
        manager.registerCommand("add_if_min", new AddIfMinCommand(collectionManager, inputReader));
        manager.registerCommand("remove_greater", new RemoveGreaterCommand(collectionManager, inputReader));
        manager.registerCommand("remove_lower", new RemoveLowerCommand(collectionManager, inputReader));
        manager.registerCommand("filter_by_price", new FilterByPriceCommand(collectionManager));
        manager.registerCommand("filter_greater_than_price", new FilterGreaterThanPriceCommand(collectionManager));
        manager.registerCommand("print_field_descending_unit_of_measure",
                new PrintFieldDescendingUnitOfMeasureCommand(collectionManager));
    }

    private static void runInteractiveMode(CommandManager commandManager, InputReader inputReader, ExitCommand exitCommand) {
        try {
            while (!exitCommand.isNeedExit()) {
                System.out.print("> ");
                String input = inputReader.readLine();

                if (input == null || input.trim().isEmpty()) {
                    continue;
                }
                commandManager.executeCommand(input.trim());
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        }
    }
}