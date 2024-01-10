package edu.ynu.se.xiecheng.achitectureclass.Service;

import edu.ynu.se.xiecheng.achitectureclass.common.service.LogicService;
import edu.ynu.se.xiecheng.achitectureclass.dao.ShopDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.Shop;
import org.springframework.stereotype.Service;

@Service
public class ShopService extends LogicService<ShopDao, Shop,Long> {
    public ShopService(ShopDao lr) {
        super(lr);
    }
}
