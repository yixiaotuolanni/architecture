package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private Shop shop;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private Set<LineItem> lineItems = new HashSet<>();

    public Order(){
        this.isPayed = 0;
        this.isConfirmed = 0;
        this.isRefunded = 0;
        this.totalPrice = 0.00;
    }

    public LineItem addLineItem(Double amount,ShopItem shopItem){
        LineItem lineItem = null;
        for (LineItem li : lineItems){
            if (li.getShopItem().equals(shopItem)){
                lineItem = li;
                break; // 找到匹配项后终止循环
            }
        }
        if (lineItem == null){
            lineItem = new LineItem();
            lineItem.setShopItem(shopItem);
            lineItem.setOrder(this);
            this.lineItems.add(lineItem); // 将新的 LineItem 添加到列表中
        }

        lineItem.setAmount(amount);
        this.setTotalPrice();
        return lineItem;
    }

    public Double setTotalPrice(){
        this.totalPrice = 0.00;
        for (LineItem lineItem : lineItems){
            this.totalPrice += lineItem.getAmount() * lineItem.getShopItem().getItem().getPrice();
        }
        return totalPrice;
    }
}
