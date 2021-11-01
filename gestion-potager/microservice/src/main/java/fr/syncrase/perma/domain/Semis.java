package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Semis.
 */
@Entity
@Table(name = "semis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Semis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee semisPleineTerre;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee semisSousAbris;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeSemis typeSemis;

    @OneToOne
    @JoinColumn(unique = true)
    private Germination germination;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeriodeAnnee getSemisPleineTerre() {
        return semisPleineTerre;
    }

    public Semis semisPleineTerre(PeriodeAnnee periodeAnnee) {
        this.semisPleineTerre = periodeAnnee;
        return this;
    }

    public void setSemisPleineTerre(PeriodeAnnee periodeAnnee) {
        this.semisPleineTerre = periodeAnnee;
    }

    public PeriodeAnnee getSemisSousAbris() {
        return semisSousAbris;
    }

    public Semis semisSousAbris(PeriodeAnnee periodeAnnee) {
        this.semisSousAbris = periodeAnnee;
        return this;
    }

    public void setSemisSousAbris(PeriodeAnnee periodeAnnee) {
        this.semisSousAbris = periodeAnnee;
    }

    public TypeSemis getTypeSemis() {
        return typeSemis;
    }

    public Semis typeSemis(TypeSemis typeSemis) {
        this.typeSemis = typeSemis;
        return this;
    }

    public void setTypeSemis(TypeSemis typeSemis) {
        this.typeSemis = typeSemis;
    }

    public Germination getGermination() {
        return germination;
    }

    public Semis germination(Germination germination) {
        this.germination = germination;
        return this;
    }

    public void setGermination(Germination germination) {
        this.germination = germination;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semis{" +
            "id=" + getId() +
            "}";
    }
}
