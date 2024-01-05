package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Shop addShop(String shopName){
        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setBusiness(this);
        shops.add(shop);
        return shop;
    }

    public Item addItem(){
        Item item = new Item();
        item.setBusiness(this);
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
}
