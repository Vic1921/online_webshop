import at.wst.online_webshop.OnlineWebshopApplication;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.services.DBFiller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OnlineWebshopApplication.class)
public class DBFillerTest {
    @Autowired
    private DBFiller dbFiller;

    @Test
    public void testReadFile() {
        String filename = "src/main/csv/products.csv";
        List<String[]> records = dbFiller.readFile(filename);

        // Add assertions to check if the records are read correctly
        // 36 items in the product csv
        assertEquals(36, records.size());
        assertEquals("Atomic Backland 78 Speed Turn Skin 78 80 Backland Expert", records.get(1)[0]);
        assertEquals("Ski", records.get(1)[1]);
        assertEquals("Atomic-Backland-78-Speed-Turn-Skin-78-80-Backland-Expert-B104408-00-422180.jpg", records.get(1)[2]);
    }

    @Test
    public void testFillProducts() {
        // You can test the fillProducts method in a similar way
        // Ensure that you have a test CSV file with data for products
        dbFiller.clearDB();
        dbFiller.fillVendors();
        dbFiller.fillProducts();

        List<Product> products = dbFiller.getProductRepository().findAll();
        assertEquals( 36, products.size());
        assertEquals("Alpina PROSHIELD WOMEN VEST K", products.get(0).getProductName());
        assertEquals("Bekleidung", products.get(0).getProductCategory());
        assertEquals("Alpina-PROSHIELD-WOMEN-VEST-K-2815121-00-242096.jpg", products.get(0).getProductImageUrl());
    }
}
