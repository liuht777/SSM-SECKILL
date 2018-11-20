package cn.liuht.seckill.service.mapper;

import cn.liuht.seckill.service.pojo.Stock;

public interface StockMapper {
    /**
     * 根据id查询库存
     * @param id
     * @return
     */
    Stock selectByPrimaryKey(Integer id);

    /**
     * 更新库存
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Stock record);

    /**
     * 乐观锁更新库存
     * @param record
     * @return
     */
    int updateByOptimistic(Stock record) ;
}
