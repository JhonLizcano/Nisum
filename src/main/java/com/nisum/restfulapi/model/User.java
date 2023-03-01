package com.nisum.restfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "USERAPI")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id_user")
    private String idUser;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private Date created;

    private Date modified;

    @Column(name = "last_login")
    private Date lastLogin;

    private String token;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "user")
    private Set<Phone> phones;
}
