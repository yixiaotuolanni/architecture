package edu.ynu.se.xiecheng.achitectureclass.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@DiscriminatorValue("0")
public class Business extends User{
    @Column
    private String name;

    @OneToMany(
            mappedBy = "business",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("business")
    private Set<Shop> shops = new HashSet<>();

    @OneToMany(
            mappedBy = "business",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("business")
    private Set<Item> items = new HashSet<>();

    public Shop addShop(String shopName,String shopDescription){
        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setDescription(shopDescription);
        shop.setBusiness(this);
        shops.add(shop);
        return shop;
    }

    public Item addItem(String itemName, Double itemPrice, String itemDescription){
        Item item = new Item();
        item.setBusiness(this);

        item.setName(itemName);
        item.setPrice(itemPrice);
        item.setDescription(itemDescription);

        items.add(item);
        return item;
    }

    public ShopItem addShopItem(Item item,Shop shop){
        if (!(items.contains(item) && shops.contains(shop))){
            return null;
        }
        ShopItem shopItem = new ShopItem();
        shopItem.setItem(item);
        shopItem.setShop(shop);
        return shopItem;
    }

    public Set<Order> findAllOrders(){
        return shops.stream()
                .flatMap(shop -> shop.getOrders().stream())
                .collect(Collectors.toSet());
    }

    public Set<Order> findShopOrders(Shop shop){
        if (!shops.contains(shop)){
            return null;
        }
        return shop.getOrders();
    }

    public Order confirm(Shop shop,Order order){
        if (!(shops.contains(shop) && shop.getOrders().contains(order))){
            return null;
        }
        order.setIsConfirmed(1);
        return order;
    }

    public Order refund(Shop shop,Order order){
        if (!(shops.contains(shop) && shop.getOrders().contains(order))){
            return null;
        }
        order.setIsRefunded(1);
        return order;
    }
}
