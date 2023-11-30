import java.util.*;

public class ShoppingCentre {
    private Set<Product> products = new TreeSet<>(Comparator.comparing(Product::getName)
            .thenComparing(Product::getProducer)
            .thenComparing(Product::getPrice));

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        this.products.add(product);
        return "Product added" + "\n";
    }

    public String delete(String name, String producer) {
        int oldProductsNumber = this.products.size();
        this.products.removeIf( (product) -> product.getName().equals(name)
                && product.getProducer().equals(producer));
        int difference = oldProductsNumber - this.products.size();
        return difference == 0 ? "No products found" + "\n" : String.format("%d products deleted" + "\n", difference);
    }

    public String delete(String producer) {
        int oldProductsNumber = this.products.size();
        this.products.removeIf((product) -> product.getProducer().equals(producer));
        int difference = oldProductsNumber - this.products.size();
        return difference == 0 ? "No products found" + "\n" : String.format("%d products deleted" + "\n", difference);
    }

    public String findProductsByName(String name) {
        StringBuilder sb = new StringBuilder();

        this.products
                .stream()
                .filter((product -> product.getName().equals(name)))
                .forEach((product) -> sb.append(product).append("\n"));

        String result = sb.toString();
        return result.length() == 0 ? "No products found" + "\n" : result;
    }

    public String findProductsByProducer(String producer) {
        StringBuilder sb = new StringBuilder();

        this.products
                .stream()
                .filter( (product) -> product.getProducer().equals(producer))
                .forEach((product) -> sb.append(product).append("\n"));

        String result = sb.toString();
        return result.length() == 0 ? "No products found" + "\n" : result;
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        StringBuilder sb = new StringBuilder();

        this.products
                .stream()
                .filter( (product) -> product.getPrice() >= priceFrom && product.getPrice() <= priceTo)
                .forEach( (product) -> sb.append(product).append("\n"));

        String result = sb.toString();
        return result.length() == 0 ? "No products found" + "\n" : result;
    }
}
