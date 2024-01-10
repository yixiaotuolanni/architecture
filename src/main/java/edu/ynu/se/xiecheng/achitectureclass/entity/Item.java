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

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class Item extends LogicEntity {
    @Column
    private String name;
    @Column
    private Double price;
    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Business business;

    @OneToMany(
            mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private Set<ShopItem> shopItems = new HashSet<>();

    public ShopItem addShopItem(ShopItem shopItem){
        shopItem.setItem(this);
        shopItems.add(shopItem);
        return shopItem;
    }
}
