package clov3r.oneit_server.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductPaginationDTO {
    private Long idx;
    private String name;
    private int originalPrice;
    private int currentPrice;
    private int discountRate;
    private String thumbnailUrl;

    public ProductPaginationDTO() {

    }

}
