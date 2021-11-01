package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A CycleDeVie.
 */
@Entity
@Table(name = "cycle_de_vie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CycleDeVie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "vitesse_de_croissance")
    private String vitesseDeCroissance;

    @OneToOne
    @JoinColumn(unique = true)
    private Semis semis;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee apparitionFeuilles;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee floraison;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee recolte;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee croissance;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee maturite;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee plantation;

    @OneToOne
    @JoinColumn(unique = true)
    private PeriodeAnnee rempotage;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVitesseDeCroissance() {
        return vitesseDeCroissance;
    }

    public CycleDeVie vitesseDeCroissance(String vitesseDeCroissance) {
        this.vitesseDeCroissance = vitesseDeCroissance;
        return this;
    }

    public void setVitesseDeCroissance(String vitesseDeCroissance) {
        this.vitesseDeCroissance = vitesseDeCroissance;
    }

    public Semis getSemis() {
        return semis;
    }

    public CycleDeVie semis(Semis semis) {
        this.semis = semis;
        return this;
    }

    public void setSemis(Semis semis) {
        this.semis = semis;
    }

    public PeriodeAnnee getApparitionFeuilles() {
        return apparitionFeuilles;
    }

    public CycleDeVie apparitionFeuilles(PeriodeAnnee periodeAnnee) {
        this.apparitionFeuilles = periodeAnnee;
        return this;
    }

    public void setApparitionFeuilles(PeriodeAnnee periodeAnnee) {
        this.apparitionFeuilles = periodeAnnee;
    }

    public PeriodeAnnee getFloraison() {
        return floraison;
    }

    public CycleDeVie floraison(PeriodeAnnee periodeAnnee) {
        this.floraison = periodeAnnee;
        return this;
    }

    public void setFloraison(PeriodeAnnee periodeAnnee) {
        this.floraison = periodeAnnee;
    }

    public PeriodeAnnee getRecolte() {
        return recolte;
    }

    public CycleDeVie recolte(PeriodeAnnee periodeAnnee) {
        this.recolte = periodeAnnee;
        return this;
    }

    public void setRecolte(PeriodeAnnee periodeAnnee) {
        this.recolte = periodeAnnee;
    }

    public PeriodeAnnee getCroissance() {
        return croissance;
    }

    public CycleDeVie croissance(PeriodeAnnee periodeAnnee) {
        this.croissance = periodeAnnee;
        return this;
    }

    public void setCroissance(PeriodeAnnee periodeAnnee) {
        this.croissance = periodeAnnee;
    }

    public PeriodeAnnee getMaturite() {
        return maturite;
    }

    public CycleDeVie maturite(PeriodeAnnee periodeAnnee) {
        this.maturite = periodeAnnee;
        return this;
    }

    public void setMaturite(PeriodeAnnee periodeAnnee) {
        this.maturite = periodeAnnee;
    }

    public PeriodeAnnee getPlantation() {
        return plantation;
    }

    public CycleDeVie plantation(PeriodeAnnee periodeAnnee) {
        this.plantation = periodeAnnee;
        return this;
    }

    public void setPlantation(PeriodeAnnee periodeAnnee) {
        this.plantation = periodeAnnee;
    }

    public PeriodeAnnee getRempotage() {
        return rempotage;
    }

    public CycleDeVie rempotage(PeriodeAnnee periodeAnnee) {
        this.rempotage = periodeAnnee;
        return this;
    }

    public void setRempotage(PeriodeAnnee periodeAnnee) {
        this.rempotage = periodeAnnee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDeVie)) {
            return false;
        }
        return id != null && id.equals(((CycleDeVie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDeVie{" +
            "id=" + getId() +
            ", vitesseDeCroissance='" + getVitesseDeCroissance() + "'" +
            "}";
    }
}
