package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ShopItem extends LogicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"shopItems"})
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"shopItems"})
    private Shop shop;

    @OneToMany(
            mappedBy = "shopItem",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("shopItem")
    private Set<LineItem> lineItems = new HashSet<>();

}
