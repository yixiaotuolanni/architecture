package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.BusinessService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.dao.BusinessDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.Business;
import edu.ynu.se.xiecheng.achitectureclass.entity.Item;
import edu.ynu.se.xiecheng.achitectureclass.entity.Shop;
import edu.ynu.se.xiecheng.achitectureclass.entity.ShopItem;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

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
}
