package uz.dtc.keycloakclient.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dtc.keycloakclient.payload.Product;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    // Ma'lumotlar bazasi o'rniga oddiy List
    private final List<Product> products = new CopyOnWriteArrayList<>(List.of(new Product(1L, "Laptop"), new Product(2L, "Mouse")));

    @GetMapping
    // Ko'rish uchun ikkala rolga ham ruxsat
    @PreAuthorize("hasAnyRole('PRODUCT_VIEWER', 'PRODUCT_MANAGER')")
    public List<Product> getAllProducts() {
        return products;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUCT_VIEWER', 'PRODUCT_MANAGER')")
    public Product getProductById(@PathVariable Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    // Yaratish uchun faqat menejerga ruxsat
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    public Product createProduct(@RequestBody Product product) {
        product.setId((long) (products.size() + 1));
        products.add(product);
        return product;
    }

    @DeleteMapping("/{id}")
    // O'chirish uchun faqat menejerga ruxsat
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    public String deleteProduct(@PathVariable Long id) {
        products.removeIf(p -> p.getId().equals(id));
        return "Product with id " + id + " deleted.";
    }
}