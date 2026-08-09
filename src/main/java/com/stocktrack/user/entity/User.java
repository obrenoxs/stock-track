package com.stocktrack.user.entity;

import com.stocktrack.shared.entity.BaseEntity;
import com.stocktrack.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "re")
public class User extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "re", nullable = false, unique = true, length = 30)
    private String re;

    @Column(name = "area", nullable = false, length = 100)
    private String area;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;
}
