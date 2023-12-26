package edu.ynu.se.xiecheng.achitectureclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("1")
public class Customer extends User{
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String addr;
}
