package edu.ynu.se.xiecheng.achitectureclass.controller;

import edu.ynu.se.xiecheng.achitectureclass.Service.CustomerService;
import edu.ynu.se.xiecheng.achitectureclass.common.controller.LogicController;
import edu.ynu.se.xiecheng.achitectureclass.common.utils.JwtTokenUtil;
import edu.ynu.se.xiecheng.achitectureclass.dao.CustomerDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "顾客实体的控制器")
@RestController
@RequestMapping("/customer")
public class CustomerController extends LogicController<CustomerService, CustomerDao, Customer,Long> {
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    public CustomerController(CustomerService customerService) {
        super(customerService);
    }
    @ApiOperation("顾客登录")
    @PostMapping("/login")
    public String login(@ApiParam("电话号码")String phoneNumber,
                        @ApiParam("密码") String password){
        return jwtTokenUtil.generateToken(getService().login(phoneNumber, password).getId());
    }

    @ApiOperation("顾客通过token登录")
    @PostMapping("/findCustomer")
    public Customer findCustomer(@ApiParam("token") String token){
        return getService().findCustomer(jwtTokenUtil.getUseIdFromToken(token));
    }

    @ApiOperation("顾客挑选商品")
    @PostMapping("/addLineItem")
    public LineItem addLineItem(@ApiParam("token")String token,
                                @ApiParam("数量") Double amount,
                                @ApiParam("店铺商品号") Long shopItemId){
        return getService().addLineItem(jwtTokenUtil.getUseIdFromToken(token), amount, shopItemId);
    }
    @ApiOperation("顾客付款")
    @PostMapping("/pay")
    public Order pay(@ApiParam("token")String token,
                     @ApiParam("订单号") Long orderId){
        return getService().pay(jwtTokenUtil.getUseIdFromToken(token), orderId);
    }
}
