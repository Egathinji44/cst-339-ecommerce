package com.nexusstore.service;

import com.nexusstore.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    
    private final ConcurrentHashMap<Long, Product> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    
    public Product save(Product product) {
        Long newId = idGenerator.getAndIncrement();
        product.setId(newId);
        productStore.put(newId, product);
        return product;
    }
    
    
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }
    
    
    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }
    
    
    public Product update(Product product) {
        if (productStore.containsKey(product.getId())) {
            productStore.put(product.getId(), product);
            return product;
        }
        return null;
    }
    
    
    public boolean deleteById(Long id) {
        return productStore.remove(id) != null;
    }
}