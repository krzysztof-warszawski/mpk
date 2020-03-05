package com.space4u.mpkgen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Entity
@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int idBuilding;
    private int nrBuilding;
    private String name;
    private String address;
    private String owner;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Project> projects;
}
