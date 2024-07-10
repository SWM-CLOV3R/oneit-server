package clov3r.oneit_server.controller;

import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.domain.data.Gender;
import clov3r.oneit_server.repository.ProductRepository;
import clov3r.oneit_server.repository.ProductSearch;
import clov3r.oneit_server.response.BaseResponse;
import clov3r.oneit_server.response.BaseResponseStatus;
import clov3r.oneit_server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static clov3r.oneit_server.response.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("/api/v1/product/result")
    public BaseResponse<List<Product>> extractProduct(@RequestBody ProductSearch productSearch) {

        // check gender
        if (!Gender.isValid(productSearch.getGender())) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        // check age
        // check price
        if (productSearch.getMinPrice() <0 || productSearch.getMaxPrice() < 0 || productSearch.getMinPrice() > productSearch.getMaxPrice()) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        // check keywords

        List<Product> results = productRepository.filterProducts(productSearch);

        return new BaseResponse<>(results);
    }

}
