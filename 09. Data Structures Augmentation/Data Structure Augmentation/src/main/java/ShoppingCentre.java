import java.util.*;

public class ShoppingCentre {
    private Set<Product> products = new TreeSet<>(Comparator.comparing(Product::getName)
            .thenComparing(Product::getProducer)
            .thenComparing(Product::getPrice));

    private Map<String, TreeSet<Product>> productsByName = new HashMap<>();
    private Map<String, TreeSet<Product>> productByProducer = new HashMap<>();
    private Map<String, TreeSet<Product>> productsByNameAndProducer = new HashMap<>();

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        this.products.add(product);

        //product by name
        this.productsByName.putIfAbsent(name, new TreeSet<>(Comparator.comparing(Product::getName)
                .thenComparing(Product::getProducer)
                .thenComparing(Product::getPrice)));
        this.productsByName.get(name).add(product);

        // product by producer
        this.productByProducer.putIfAbsent(producer, new TreeSet<>(Comparator.comparing(Product::getName)
                .thenComparing(Product::getProducer)
                .thenComparing(Product::getPrice)));
        this.productByProducer.get(producer).add(product);

        // product by name and producer
        String nameAndProducer = name + "-" + producer;
        this.productsByNameAndProducer.putIfAbsent(nameAndProducer, new TreeSet<>(Comparator.comparing(Product::getName)
                .thenComparing(Product::getProducer)
                .thenComparing(Product::getPrice)));
        this.productsByNameAndProducer.get(nameAndProducer).add(product);

        return "Product added" + "\n";
    }

    public String delete(String name, String producer) {
        String nameAndProducer = name + "-" + producer;
        Set<Product> products = this.productsByNameAndProducer.remove(nameAndProducer);
        if (products == null) {
            return "No products found" + "\n";
        }

        this.productsByName.remove(name);
        this.productByProducer.remove(producer);
        return  String.format("%d products deleted" + "\n", products.size());
    }

    public String delete(String producer) {
        Set<Product> products = this.productByProducer.remove(producer);
        if (products == null) {
            return "No products found" + "\n";
        }

        return String.format("%d products deleted" + "\n", products.size());
    }

    public String findProductsByName(String name) {
        Set<Product> products = this.productsByName.get(name);
        if (products == null) {
            return "No products found" + "\n";
        }

        StringBuilder sb = new StringBuilder();
        products.forEach((product) -> sb.append(product).append("\n"));

        return sb.toString();
    }

    public String findProductsByProducer(String producer) {
        Set<Product> products = this.productByProducer.get(producer);
        if (products == null) {
            return "No products found" + "\n";
        }

        StringBuilder sb = new StringBuilder();
        products.forEach((product) -> sb.append(product).append("\n"));

        return sb.toString();
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        StringBuilder sb = new StringBuilder();

        this.products
                .stream()
                .filter((product) -> product.getPrice() >= priceFrom && product.getPrice() <= priceTo)
                .forEach((product) -> sb.append(product).append("\n"));

        String result = sb.toString();
        return result.length() == 0 ? "No products found" + "\n" : result;
    }
}
