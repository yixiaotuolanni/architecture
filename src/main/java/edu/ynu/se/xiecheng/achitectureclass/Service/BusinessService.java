package edu.ynu.se.xiecheng.achitectureclass.Service;

import edu.ynu.se.xiecheng.achitectureclass.common.service.LogicService;
import edu.ynu.se.xiecheng.achitectureclass.dao.BusinessDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.ItemDao;
import edu.ynu.se.xiecheng.achitectureclass.dao.ShopDao;
import edu.ynu.se.xiecheng.achitectureclass.entity.Business;
import edu.ynu.se.xiecheng.achitectureclass.entity.Item;
import edu.ynu.se.xiecheng.achitectureclass.entity.Shop;
import edu.ynu.se.xiecheng.achitectureclass.entity.ShopItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class BusinessService extends LogicService<BusinessDao, Business, Long> {
    @Resource
    private ItemDao itemDao;
    @Resource
    private ShopDao shopDao;
    public BusinessService(@Autowired BusinessDao businessDao){
        super(businessDao);
    }

    public Shop addShop(Long businessId,String shopName, String shopDescription){
        Business business = getDAO().getReferenceById(businessId);
        Shop shop = business.addShop(shopName, shopDescription);
        getDAO().save(business);
        return shop;
    }

    public Item addItem(Long businessId,String itemName, Double itemPrice, String itemDescription){
        Business business = getDAO().getReferenceById(businessId);
        Item item = business.addItem(itemName, itemPrice, itemDescription);
        getDAO().save(business);
        return item;
    }

    public ShopItem addShopItem(Long businessId,Long itemId, Long shopId){
        Business business = getDAO().getReferenceById(businessId);
        Item item = itemDao.getReferenceById(itemId);
        Shop shop = shopDao.getReferenceById(shopId);

        ShopItem shopItem = business.addShopItem(item,shop);
        getDAO().save(business);
        return shopItem;
    }
}
