package edu.ynu.se.xiecheng.achitectureclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("0")
public class Business extends User{
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String addr;

    @OneToMany(
            mappedBy = "business",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("business")
    private Set<Shop> shops = new HashSet<>();

    @OneToMany(
            mappedBy = "business",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnoreProperties("business")
    private Set<Item> items = new HashSet<>();
}
