package com.nisum.restfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "PHONES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @Column(name = "number")
    private long number;

    @Column(name = "city_code")
    private int cityCode;

    @Column(name = "country_code")
    private int countryCode;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable=false)
    private User user;
}
