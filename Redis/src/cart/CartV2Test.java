package cart;


import helper.JedisHelper;
import org.json.simple.JSONArray;
import org.junit.*;

import static org.junit.Assert.*;

public class CartV2Test {
    private static final String TESTUSER = "12521";
    static JedisHelper jedisHelper;
    private CartV2 cart;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedisHelper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedisHelper.destroyPool();
    }

    @Before
    public void setUp() throws Exception {
        this.cart = new CartV2(jedisHelper, TESTUSER);
        assertNotNull(this.cart);
    }

    @Test
    public void testAddProduct() {
        assertEquals("OK", this.cart.addProduct("151", "원두커피", 1));
        assertEquals("OK", this.cart.addProduct("156", "캔커피", 5));
    }

    @Test
    public void testGetProductList() {
        JSONArray products = this.cart.getProductList();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Ignore
    @Test
    public void testDeleteProduct() {
        String[] products = {"151"};
        int result = this.cart.deleteProduct(products);
        assertEquals(1, result);
    }

    @Test
    public void testFlushCart() {
        assertTrue(this.cart.flushCart() > -1);
        assertTrue(this.cart.flushCartDeprecated() > -1);
    }
}
