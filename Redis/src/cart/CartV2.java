package cart;


import helper.JedisHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.List;
import java.util.Set;

public class CartV2 {
    private Jedis jedis;
    private JSONObject cartInfo;
    private String userNo;
    private static final String KEY_CART_LIST = ":cart:product";
    private static final String KEY_CART_PRODUCT = ":cart:productid:";
    private static final String JSON_PRODUCT_LIST = "products";
    private static final int EXPIRE = 60 * 60 * 24 * 3;

    /**
     * 장바구니를 처리하기 위한 Cart 클래스의 생성자
     * @param jedisHelper 제디스 헬퍼 객체
     * @param userNo
     */
    public CartV2(JedisHelper jedisHelper, String userNo) {
        this.jedis = jedisHelper.getConnection();
        this.userNo = userNo;
        this.cartInfo = getCartInfo();
    }

    /**
     * 레디스에 저장된 장바구니 정보를 조회하여 JSON 객체로 변환한다
     * @return 장바구니가 저장된 JSONObject
     */
    private JSONObject getCartInfo() {
        String productInfo = this.jedis.get(this.userNo + KEY_CART_LIST);
        if (null == productInfo || "".equals(productInfo)) {
            return makeEmptyCart();
        }

        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(productInfo);
        } catch (ParseException e) {
            return makeEmptyCart();
        }
    }

    /**
     * 장바구니가 존재하지 않는 사용자를 위한 빈 장바구니 정보를 생성한다.
     * @return 빈 장바구니 정보
     */
    private JSONObject makeEmptyCart() {
        JSONObject cart = new JSONObject();
        cart.put(JSON_PRODUCT_LIST, new JSONArray());
        return cart;
    }

    /**
     * 장바구니에 저장된 상품을 삭제한다.
     * @return
     */
    public int flushCart() {
        JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);

        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < products.size(); i++) {
            pipeline.del(this.userNo + KEY_CART_PRODUCT + products.get(i));
        }

        pipeline.set(this.userNo + KEY_CART_LIST, "");
        pipeline.sync();

        return products.size();
    }

    /**
     * 장바구니에 상품을 추가한다.
     * @param productNo 장바구니에 추가할 상품번호
     * @param productName 장바구니에 추가할 상품명
     * @param quantity 상품개수
     * @return 장바구니 등록 결과
     */
    public String addProduct(String productNo, String productName, int quantity) {
        JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
        products.add(productNo);

        this.jedis.set(this.userNo + KEY_CART_LIST, this.cartInfo.toJSONString());

        JSONObject product = new JSONObject();
        product.put("productNo", productNo);
        product.put("productName", productName);
        product.put("quantity", quantity);

        String productKey = this.userNo + KEY_CART_PRODUCT + productNo;
        return this.jedis.setex(productKey, EXPIRE, product.toJSONString());
    }

    /**
     * 장바구니에 저장된 상품 정보를 삭제한다
     * @param productNo 삭제할 상품번호 목록
     * @return 삭제된 상품개수
     */
    public int deleteProduct(String[] productNo) {
        JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
        int result = productNo.length;

        Pipeline pipeline = jedis.pipelined(); // --- 1
        for (String item : productNo) {
            products.remove(item);
            pipeline.del(this.userNo + KEY_CART_PRODUCT + item);// --- 2
        }
        pipeline.set(this.userNo + KEY_CART_LIST, this.cartInfo.toJSONString());
        pipeline.sync(); // --- 3

        return result;
    }

    /**
     * 장바구니에 저장된 상품 목록을 JSONArray 형식으로 조회한다.
     * @return 조회된 상품 목록
     */
    public JSONArray getProductList() {
        boolean isChanged = false;
        JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);

        Pipeline pipeline = jedis.pipelined(); // --- 4
        for (int i = 0; i < products.size(); i++) {
            pipeline.get(this.userNo + KEY_CART_PRODUCT + products.get(i)); // --- 5
        }
        List<Object> redisResult = pipeline.syncAndReturnAll(); // --- 6

        JSONArray result = new JSONArray();
        for (Object item : redisResult) {
            if (item == null) {
                isChanged = true;
            } else {
                result.add(item);
            }
        }

        if (isChanged) {
            this.jedis.set(this.userNo + KEY_CART_LIST, this.cartInfo.toJSONString());
        }

        return result;
    }

    /**
     * keys 명령을 사용하여 장바구니에 저장된 상품을 삭제한다.
     * @return 삭제된 상품 개수
     * @Deprecated keys 명령을 사용한 잘못된 구현
     */
    @Deprecated
    public int flushCartDeprecated() {
        Set<String> keys = this.jedis.keys(this.userNo + KEY_CART_LIST + "*");

        Pipeline pipeline = jedis.pipelined();
        for (String key : keys) {
            pipeline.del(key);
        }
        pipeline.set(this.userNo + KEY_CART_LIST, "");
        pipeline.sync();

        return keys.size();
    }
}
