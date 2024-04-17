package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.service.TodoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService service;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable(name="tno") Long tno){
        return service.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        log.info("requestDTO : {}",pageRequestDTO);
        return service.list(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO){
        log.info("TodoDTO: {}",todoDTO);

        Long tno = service.register(todoDTO);
        return Map.of("TNO : {}", tno);
    }

    @PutMapping("/{tno}")
    public Map<String,String> modify(@PathVariable(name="tno") Long tno, @RequestBody TodoDTO todoDTO){
        todoDTO.setTno(tno);
        log.info("Modify: {}", tno);
        service.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }


    @DeleteMapping("/{tno}")
    public Map<String,String> delete(@PathVariable(name="tno") Long tno){
        log.info("Delete: {}", tno);
        service.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }
}
