package com.cy.es.service;

import com.cy.es.entity.Item;
import com.cy.es.repository.ESRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ESService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ESRepository esRepository;

    /**
     * 创建索引和创建字段映射
     */
    public void create_index() {
        elasticsearchTemplate.createIndex(Item.class);
        log.info("创建索引成功");
        elasticsearchTemplate.putMapping(Item.class);
        log.info("字段映射成功");
    }

    /**
     * 删除索引
     */
    public void delete_index() {
        //Clazz String
        elasticsearchTemplate.deleteIndex(Item.class);
//        elasticsearchTemplate.deleteIndex("item");
        log.info("删除索引成功");
    }

    /**
     * 新增
     */
    public void add() {
        Item item = esRepository.save(
                new Item(4L, "Iphone X", "手机", "小米",
                        6398.00, "http://image.baidu.com/13123.jpg")
        );
        log.info("添加数据:" + item.toString());
        //可迭代
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        esRepository.saveAll(list);
        log.info("批量添加数据");
    }


    /**
     * 修改
     */
    public void update() {
        Item item = esRepository.save(
                new Item(1L, "Iphone X", "手机", "苹果",
                        6399.00, "http://image.baidu.com/13123.jpg")
        );
    }

    /**
     * 删除
     */
    public void delete() {
        esRepository.delete(
                new Item(1L, "Iphone X", "手机", "小米",
                        6399.00, "http://image.baidu.com/13123.jpg")
        );

//        esRepository.deleteById(1L);

    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     */
    public void search_paging(int page, int size) {
        Page<Item> items = esRepository.findAll(PageRequest.of(page, size));
        items.forEach(x -> log.info(x.toString()));
    }

    /**
     * 排序查询
     */
    public void search_sort() {
        Iterable<Item> items = esRepository.findAll(Sort.by("price").descending());
        items.forEach(x -> log.info(x.toString()));
    }

    /**
     * term query
     */
    public void search_as_your_want() {
        //根据term query 再来进行分页查询 此方法默认查询第一页的前十条
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("brand", "小米")).build();
        Page<Item> pages = esRepository.search(searchQuery);
        pages.stream().sorted(Comparator.comparing(Item::getPrice)).forEach(x -> log.info(x.toString()));
    }

    /**
     * match query
     */
    public void search_as_your_want2(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "Iphone X")).build();

        Page<Item> pages = esRepository.search(query);
        pages.stream().sorted(Comparator.comparingDouble(Item::getPrice)).forEach(x->log.info(x.toString()));

    }

    /**
     * range query
     */
    public void search_as_your_want3(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.rangeQuery("price").gte(0).lte(10000)).build();

        Page<Item> pages = esRepository.search(query);
        pages.stream().sorted(Comparator.comparingDouble(Item::getPrice)).forEach(x->log.info(x.toString()));

    }

    /**
     * bool query
     */
    public void search_as_your_want4(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder boolQuery = boolQueryBuilder.filter(QueryBuilders.termQuery("brand", "小米"))
                .should(QueryBuilders.rangeQuery("price").gte(3000).lte(7000));

        Iterable<Item> items = esRepository.search(boolQuery);
        StreamSupport.stream(items.spliterator(),false)
                .sorted(Comparator.comparingDouble(Item::getPrice))
                .forEach(x->log.info(x.toString()));

    }

    /**
     *  sort query
     */
    public void search_as_your_want5(){

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("brand", "小米"))
                .withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC)).build();
        Page<Item> items = esRepository.search(searchQuery);
        items.forEach(x->log.info(x.toString()));

    }

    /**
     * fuzzy query
     */
    public void search_as_your_want6(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.fuzzyQuery("brand", "小米")).build();
        Page<Item> items = esRepository.search(searchQuery);
        items.forEach(x->log.info(x.toString()));
    }

}
