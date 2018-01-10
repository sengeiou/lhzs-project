package cn.lhzs.data.dao;

import cn.lhzs.data.base.Mapper;
import cn.lhzs.data.bean.Shop;

import java.util.List;

public interface ShopMapper  extends Mapper<Shop> {

    void deleteTable();

    void batchInsert(List<Shop> shopList);
}