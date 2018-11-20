package cn.liuht.seckill.service.dbservice;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 17:12
 */
public interface OrderDbService {

    /**
     * 创建订单
     *
     * @param sid 库存ID
     * @return 订单ID
     * @throws Exception
     */
    int createWrongOrder(int sid) throws Exception;


    /**
     * 创建订单 乐观锁
     *
     * @param sid
     * @return
     * @throws Exception
     */
    int createOptimisticOrder(int sid) throws Exception;


    /**
     * 创建订单 乐观锁，库存查 Redis 减小 DB 压力。
     *
     * @param sid
     * @return
     * @throws Exception
     */
    int createOptimisticOrderUseRedis(int sid) throws Exception;
}
