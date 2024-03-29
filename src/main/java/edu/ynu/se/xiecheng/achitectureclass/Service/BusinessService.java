package edu.ynu.se.xiecheng.achitectureclass.Service;

import edu.ynu.se.xiecheng.achitectureclass.common.service.LogicService;
import edu.ynu.se.xiecheng.achitectureclass.dao.BusinessDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.ItemDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.OrderDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.ShopDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BusinessService extends LogicService<BusinessDao, Business, Long> {
    @Resource
    private ItemDao itemDao;
    @Resource
    private ShopDao shopDao;
    @Resource
    private OrderDao orderDao;

    public BusinessService(@Autowired BusinessDao businessDao) {
        super(businessDao);
    }

    public Business login(String phoneNumber, String password) {
        return getDAO().findBusinessByPhoneNumberAndPassword(phoneNumber, DigestUtils.md5DigestAsHex(password.getBytes()));
    }

    public Business findBusiness(Long businessId) {
        return getDAO().getReferenceById(businessId);
    }

    @Transactional
    public Shop addShop(Long businessId, String shopName, String phoneNumber, String address, String shopDescription) {
        Business business = getDAO().getReferenceById(businessId);
        Shop shop = business.addShop(shopName, phoneNumber, address, shopDescription);
        getDAO().save(business);
        return shop;
    }

    @Transactional
    public Item addItem(Long businessId, String itemName, Double itemPrice, String itemDescription) {
        Business business = getDAO().getReferenceById(businessId);
        Item item = business.addItem(itemName, itemPrice, itemDescription);
        getDAO().save(business);
        return item;
    }

    @Transactional
    public ShopItem addShopItem(Long businessId, Long itemId, Long shopId) {
        Business business = getDAO().getReferenceById(businessId);
        Item item = itemDao.getReferenceById(itemId);
        Shop shop = shopDao.getReferenceById(shopId);

        ShopItem shopItem = business.addShopItem(item, shop);
        getDAO().save(business);
        return shopItem;
    }

    @Transactional
    public Set<ShopItem> addShopItems(Long businessId, List<Long> itemId, Long shopId) {
        Business business = getDAO().getReferenceById(businessId);
        List<Item> items = itemDao.findAllById(itemId);
        Shop shop = shopDao.getReferenceById(shopId);

        Set<ShopItem> shopItems = business.addShopItems(items, shop);
        getDAO().save(business);
        return shopItems;
    }

    public Set<Order> findAllOrders(Long businessId) {
        Business business = getDAO().getReferenceById(businessId);
        return business.findAllOrders();
    }

    public Set<Order> findShopOrders(Long businessId, Long shopId) {
        Business business = getDAO().getReferenceById(businessId);
        Shop shop = shopDao.getReferenceById(shopId);
        return business.findShopOrders(shop);
    }

    @Transactional
    public Order confirm(Long businessId, Long shopId, Long orderId) {
        Business business = getDAO().getReferenceById(businessId);
        Shop shop = shopDao.getReferenceById(shopId);
        Order order = orderDao.getReferenceById(orderId);
        order = business.confirm(shop, order);
        getDAO().save(business);
        return order;
    }

    @Transactional
    public Order refund(Long businessId, Long shopId, Long orderId) {
        Business business = getDAO().getReferenceById(businessId);
        Shop shop = shopDao.getReferenceById(shopId);
        Order order = orderDao.getReferenceById(orderId);
        order = business.refund(shop, order);
        getDAO().save(business);
        return order;
    }
}
