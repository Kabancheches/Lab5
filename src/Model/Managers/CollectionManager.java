package Model.Managers;

import Model.Classes.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;

public class CollectionManager {
    private PriorityQueue<Product> collection;
    private ArrayList<Long> idListProduct = new ArrayList<>();
    private ArrayList<Long> idListOrganization = new ArrayList<>();
    private final LocalDateTime initializationDate;

    public CollectionManager() {
        this.collection = new PriorityQueue<>();
        this.idListProduct = new ArrayList<>();
        this.idListOrganization = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();
    }

    public CollectionManager(PriorityQueue<Product> collection) {
        this.collection = new PriorityQueue<>(collection);
        this.initializationDate = LocalDateTime.now();
        for (int i = 0; i < collection.size(); i++) {
            Product product = collection.poll();
            idListProduct.add(product.getId());
            if (product.getManufacturer() != null) {
                idListOrganization.add(product.getManufacturer().getId());
            }
        }
        idListProduct.sort(null);
        idListOrganization.sort(null);
    }

    public Long getFirstNotUsedId(ArrayList<Long> idList) {
        int currentId = 0;
        idList.sort(null);
        if (!idList.isEmpty()) {
            for (int i = 1; i < idList.size(); i++) {
                if (idList.get(currentId++) - idList.get(i) > 0) {
                    return idList.get(currentId) + 1;
                }
            }
            if (idList.get(0) == 1L) {
                return 2L;
            } else {
                return 1L;
            }
        } else {
            return 1L;
        }
    }

    public Long getFirstNotUsedIdProduct() {
        return getFirstNotUsedId(idListProduct);
    }

    public Long getFirstNotUsedIdOrganization() {
        return getFirstNotUsedId(idListOrganization);
    }

    public boolean checkIsIdUsed(ArrayList<Long> idList, Product product) {
        boolean bool = true;
        Long productId = product.getId();
        for (Long Id : idList) {
            if (Objects.equals(Id, productId)) {
                bool = false;
                break;
            }
        }
        return bool;
    }

    public boolean checkIsIdUsedProduct(Product product) {
        return checkIsIdUsed(idListProduct, product);
    }

    public boolean checkIsIdUsedOrganization(Product product) {
        return checkIsIdUsed(idListOrganization, product);
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            return false;
        }
        idListProduct.add(product.getId());
        idListProduct.sort(null);
        return collection.add(product);
    }

    public Product getProductById(Long id) {
        for (Product product : collection) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public boolean removeById(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            idListProduct.remove(product.getId());

            if (product.getManufacturer() != null) {
                idListOrganization.remove(product.getManufacturer().getId());
            }
            return collection.remove(product);
        }
        return false;
    }

    public PriorityQueue<Product> getCollection() {
        return new PriorityQueue<Product>(collection);
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public void clear() {
        collection.clear();
        idListProduct.clear();
    }

}