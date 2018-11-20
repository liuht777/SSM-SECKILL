package cn.liuht.seckill.common;

/**
 * 库存
 *
 * @author liuht
 * @date 2018/5/21 11:12
 */
public interface StockService {

    /**
     * 获取当前库存
     *
     * @return
     * @throws Exception
     */
    Integer getCurrentCount() throws Exception;
}
