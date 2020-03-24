package com.tyj.venus.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_member")
public class Member extends BaseEntity <Member> {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

}
