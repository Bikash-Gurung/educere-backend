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
import java.util.Date;

@Entity
@Getter
@Setter
public class Availability extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tutor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tutor tutor;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private boolean status =true;
}
