package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PeriodeAnnee.
 */
@Entity
@Table(name = "periode_annee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PeriodeAnnee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Mois debut;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Mois fin;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mois getDebut() {
        return debut;
    }

    public PeriodeAnnee debut(Mois mois) {
        this.debut = mois;
        return this;
    }

    public void setDebut(Mois mois) {
        this.debut = mois;
    }

    public Mois getFin() {
        return fin;
    }

    public PeriodeAnnee fin(Mois mois) {
        this.fin = mois;
        return this;
    }

    public void setFin(Mois mois) {
        this.fin = mois;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeAnnee{" +
            "id=" + getId() +
            "}";
    }
}
