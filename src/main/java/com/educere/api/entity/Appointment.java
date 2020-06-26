package com.educere.api.entity;

import com.educere.api.common.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
public class Appointment extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinColumn(name = "institution_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Institution> institution;

    @ManyToMany
    @JoinColumn(name = "tutor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Tutor> tutor;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private boolean accepted;

    @Column
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String location;
}
