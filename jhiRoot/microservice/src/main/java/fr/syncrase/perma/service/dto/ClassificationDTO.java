package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Classification} entity.
 */
public class ClassificationDTO implements Serializable {

    private Long id;

    private RaunkierDTO raunkier;

    private CronquistDTO cronquist;

    private APGIDTO apg1;

    private APGIIDTO apg2;

    private APGIIIDTO apg3;

    private APGIVDTO apg4;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RaunkierDTO getRaunkier() {
        return raunkier;
    }

    public void setRaunkier(RaunkierDTO raunkier) {
        this.raunkier = raunkier;
    }

    public CronquistDTO getCronquist() {
        return cronquist;
    }

    public void setCronquist(CronquistDTO cronquist) {
        this.cronquist = cronquist;
    }

    public APGIDTO getApg1() {
        return apg1;
    }

    public void setApg1(APGIDTO apg1) {
        this.apg1 = apg1;
    }

    public APGIIDTO getApg2() {
        return apg2;
    }

    public void setApg2(APGIIDTO apg2) {
        this.apg2 = apg2;
    }

    public APGIIIDTO getApg3() {
        return apg3;
    }

    public void setApg3(APGIIIDTO apg3) {
        this.apg3 = apg3;
    }

    public APGIVDTO getApg4() {
        return apg4;
    }

    public void setApg4(APGIVDTO apg4) {
        this.apg4 = apg4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationDTO)) {
            return false;
        }

        ClassificationDTO classificationDTO = (ClassificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationDTO{" +
            "id=" + getId() +
            ", raunkier=" + getRaunkier() +
            ", cronquist=" + getCronquist() +
            ", apg1=" + getApg1() +
            ", apg2=" + getApg2() +
            ", apg3=" + getApg3() +
            ", apg4=" + getApg4() +
            "}";
    }
}
