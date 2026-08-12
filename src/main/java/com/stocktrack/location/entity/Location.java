package com.stocktrack.location.entity;

import com.stocktrack.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity {

    @Column(name = "corridor", nullable = false, length = 50)
    private String corridor;

    @Column(name = "shelf", nullable = false, length = 50)
    private String shelf;

    @Column(name = "drawer", nullable = false, length = 50)
    private String drawer;
}
