package com.longge.dao;

import com.longge.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author longge
 * @create 2019-10-30 上午11:37
 */
public interface ItemDao extends JpaRepository<Item,Long> {
}
