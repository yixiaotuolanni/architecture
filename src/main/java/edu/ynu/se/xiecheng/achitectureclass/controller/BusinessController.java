package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.BusinessService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.dao.BusinessDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;
@Api(tags = "商家实体的控制器")
@RestController
@RequestMapping("/business")
public class BusinessController extends LogicController<BusinessService, BusinessDao, Business, Long> {

    @Resource
    private ModelMapper modelMapper;
    public BusinessController(@Autowired BusinessService businessService){
        super(businessService);
    }

    @ApiOperation("商家创建店铺")
    @GetMapping("/addShop")
    public Shop addShop(@ApiParam("商家号")Long businessId,
                        @ApiParam("店铺名称") String shopName,
                        @ApiParam("店铺描述") String shopDescription){
        return getService().addShop(businessId,shopName,shopDescription);
    }

    @ApiOperation("商家创建商品")
    @GetMapping("/addShop")
    public Item addItem(@ApiParam("商家号")Long businessId,
                        @ApiParam("商品名称")String itemName,
                        @ApiParam("商品价格")Double itemPrice,
                        @ApiParam("商品描述")String itemDescription){
        return getService().addItem(businessId, itemName, itemPrice, itemDescription);
    }

    @ApiOperation("商家上架商品")
    @GetMapping("/addShop")
    public ShopItem addShopItem(@ApiParam("商家号")Long businessId,
                                @ApiParam("商品号")Long itemId,
                                @ApiParam("店铺号")Long shopId){
        return getService().addShopItem(businessId, itemId, shopId);
    }
    @ApiOperation("商家查找所有订单")
    @GetMapping("/findAllOrders")
    public Set<Order> findAllOrders(@ApiParam("商家号")Long businessId){
        return getService().findAllOrders(businessId);
    }
    @ApiOperation("商家查找店铺订单")
    @GetMapping("/findShopOrders")
    public Set<Order> findShopOrders(@ApiParam("商家号")Long businessId,
                                     @ApiParam("店铺号")Long shopId){
        return getService().findShopOrders(businessId, shopId);
    }
    @ApiOperation("商家确认订单")
    @GetMapping("/confirm")
    public Order confirm(@ApiParam("商家号")Long businessId,
                         @ApiParam("店铺号")Long shopId,
                         @ApiParam("订单号") Long orderId){
        return getService().confirm(businessId,shopId,orderId);
    }
    @ApiOperation("商家取消订单")
    @GetMapping("/refund")
    public Order refund(@ApiParam("商家号")Long businessId,
                        @ApiParam("店铺号")Long shopId,
                        @ApiParam("订单号") Long orderId){
        return getService().refund(businessId, shopId, orderId);
    }
}
