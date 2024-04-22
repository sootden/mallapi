package org.zerock.mallapi.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    @PostMapping("/file")
    public Map<String, String> saveFile(ProductDTO productDTO){
        log.info("rgister: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames);
        log.info(uploadFileNames.toString());

        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO){
        log.info("list............." + pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){
        log.info("register: {}", productDTO);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames);
        log.info(""+uploadFileNames);

        Long pno = productService.register(productDTO);
        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name="pno") Long pno){
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public  Map<String,String> modify(@PathVariable(name="pno")Long pno, ProductDTO productDTO){
        productDTO.setPno(pno);
        ProductDTO oldProductDTO = productService.get(pno);

        //기존 업로드 파일
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        //새롭게 업로드 해야하는 파일
        List<MultipartFile> files = productDTO.getFiles();
        //새로 업로드되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);
        //화면에서 변화 없이 유지된 파일들
        List<String> uploadedFileNames = productDTO.getUploadFileNames();
        //유지된 파일들 + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이됨
        if(currentUploadFileNames != null && currentUploadFileNames.size() > 0){
            uploadedFileNames.addAll(currentUploadFileNames);
        }
        //수정 작업
        productService.modify(productDTO);

        if(oldFileNames != null && oldFileNames.size() > 0){
            //지워야하는 파일 목록 찾기
            //예전 파일들 중에서 지워져야하는 파일이륻들
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            //실제 파일 삭제
            fileUtil.deleteFile(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String,String> remove(@PathVariable("pno") Long pno){
        //삭제해야할 파일들 알아내기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFile(oldFileNames);
        return Map.of("RESULT", "SUCCESS");
    }
}
