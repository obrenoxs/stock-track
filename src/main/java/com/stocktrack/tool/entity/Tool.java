package com.stocktrack.tool.entity;

import com.stocktrack.location.entity.Location;
import com.stocktrack.shared.entity.BaseEntity;
import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tooltype.entity.ToolType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tools")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "serialNumber")
public class Tool extends BaseEntity {

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ToolStatus status;

    @Column(name = "last_calibration_date")
    private LocalDate lastCalibrationDate;

    @Column(name = "next_calibration_date")
    private LocalDate nextCalibrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_type_id", nullable = false)
    private ToolType toolType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}
