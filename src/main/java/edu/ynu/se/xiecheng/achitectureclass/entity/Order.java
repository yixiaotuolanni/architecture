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
    private Double total;
    @Column
    private Integer status;

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

}
