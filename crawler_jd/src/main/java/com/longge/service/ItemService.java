package com.longge.service;


import com.longge.pojo.Item;

import java.util.List;

/**
 * @author longge
 * @create 2019-10-30 上午11:38
 */
public interface ItemService {
    //保存商品
    void save(Item item);

    //根据条件查询商品
    List<Item> findAll(Item item);
}
