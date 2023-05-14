package com.github.alsaghir.autoirrigationsystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Plot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "cultivated_area")
    private Double cultivatedArea;
    @Column(name = "crop_type")
    private String cropType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plot current = (Plot) o;

        return getId() != null && getId().equals(current.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null)
            return getClass().hashCode();
        else
            return Objects.hash(this.getId());
    }
}
