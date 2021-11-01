package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Classification} entity.
 */
public class ClassificationDTO implements Serializable {
    
    private Long id;


    private Long raunkierId;

    private Long cronquistId;

    private Long apg1Id;

    private Long apg2Id;

    private Long apg3Id;

    private Long apg4Id;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRaunkierId() {
        return raunkierId;
    }

    public void setRaunkierId(Long raunkierId) {
        this.raunkierId = raunkierId;
    }

    public Long getCronquistId() {
        return cronquistId;
    }

    public void setCronquistId(Long cronquistId) {
        this.cronquistId = cronquistId;
    }

    public Long getApg1Id() {
        return apg1Id;
    }

    public void setApg1Id(Long aPGIId) {
        this.apg1Id = aPGIId;
    }

    public Long getApg2Id() {
        return apg2Id;
    }

    public void setApg2Id(Long aPGIIId) {
        this.apg2Id = aPGIIId;
    }

    public Long getApg3Id() {
        return apg3Id;
    }

    public void setApg3Id(Long aPGIIIId) {
        this.apg3Id = aPGIIIId;
    }

    public Long getApg4Id() {
        return apg4Id;
    }

    public void setApg4Id(Long aPGIVId) {
        this.apg4Id = aPGIVId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationDTO)) {
            return false;
        }

        return id != null && id.equals(((ClassificationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationDTO{" +
            "id=" + getId() +
            ", raunkierId=" + getRaunkierId() +
            ", cronquistId=" + getCronquistId() +
            ", apg1Id=" + getApg1Id() +
            ", apg2Id=" + getApg2Id() +
            ", apg3Id=" + getApg3Id() +
            ", apg4Id=" + getApg4Id() +
            "}";
    }
}
