package com.longge.service.impl;

import com.longge.dao.ItemDao;
import com.longge.pojo.Item;
import com.longge.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author longge
 * @create 2019-10-30 上午11:40
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemDao dao;

    @Override
    @Transactional
    public void save(Item item) {
        dao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        Example<Item> example = Example.of(item);
        List<Item> list = dao.findAll(example);
        return list;
    }
}
