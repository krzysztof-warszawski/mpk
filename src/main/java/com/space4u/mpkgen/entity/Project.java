package com.space4u.mpkgen.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "project")
@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "project_num")
    private int projectNum;
    private String tenant;
    @NotNull(message = "Wprowadź datę")
    @Size(min=1, message = "Wprowadź datę")
    private String date;
    private String floor;
    @Column(name = "short_description")
    @NotNull(message = "Wprowadź opis")
    @Size(min=1, message = "Wprowadź opis")
    private String shortDescription;
    private String mpk;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

}
