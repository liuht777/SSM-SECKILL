package cn.liuht.seckill.service.stock;

import cn.liuht.seckill.common.StockService;
import cn.liuht.seckill.common.constant.RedisKeysConstant;
import cn.liuht.seckill.service.dbservice.StockDbService;
import cn.liuht.seckill.service.pojo.Stock;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 17:23
 */
@Service(interfaceClass = StockService.class)
@Component
public class StockServiceImpl implements StockService {
    private Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Resource(name = "DBStockService")
    private StockDbService stockDbService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Integer getCurrentCount() {
        String remoteAddressString = RpcContext.getContext().getRemoteHostName();
        logger.info("request ={}", remoteAddressString);

        return getStockCount();
    }

    /**
     * 再 Redis 中设置库存
     *
     * @return
     */
    private Integer getStockCount() {
        String count = redisTemplate.opsForValue().get(RedisKeysConstant.STOCK_COUNT + 1);
        if (count == null) {
            Stock stock = stockDbService.getStockById(1);
            if (stock != null) {
                count = stock.getCount().toString();
                redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_COUNT + 1, stock.getCount().toString());
                redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_SALE + 1, stock.getSale().toString());
                redisTemplate.opsForValue().set(RedisKeysConstant.STOCK_VERSION + 1, stock.getVersion().toString());
            }
        }

        return StringUtils.isEmpty(count) ? 0 : Integer.parseInt(count);
    }
}
