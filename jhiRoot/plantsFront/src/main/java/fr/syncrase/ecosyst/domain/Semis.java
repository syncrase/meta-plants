package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Semis.
 */
@Table("semis")
public class Semis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private PeriodeAnnee semisPleineTerre;

    @Transient
    private PeriodeAnnee semisSousAbris;

    @Transient
    private TypeSemis typeSemis;

    @Transient
    private Germination germination;

    @Column("semis_pleine_terre_id")
    private Long semisPleineTerreId;

    @Column("semis_sous_abris_id")
    private Long semisSousAbrisId;

    @Column("type_semis_id")
    private Long typeSemisId;

    @Column("germination_id")
    private Long germinationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semis id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeriodeAnnee getSemisPleineTerre() {
        return this.semisPleineTerre;
    }

    public void setSemisPleineTerre(PeriodeAnnee periodeAnnee) {
        this.semisPleineTerre = periodeAnnee;
        this.semisPleineTerreId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public Semis semisPleineTerre(PeriodeAnnee periodeAnnee) {
        this.setSemisPleineTerre(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getSemisSousAbris() {
        return this.semisSousAbris;
    }

    public void setSemisSousAbris(PeriodeAnnee periodeAnnee) {
        this.semisSousAbris = periodeAnnee;
        this.semisSousAbrisId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public Semis semisSousAbris(PeriodeAnnee periodeAnnee) {
        this.setSemisSousAbris(periodeAnnee);
        return this;
    }

    public TypeSemis getTypeSemis() {
        return this.typeSemis;
    }

    public void setTypeSemis(TypeSemis typeSemis) {
        this.typeSemis = typeSemis;
        this.typeSemisId = typeSemis != null ? typeSemis.getId() : null;
    }

    public Semis typeSemis(TypeSemis typeSemis) {
        this.setTypeSemis(typeSemis);
        return this;
    }

    public Germination getGermination() {
        return this.germination;
    }

    public void setGermination(Germination germination) {
        this.germination = germination;
        this.germinationId = germination != null ? germination.getId() : null;
    }

    public Semis germination(Germination germination) {
        this.setGermination(germination);
        return this;
    }

    public Long getSemisPleineTerreId() {
        return this.semisPleineTerreId;
    }

    public void setSemisPleineTerreId(Long periodeAnnee) {
        this.semisPleineTerreId = periodeAnnee;
    }

    public Long getSemisSousAbrisId() {
        return this.semisSousAbrisId;
    }

    public void setSemisSousAbrisId(Long periodeAnnee) {
        this.semisSousAbrisId = periodeAnnee;
    }

    public Long getTypeSemisId() {
        return this.typeSemisId;
    }

    public void setTypeSemisId(Long typeSemis) {
        this.typeSemisId = typeSemis;
    }

    public Long getGerminationId() {
        return this.germinationId;
    }

    public void setGerminationId(Long germination) {
        this.germinationId = germination;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semis)) {
            return false;
        }
        return id != null && id.equals(((Semis) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semis{" +
            "id=" + getId() +
            "}";
    }
}
