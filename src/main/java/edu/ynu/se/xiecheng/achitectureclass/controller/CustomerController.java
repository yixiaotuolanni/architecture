package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.CustomerService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.dao.CustomerDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.Set;

public class CustomerController extends LogicController<CustomerService, CustomerDao, Customer,Long> {
    @Resource
    private ModelMapper modelMapper;
    public CustomerController(CustomerService customerService) {
        super(customerService);
    }
    @ApiOperation("顾客挑选商品")
    @GetMapping("/addLineItem")
    public LineItem addLineItem(@ApiParam("顾客号") Long customerId,
                                @ApiParam("数量") Double amount,
                                @ApiParam("店铺商品号") Long shopItemId){
        return getService().addLineItem(customerId, amount, shopItemId);
    }
    @ApiOperation("顾客付款")
    @GetMapping("/pay")
    public Order pay(@ApiParam("顾客号") Long customerId,
                     @ApiParam("订单号") Long orderId){
        return getService().pay(customerId, orderId);
    }
}
