package View;

import Model.Classes.*;
import Model.Enums.OrganizationType;
import Model.Enums.UnitOfMeasure;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

public class InputReader {

    private BufferedReader reader;

    public InputReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public int readInt(String prompt, Integer min, Boolean isMinInclude, Integer max, Boolean isMaxInclude) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            int value;
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод данных. Введите целое число.");
                continue;
            }

            if (min != null) {
                if (!isMinInclude) {
                    if (value <= min) {
                        System.out.println("Число должно быть больше " + min + ".");
                        continue;
                    }
                } else {
                    if (value < min) {
                        System.out.println("Число должно быть не меньше " + min + ".");
                        continue;
                    }
                }
            }

            if (max != null) {
                if (!isMaxInclude) {
                    if (value >= max) {
                        System.out.println("Число должно быть меньше " + max + ".");
                        continue;
                    }
                } else {
                    if (value > max) {
                        System.out.println("Число должно быть не больше " + max + ".");
                        continue;
                    }
                }
            }

            return value;
        }
    }

    public float readFloat(String prompt, Float min, Float max) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();
            input = input.replace(',', '.');

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            float value;
            try {
                value = Float.parseFloat(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод данных. Введите число с плавающей точкой.");
                continue;
            }

            if (min != null && value <= min) {
                System.out.println("Число должно быть больше " + min + ".");
                continue;
            }

            if (max != null && value >= max) {
                System.out.println("Число должно быть меньше " + max + ".");
                continue;
            }

            return value;
        }
    }

    public Long readLong(String prompt, Long min, Long max) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            long value;
            try {
                value = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                continue;
            }

            if (min != null && value <= min) {
                System.out.println("Число должно быть больше " + min + ".");
                continue;
            }

            if (min != null && value >= max){
                System.out.println("Число должно быть меньше " + max + ".");
            }
            return value;
        }
    }


    public String readString(String prompt, boolean nullable) throws IOException {
        if (prompt != null) {
            System.out.println(prompt);
        }
        while (true) {
            String value = readLine();
            if (value.isBlank()) {
                if (nullable) {
                    return null;
                }
                System.out.println("Строка не может быть пустой.");
                continue;
            }
            return value;
        }
    }

    public <E extends Enum<E>> E readEnum(String prompt, Class<E> enumClass) throws IOException {
        E[] enums = enumClass.getEnumConstants();

        System.out.println("Доступные значения:");
        for (int i = 0; i < enums.length; i++) {
            System.out.printf("%d) %s%n", i + 1, enums[i].name());
        }

        while (true) {
            System.out.print(prompt);
            String input = readLine().trim().toUpperCase();

            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректное значение. Выберите из списка.");
            }

            System.out.println("Если хотите значение null, нажмите enter");
            if (input.isEmpty()) {
                return null;
            }
        }
    }


    public Coordinates readCoordinates() throws IOException {
        Coordinates coordinates = new Coordinates();

        int x = readInt("Введите координату X (макс. 12): ", coordinates.getMinX(), coordinates.isMinXInclude(), coordinates.getMaxX(), coordinates.isMaxXInclude());
        float y = readFloat("Введите координату Y: ", null, null);

        coordinates.setX(x);
        coordinates.setY(y);

        return coordinates;
    }

    public Location readLocation() throws IOException {
        Location location = new Location();

        float x = readFloat("Введите координату X: ", null, null);
        Long y = readLong("Введите координату Y: ", null, null);
        int z = readInt("Введите координату Z: ", null, false,null,false);

        location.setX(x);
        location.setY(y);
        location.setZ(z);

        return location;
    }

    public Address readAddress() throws IOException {
        Address address = new Address();

        String street = readString("Введите улицу (если строка пустая, значение будет null): ", true);
        System.out.println("Введите местоположение:");
        Location town = readLocation();

        address.setStreet(street);
        address.setTown(town);

        return address;
    }

    public Organization readOrganization() throws IOException {
        Organization organization = new Organization();

        String name = readString("Введите название организации: ", false);
        String fullName = readString("Введите полное название: ", false);
        int employeesCount = readInt("Введите количество сотрудников: ", organization.getMinEmployeesCount(), organization.isMinEmployeesCountIncluded(), organization.getMaxEmployeeCount(), organization.isMaxEmployeeCountIncluded());

        OrganizationType type;
        System.out.print("Ввести тип организации? (y/n): ");
        if (readLine().trim().equalsIgnoreCase("y")) {
            type = readEnum("Выберите тип организации: ", OrganizationType.class);
        } else {
            type = null;
        }

        System.out.println("Введите адрес организации:");
        Address address = readAddress();

        organization.setName(name);
        organization.setFullName(fullName);
        organization.setEmployeesCount(employeesCount);
        organization.setType(type);
        organization.setOfficialAddress(address);

        return organization;
    }

    public Product readProduct() throws IOException {
        Product product = new Product();

        String name = readString("Введите название продукта: ", false);

        System.out.println("Введите координаты:");
        Coordinates coordinates = readCoordinates();

        float price = readFloat("Введите цену (больше 0): ", 0f, null);

        UnitOfMeasure unitOfMeasure;
        System.out.print("Ввести единицу измерения? (y/n): ");
        if (readLine().trim().equalsIgnoreCase("y")) {
            unitOfMeasure = readEnum("Выберите единицу измерения: ", UnitOfMeasure.class);
        } else {
            unitOfMeasure = null;
        }

        Organization manufacturer;
        System.out.print("Ввести производителя? (y/n): ");
        if (readLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Введите информацию о производителе:");
            manufacturer = readOrganization();
        } else {
            manufacturer = null;
        }

        product.setName(name);
        product.setCoordinates(coordinates);
        product.setPrice(price);
        product.setUnitOfMeasure(unitOfMeasure);
        product.setManufacturer(manufacturer);
        product.setCreationDate(LocalDateTime.now());

        return product;
    }
}