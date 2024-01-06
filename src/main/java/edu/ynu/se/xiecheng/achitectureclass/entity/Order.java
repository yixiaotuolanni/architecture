package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.ynu.se.xiecheng.achitectureclass.common.entity.LogicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class Order extends LogicEntity {
    @Column
    private Double totalPrice;
    @Column(columnDefinition = "int default 0")
    private Integer isPayed;
    @Column(columnDefinition = "int default 0")
    private Integer isConfirmed;
    @Column(columnDefinition = "int default 0")
    private Integer isRefunded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"order"})
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"order"})
    private Shop shop;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("order")
    private Set<LineItem> lineItems = new HashSet<>();

    public Order(){
        this.isPayed = 0;
        this.isConfirmed = 0;
        this.isRefunded = 0;
        this.totalPrice = 0.00;
    }

    public LineItem addLineItem(Double amount,ShopItem shopItem){
        LineItem lineItem = new LineItem();
        lineItem.setAmount(amount);
        lineItem.setShopItem(shopItem);
        lineItem.setOrder(this);
        this.lineItems.add(lineItem);
        this.setTotalPrice(amount,shopItem);
        return lineItem;
    }

    public Double setTotalPrice(Double amount,ShopItem shopItem){
        this.totalPrice += shopItem.getItem().getPrice() * amount;
        return totalPrice;
    }
}
