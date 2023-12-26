package edu.ynu.se.xiecheng.achitectureclass.entity;


import edu.ynu.se.xiecheng.achitectureclass.common.entity.LogicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class Item extends LogicEntity {
    @Column
    private String name;
    @Column
    private Double price;

}
