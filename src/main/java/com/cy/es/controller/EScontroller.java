package com.cy.es.controller;

import com.cy.es.response.RO;
import com.cy.es.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EScontroller {

    @Autowired
    private ESService esService;

    @GetMapping("/create_index")
    public RO create(){
        esService.create_index();
        return RO.success();
    }


    @GetMapping("/delete_index")
    public RO delete_index(){
        esService.delete_index();
        return RO.success();
    }

    @GetMapping("/add")
    public RO add(){
        //添加数据
        esService.add();
        return RO.success();
    }

    @GetMapping("/update")
    public RO update(){
        //添加数据
        esService.update();
        return RO.success();
    }

    @GetMapping("/delete")
    public RO delete(){
        //添加数据
        esService.delete();
        return RO.success();
    }

    @GetMapping("/search_paging")
    public RO search_paging(@RequestParam(value = "page",defaultValue = "0")int page){
        //添加数据
        esService.search_paging(page,2);
        return RO.success();
    }

    @GetMapping("/search_sort")
    public RO search_sort(){
        esService.search_sort();
        return RO.success();

    }


    @GetMapping("/search_as_your_want")
    public RO search_as_your_want(){
        esService.search_as_your_want();
        log.info("------------------------");
        esService.search_as_your_want2();
        log.info("------------------------");
        esService.search_as_your_want3();
        log.info("------------------------");
        esService.search_as_your_want4();
        log.info("------------------------");
        esService.search_as_your_want5();
        log.info("------------------------");
        esService.search_as_your_want6();
        return RO.success();

    }
}
