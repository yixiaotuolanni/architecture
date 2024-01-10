package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.ShopService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.dao.ShopDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.Shop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Api(tags = "店铺实体的控制器")
@RestController
@RequestMapping("/shop")
public class ShopController extends LogicController<ShopService, ShopDao,Shop,Long> {
    public ShopController(@Autowired ShopService ls) {
        super(ls);
    }
}
