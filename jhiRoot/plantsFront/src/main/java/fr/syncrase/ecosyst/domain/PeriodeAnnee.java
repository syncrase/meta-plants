package fr.syncrase.ecosyst.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PeriodeAnnee.
 */
@Table("periode_annee")
public class PeriodeAnnee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private Mois debut;

    @Transient
    private Mois fin;

    @Column("debut_id")
    private Long debutId;

    @Column("fin_id")
    private Long finId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PeriodeAnnee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mois getDebut() {
        return this.debut;
    }

    public void setDebut(Mois mois) {
        this.debut = mois;
        this.debutId = mois != null ? mois.getId() : null;
    }

    public PeriodeAnnee debut(Mois mois) {
        this.setDebut(mois);
        return this;
    }

    public Mois getFin() {
        return this.fin;
    }

    public void setFin(Mois mois) {
        this.fin = mois;
        this.finId = mois != null ? mois.getId() : null;
    }

    public PeriodeAnnee fin(Mois mois) {
        this.setFin(mois);
        return this;
    }

    public Long getDebutId() {
        return this.debutId;
    }

    public void setDebutId(Long mois) {
        this.debutId = mois;
    }

    public Long getFinId() {
        return this.finId;
    }

    public void setFinId(Long mois) {
        this.finId = mois;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodeAnnee)) {
            return false;
        }
        return id != null && id.equals(((PeriodeAnnee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeAnnee{" +
            "id=" + getId() +
            "}";
    }
}
