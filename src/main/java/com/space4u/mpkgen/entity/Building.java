package com.space4u.mpkgen.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "building")
@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "building_num")
    private int buildingNum;
    @NotNull(message = "Wprowadź nazwę budynku")
    @Size(min=1, message="Wprowadź nazwę budynku")
    private String name;
    @NotNull(message = "Wprowadź adres budynku")
    @Size(min=1, message="Wprowadź adres budynku")
    private String address;
    @NotNull(message = "Wprowadź właściciela budynku")
    @Size(min=1, message="Wprowadź właściciela budynku")
    private String owner;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Project> projects;


}
