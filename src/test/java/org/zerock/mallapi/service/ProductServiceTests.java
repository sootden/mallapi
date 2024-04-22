package org.zerock.mallapi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class ProductServiceTests {
    @Autowired
    ProductService productService;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info("" + dto));
    }

    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();

        //uuid가 있어야함
        productDTO.setUploadFileNames(List.of((UUID.randomUUID().toString()+"_Test1.jpg"),(UUID.randomUUID().toString()+"_Test2.jpg")));

        productService.register(productDTO);
    }

    @Test
    public void testRead(){
        Long pno = 12L;
        ProductDTO productDTO = productService.get(pno);

        log.info(""+productDTO);
        log.info(""+productDTO.getUploadFileNames());
    }


}
