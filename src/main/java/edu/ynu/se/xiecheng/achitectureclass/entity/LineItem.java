package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.ynu.se.xiecheng.achitectureclass.common.entity.LogicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class LineItem extends LogicEntity {
    @Column
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private ShopItem shopItem;
}
