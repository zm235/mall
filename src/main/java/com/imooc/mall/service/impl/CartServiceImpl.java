package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartProductVo;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm) {
        Integer quantity = 1;
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        // Check whether product exists
        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        // Check whether product is on sale status
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        // Check whether product inventory is enough
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        // Write into the redis (hash)
        //  --> key: cart_id
        // need to transform the object into string(json)

        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        Cart cart;
        String value = (String) opsForHash.get(redisKey, String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            // the product doesn't exist -> just new one
            cart = new Cart(product.getId(), quantity, cartAddForm.getSelected());
        } else {
            // already exist -> number plus one
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(redisKey,
                String.valueOf(product.getId()),
                gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        Map<String, Object> entries = opsForHash.entries(redisKey);

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();

        Set<Integer> productIdSet = entries.keySet().stream().map(d -> Integer.valueOf(d)).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);

        // Construct a hashmap for easy access to specific product based on different productId
        Map<Integer, Product> map = new HashMap<>();
        for (Product product : productList) {
            map.put(product.getId(), product);
        }

        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(String.valueOf(entry.getValue()), Cart.class);

            Product product = map.get(productId);

            if (product != null) {
                // Construct CartProductVO
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected());
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                } else {
                    // Calculate the total price (just selected product)
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }


            }

            cartTotalQuantity += cart.getQuantity();
        }

        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = (String) opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            // the product doesn't exist -> return error
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        // already exist -> modify the quantity
        Cart cart = gson.fromJson(value, Cart.class);
        if (cartUpdateForm.getQuantity() != null
        && cartUpdateForm.getQuantity() >= 0) {
            cart.setQuantity(cartUpdateForm.getQuantity());
        }
        if (cartUpdateForm.getSelected() != null) {
            cart.setProductSelected(cartUpdateForm.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = (String) opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            // the product doesn't exist -> return error
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        // already exist -> delete
        opsForHash.delete(redisKey, String.valueOf(productId));

        return list(uid);
    }
}
