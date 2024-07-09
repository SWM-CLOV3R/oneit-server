package clov3r.oneit_server.service;

import clov3r.oneit_server.domain.Product;
import clov3r.oneit_server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(Product product) {
        System.out.println("product name = " + product.getName());
        productRepository.save(product);
        return product.getIdx();

    }

}
