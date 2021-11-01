package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Sol} entity.
 */
public class SolDTO implements Serializable {
    
    private Long id;

    private Double acidite;

    private String type;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAcidite() {
        return acidite;
    }

    public void setAcidite(Double acidite) {
        this.acidite = acidite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolDTO)) {
            return false;
        }

        return id != null && id.equals(((SolDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolDTO{" +
            "id=" + getId() +
            ", acidite=" + getAcidite() +
            ", type='" + getType() + "'" +
            "}";
    }
}
