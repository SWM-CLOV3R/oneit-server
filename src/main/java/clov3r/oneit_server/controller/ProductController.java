package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/product/test")
    public BaseResponse testCreateProduct() {
        Product product = new Product();
        product.setName("productA");
        product.setOriginalPrice(40000);
        Long resultIdx = productService.createProduct(product);
        return new BaseResponse(resultIdx);
    }

}
