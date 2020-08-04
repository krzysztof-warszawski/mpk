package com.space4u.mpkgen.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user")
@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "username")
    private String userName;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() { }

    public User(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String userName, String password, String firstName, String lastName, Collection<Role> roles) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
