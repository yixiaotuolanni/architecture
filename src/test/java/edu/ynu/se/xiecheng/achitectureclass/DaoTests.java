package edu.ynu.se.xiecheng.achitectureclass;

import edu.ynu.se.xiecheng.achitectureclass.dao.*;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class DaoTests {
    @Resource
    BusinessDao businessDao;
    @Resource
    ShopDao shopDao;
    @Resource
    ItemDao itemDao;
    @Resource
    CustomerDao customerDao;
    @Resource
    ShopItemDao shopItemDao;
    @Resource
    OrderDao orderDao;

    @Test
    @Transactional
    public void creatBusiness(){
        Business business = new Business();
        business.setName("xgy");
        business.setPassword("123456");
        businessDao.save(business);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addItem(){
        Business business = businessDao.getReferenceById(9L);
        business.addItem("薯片",5.00,"好吃的薯片");
        businessDao.save(business);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addShop(){
        Business business = businessDao.getReferenceById(9L);
        business.addShop("小卖铺","卖各种东西");
        businessDao.save(business);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addShopItem(){
        Business business = businessDao.getReferenceById(9L);
        Shop shop = shopDao.getReferenceById(4L);
        Item item = itemDao.getReferenceById(4L);
        business.addShopItem(item,shop);
        businessDao.save(business);
    }

    @Test
    public void creatCustomer(){
        Customer customer = new Customer();
        customer.setName("xgy");
        customer.setPassword("123456");
        customerDao.save(customer);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void addLineItem(){
        Customer customer = customerDao.getReferenceById(12L);
        ShopItem shopItem  = shopItemDao.getReferenceById(2L);
        customer.addLineItem(2.00,shopItem);
        customerDao.save(customer);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void pay(){
        Customer customer = customerDao.getReferenceById(12L);
        Order order = orderDao.getReferenceById(1L);
        customer.pay(order);
        customerDao.save(customer);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void findAllOrders(){
        Business business = businessDao.getReferenceById(9L);
        Set<Order> orders = business.findAllOrders();
        System.out.println(orders.size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void findShopOrders(){
        Business business = businessDao.getReferenceById(9L);
        Shop shop = shopDao.getReferenceById(4L);
        Set<Order> orders = business.findShopOrders(shop);
        System.out.println(orders.size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void confirm(){
        Business business = businessDao.getReferenceById(9L);
        Shop shop = shopDao.getReferenceById(4L);
        Order order = orderDao.getReferenceById(1L);
        business.confirm(shop,order);
        businessDao.save(business);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void refund(){
        Business business = businessDao.getReferenceById(9L);
        Shop shop = shopDao.getReferenceById(4L);
        Order order = orderDao.getReferenceById(1L);
        business.refund(shop,order);
        businessDao.save(business);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void removeOrder(){
        Customer customer = customerDao.getReferenceById(12L);
        Order order = orderDao.getReferenceById(1L);
        orderDao.delete(order);
    }
}
