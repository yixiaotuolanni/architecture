package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.BusinessService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.common.utils.JwtTokenUtil;
import edu.ynu.se.xiecheng.achitectureclass.dao.BusinessDao;
import edu.ynu.se.xiecheng.achitectureclass.dto.BusinessDTO;
import edu.ynu.se.xiecheng.achitectureclass.dto.RequestData;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "商家实体的控制器")
@RestController
@RequestMapping("/business")
public class BusinessController extends LogicController<BusinessService, BusinessDao, Business, Long> {
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    public BusinessController(@Autowired BusinessService businessService){
        super(businessService);
    }
    @ApiOperation("商家登录")
    @PostMapping("/login")
    public String login(@ApiParam("电话号码") @RequestParam String phoneNumber,
                        @ApiParam("密码") @RequestParam String password){
        return jwtTokenUtil.generateToken(getService().login(phoneNumber, password).getId());
    }

    @ApiOperation("商家通过token登录")
    @PostMapping("/findBusiness")
    public Business findBusiness(@ApiParam("token") String token){
        return getService().findBusiness(jwtTokenUtil.getUseIdFromToken(token));
    }
    @ApiOperation("商家创建店铺")
    @PostMapping("/addShop")
    public Shop addShop(@ApiParam("token")String token,
                        @ApiParam("店铺名称") String shopName,
                        @ApiParam("店铺电话") String phoneNumber,
                        @ApiParam("店铺地址") String address,
                        @ApiParam("店铺描述") String shopDescription){
        return getService().addShop(jwtTokenUtil.getUseIdFromToken(token),shopName,phoneNumber,address,shopDescription);
    }

    @ApiOperation("商家创建商品")
    @PostMapping("/addItem")
    public Item addItem(@ApiParam("token")String token,
                        @ApiParam("商品名称")String itemName,
                        @ApiParam("商品价格")Double itemPrice,
                        @ApiParam("商品描述")String itemDescription){
        return getService().addItem(jwtTokenUtil.getUseIdFromToken(token), itemName, itemPrice, itemDescription);
    }

    @ApiOperation("商家上架商品")
    @PostMapping("/addShopItem")
    public ShopItem addShopItem(@ApiParam("token")String token,
                                @ApiParam("商品号")Long itemId,
                                @ApiParam("店铺号")Long shopId){
        return getService().addShopItem(jwtTokenUtil.getUseIdFromToken(token), itemId, shopId);
    }
    @ApiOperation("商家上架一系列商品")
    @PostMapping("/addShopItems")
    public Set<ShopItem> addShopItems(@RequestBody RequestData requestData){
        String token = requestData.getToken();
        List<Long> itemsId = requestData.getItemsId();
        Long shopId = requestData.getShopId();
        return getService().addShopItems(jwtTokenUtil.getUseIdFromToken(token), itemsId, shopId);
    }
    @ApiOperation("商家查找所有订单")
    @PostMapping("/findAllOrders")
    public Set<Order> findAllOrders(@ApiParam("token")String token){
        return getService().findAllOrders(jwtTokenUtil.getUseIdFromToken(token));
    }
    @ApiOperation("商家查找店铺订单")
    @PostMapping("/findShopOrders")
    public Set<Order> findShopOrders(@ApiParam("token")String token,
                                     @ApiParam("店铺号")Long shopId){
        return getService().findShopOrders(jwtTokenUtil.getUseIdFromToken(token), shopId);
    }
    @ApiOperation("商家确认订单")
    @PostMapping("/confirm")
    public Order confirm(@ApiParam("token")String token,
                         @ApiParam("店铺号")Long shopId,
                         @ApiParam("订单号") Long orderId){
        return getService().confirm(jwtTokenUtil.getUseIdFromToken(token),shopId,orderId);
    }
    @ApiOperation("商家取消订单")
    @PostMapping("/refund")
    public Order refund(@ApiParam("token")String token,
                        @ApiParam("店铺号")Long shopId,
                        @ApiParam("订单号") Long orderId){
        return getService().refund(jwtTokenUtil.getUseIdFromToken(token), shopId, orderId);
    }
}
