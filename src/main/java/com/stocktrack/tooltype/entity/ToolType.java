package com.stocktrack.tooltype.entity;

import com.stocktrack.category.entity.Category;
import com.stocktrack.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tool_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToolType extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "minimum_stock", nullable = false)
    private int minimumStock;

    @Column(name = "requires_calibration", nullable = false)
    private boolean requiresCalibration;

    @Column(name = "calibration_interval_months")
    private Integer calibrationIntervalMonths;

    @ManyToMany
    @JoinTable(
            name = "tool_types_categories",
            joinColumns = @JoinColumn(name = "tool_type_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}
