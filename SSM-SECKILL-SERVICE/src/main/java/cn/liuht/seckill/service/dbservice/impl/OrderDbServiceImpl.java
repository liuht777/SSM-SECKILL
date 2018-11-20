package cn.liuht.seckill.service.dbservice.impl;

import cn.liuht.seckill.common.constant.RedisKeysConstant;
import cn.liuht.seckill.common.lock.RedisDistributedLock;
import cn.liuht.seckill.service.dbservice.OrderDbService;
import cn.liuht.seckill.service.dbservice.StockDbService;
import cn.liuht.seckill.service.mapper.StockOrderMapper;
import cn.liuht.seckill.service.pojo.Stock;
import cn.liuht.seckill.service.pojo.StockOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 订单数据库操作Service
 *
 * @author liuht
 * @date 2018/5/21 17:13
 */
@Transactional(rollbackFor = Exception.class)
@Service(value = "DBOrderService")
public class OrderDbServiceImpl implements OrderDbService {
    private Logger logger = LoggerFactory.getLogger(OrderDbServiceImpl.class);

    @Resource(name = "DBStockService")
    private StockDbService stockDbService;

    @Autowired
    private StockOrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public int createWrongOrder(int sid) throws Exception{
        //校验库存
        Stock stock = checkStock(sid);
        //扣库存
        saleStock(stock);
        //创建订单
        return createOrder(stock);
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {

        //校验库存
        Stock stock = checkStock(sid);

        //乐观锁更新库存
        saleStockOptimistic(stock);

        //创建订单
        return createOrder(stock);
    }

    @Override
    public int createOptimisticOrderUseRedis(int sid) throws Exception {
        int oid = 0;
        RedisDistributedLock lock = new RedisDistributedLock(redisTemplate);
        String key = "lock.lock";
        if (lock.lock(key)) {
            try {
                logger.info(Thread.currentThread().getName() + ",获得锁");
                //检验库存，从 Redis 获取
                Stock stock = checkStockByRedis(sid);
                //乐观锁更新库存 以及更新 Redis
                saleStockOptimisticByRedis(stock);
                //创建订单
                oid = createOrder(stock);
            } finally {
                logger.info(Thread.currentThread().getName() + ",释放锁");
                lock.releaseLock(key);
            }
        }
        return oid;
    }

    private Stock checkStockByRedis(int sid) throws Exception {
        Integer count = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + sid));
        Integer sale = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_SALE + sid));
        if (count.equals(sale)){
            throw new RuntimeException("库存不足 Redis currentCount=" + sale);
        }
        Integer version = Integer.parseInt(redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_VERSION + sid));
        Stock stock = new Stock();
        stock.setId(sid);
        stock.setCount(count);
        stock.setSale(sale);
        stock.setVersion(version);
        return stock;
    }

    /**
     * 乐观锁更新数据库 还要更新 Redis
     * @param stock
     */
    private void saleStockOptimisticByRedis(Stock stock) {
        int count = stockDbService.updateStockByOptimistic(stock);
        if (count == 0){
            throw new RuntimeException("并发更新库存失败") ;
        }
        //自增
        redisTemplate.opsForValue().increment(RedisKeysConstant.STOCK_SALE + stock.getId(),1) ;
        redisTemplate.opsForValue().increment(RedisKeysConstant.STOCK_VERSION + stock.getId(),1) ;
    }

    private Stock checkStock(int sid) {
        Stock stock = stockDbService.getStockById(sid);
        if (stock == null || (stock.getSale().equals(stock.getCount()))) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    private void saleStockOptimistic(Stock stock) {
        int count = stockDbService.updateStockByOptimistic(stock);
        if (count == 0){
            throw new RuntimeException("并发更新库存失败") ;
        }
    }


    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        return orderMapper.insertSelective(order);
    }

    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        return stockDbService.updateStockById(stock);
    }
}
