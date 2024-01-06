package edu.ynu.se.xiecheng.achitectureclass.Service;

import edu.ynu.se.xiecheng.achitectureclass.common.service.LogicService;
import edu.ynu.se.xiecheng.achitectureclass.dao.CustomerDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.OrderDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.ShopItemDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;

import javax.annotation.Resource;

public class CustomerService extends LogicService<CustomerDao, Customer,Long> {
    @Resource
    ShopItemDao shopItemDao;
    @Resource
    OrderDao orderDao;
    public CustomerService(CustomerDao customerDao) {
        super(customerDao);
    }

    public LineItem addLineItem(Long customerId,Double amount, Long shopItemId){
        Customer customer = getDAO().getReferenceById(customerId);
        ShopItem shopItem = shopItemDao.getReferenceById(shopItemId);
        LineItem lineItem = customer.addLineItem(amount,shopItem);
        getDAO().save(customer);
        return lineItem;
    }

    public Order pay(Long customerId,Long orderId){
        Customer customer = getDAO().getReferenceById(customerId);
        Order order = orderDao.getReferenceById(orderId);
        order = customer.pay(order);
        getDAO().save(customer);
        return order;
    }
}
