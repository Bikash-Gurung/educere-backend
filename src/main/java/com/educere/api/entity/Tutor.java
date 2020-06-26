package com.educere.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Tutor extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @OneToOne(optional = false)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;

    @Column
    private String linkedin;

    @Column
    private String github;

    @Column
    private String twitter;

    @Column
    private String facebook;

    @Column
    private String phoneOne;

    @Column
    private String phoneTwo;

    @Column
    private String phoneThree;

    @Column
    private String website;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column
    private String db;

    @Column
    private String wall;

    @Column
    private boolean status = true;

}
