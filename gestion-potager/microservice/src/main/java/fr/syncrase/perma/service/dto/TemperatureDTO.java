package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Temperature} entity.
 */
public class TemperatureDTO implements Serializable {

    private Long id;

    private Double min;

    private Double max;

    private String description;

    private String rusticite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRusticite() {
        return rusticite;
    }

    public void setRusticite(String rusticite) {
        this.rusticite = rusticite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemperatureDTO)) {
            return false;
        }

        TemperatureDTO temperatureDTO = (TemperatureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, temperatureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemperatureDTO{" +
            "id=" + getId() +
            ", min=" + getMin() +
            ", max=" + getMax() +
            ", description='" + getDescription() + "'" +
            ", rusticite='" + getRusticite() + "'" +
            "}";
    }
}
