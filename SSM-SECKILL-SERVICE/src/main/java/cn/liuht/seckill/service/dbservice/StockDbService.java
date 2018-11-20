package cn.liuht.seckill.service.dbservice;

import cn.liuht.seckill.service.pojo.Stock;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 17:15
 */
public interface StockDbService {

    /**
     * 获取剩余库存
     * @param id
     * @return
     */
    int getStockCount(int id) ;

    /**
     * 根据库存 ID 查询库存信息
     * @param id
     * @return
     */
    Stock getStockById(int id) ;

    /**
     * 更新库存信息
     * @param stock
     * return
     */
    int updateStockById(Stock stock);

    /**
     * 乐观锁更新库存
     * @param stock
     * @return
     */
    int updateStockByOptimistic(Stock stock);
}
