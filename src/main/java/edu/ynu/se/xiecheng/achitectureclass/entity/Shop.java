package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.ynu.se.xiecheng.achitectureclass.common.entity.LogicEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class Shop extends LogicEntity {
    @Column
    private String name;
    @Column
    private Integer creditHours;
    @Column
    private String credit;
}
