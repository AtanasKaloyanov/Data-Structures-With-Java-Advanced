import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShoppingCentreTest {
    private ShoppingCentre shoppingCentre = new ShoppingCentre();

    @Before
    public void setUp() {
        this.shoppingCentre = new ShoppingCentre();
        this.shoppingCentre.addProduct("A", 20, "C");
        this.shoppingCentre.addProduct("A", 10, "B");
        this.shoppingCentre.addProduct("B", 30, "A");
        this.shoppingCentre.addProduct("B", 40, "C");
        this.shoppingCentre.addProduct("A", 10, "A");
    }

    @Test
    public void testFindProductByName() {
        String actualResult = this.shoppingCentre.findProductsByName("A");
        String expectedResult = """
                {A;A;10,00}
                {A;B;10,00}
                {A;C;20,00}
                """;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteProductsByProducer() {
        String actualResult = this.shoppingCentre.delete("A");
        String expectedResult = "2 products deleted" + "\n";
        Assert.assertEquals(actualResult, expectedResult);
        Assert.assertEquals("No products found" + "\n", this.shoppingCentre.delete("D"));

        String actualFindByProductResult = this.shoppingCentre.findProductsByName("A");
        String expectedFindByProductResult = """
                {A;B;10,00}
                {A;C;20,00}
                """;

        Assert.assertEquals(actualFindByProductResult, expectedFindByProductResult);
    }


}
