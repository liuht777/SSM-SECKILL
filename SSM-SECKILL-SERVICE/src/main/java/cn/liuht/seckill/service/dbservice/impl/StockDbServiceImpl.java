package cn.liuht.seckill.service.dbservice.impl;

import cn.liuht.seckill.service.dbservice.StockDbService;
import cn.liuht.seckill.service.mapper.StockMapper;
import cn.liuht.seckill.service.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 17:16
 */
@Service("DBStockService")
public class StockDbServiceImpl implements StockDbService {
    @Autowired
    private StockMapper stockMapper;

    @Override
    public int getStockCount(int id) {
        Stock ssmStock = stockMapper.selectByPrimaryKey(id);
        return ssmStock.getCount();
    }

    @Override
    public Stock getStockById(int id) {
        return stockMapper.selectByPrimaryKey(id) ;
    }

    @Override
    public int updateStockById(Stock stock) {
        return stockMapper.updateByPrimaryKeySelective(stock) ;
    }

    @Override
    public int updateStockByOptimistic(Stock stock) {
        return stockMapper.updateByOptimistic(stock);
    }
}
