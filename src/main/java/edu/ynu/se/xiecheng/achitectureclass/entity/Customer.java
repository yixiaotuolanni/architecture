package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("1")
public class Customer extends User{
    @Column
    private String name;

    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("customer")
    private Set<Order> orders = new HashSet<>();

    public Order addOrder(Shop shop){
        Order order = new Order();
        order.setCustomer(this);
        order.setShop(shop);
        order.setIsPayed(0);
        order.setIsConfirmed(0);
        order.setTotalPrice(0.00);
        orders.add(order);
        return order;
    }

    public Set<Order> removeOrder(Order order){
        if (!orders.contains(order)){
            return null;
        }
        orders.remove(order);
        return orders;
    }
    public LineItem addLineItem(Double amount,ShopItem shopItem){
        Shop shop = shopItem.getShop();
        Order order = null;
        for (Order o: orders) {
            if (o.getShop().equals(shop) && o.getIsPayed() == 0){
                order = o;
            }
        }
        if (order == null){
            order = this.addOrder(shop);
        }
        return order.addLineItem(amount,shopItem);
    }



    public Order pay(Order order){
        if (!orders.contains(order)){
            return null;
        }
        order.setIsPayed(1);
        return order;
    }
}
