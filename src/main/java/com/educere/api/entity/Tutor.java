package com.educere.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Tutor extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Type(type = "uuid-char")
    private UUID referenceId;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @OneToOne
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

    @OneToMany(mappedBy = "tutor")
    private Collection<Experties> experties;

}
