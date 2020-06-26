package com.educere.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Address extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String country;

    @Column
    private String state;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String zip;

    @Column
    private String latitude;

    @Column
    private String longitude;
}
