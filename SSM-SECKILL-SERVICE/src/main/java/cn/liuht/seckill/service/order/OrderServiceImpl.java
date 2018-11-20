package cn.liuht.seckill.service.order;

import cn.liuht.seckill.common.OrderService;
import cn.liuht.seckill.service.dbservice.OrderDbService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * dubbo 接口, 模拟下单
 *
 * @author liuht
 * @date 2018/5/21 14:20
 */
@Service(interfaceClass = OrderService.class)
@Component
public class OrderServiceImpl implements OrderService {

    @Resource(name = "DBOrderService")
    private OrderDbService orderDbService ;

    @Override
    public int createWrongOrder(int sid) throws Exception {
        return orderDbService.createWrongOrder(sid);
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {
        return orderDbService.createOptimisticOrder(sid);
    }

    @Override
    public int createOptimisticOrderUseRedis(int sid) throws Exception {
        return orderDbService.createOptimisticOrderUseRedis(sid);
    }
}
