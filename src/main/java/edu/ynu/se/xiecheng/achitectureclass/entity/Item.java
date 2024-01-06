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
public class Item extends LogicEntity {
    @Column
    private String name;
    @Column
    private Double price;
    @Column
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"item"})
    private Business business;

    @OneToMany(
            mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("item")
    private Set<ShopItem> shopItems = new HashSet<>();
}
