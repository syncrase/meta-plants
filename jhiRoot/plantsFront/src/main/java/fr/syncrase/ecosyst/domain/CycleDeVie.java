package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CycleDeVie.
 */
@Table("cycle_de_vie")
public class CycleDeVie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private Semis semis;

    @Transient
    private PeriodeAnnee apparitionFeuilles;

    @Transient
    private PeriodeAnnee floraison;

    @Transient
    private PeriodeAnnee recolte;

    @Transient
    private PeriodeAnnee croissance;

    @Transient
    private PeriodeAnnee maturite;

    @Transient
    private PeriodeAnnee plantation;

    @Transient
    private PeriodeAnnee rempotage;

    @Transient
    @JsonIgnoreProperties(value = { "cycleDeVies" }, allowSetters = true)
    private Reproduction reproduction;

    @Column("semis_id")
    private Long semisId;

    @Column("apparition_feuilles_id")
    private Long apparitionFeuillesId;

    @Column("floraison_id")
    private Long floraisonId;

    @Column("recolte_id")
    private Long recolteId;

    @Column("croissance_id")
    private Long croissanceId;

    @Column("maturite_id")
    private Long maturiteId;

    @Column("plantation_id")
    private Long plantationId;

    @Column("rempotage_id")
    private Long rempotageId;

    @Column("reproduction_id")
    private Long reproductionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CycleDeVie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Semis getSemis() {
        return this.semis;
    }

    public void setSemis(Semis semis) {
        this.semis = semis;
        this.semisId = semis != null ? semis.getId() : null;
    }

    public CycleDeVie semis(Semis semis) {
        this.setSemis(semis);
        return this;
    }

    public PeriodeAnnee getApparitionFeuilles() {
        return this.apparitionFeuilles;
    }

    public void setApparitionFeuilles(PeriodeAnnee periodeAnnee) {
        this.apparitionFeuilles = periodeAnnee;
        this.apparitionFeuillesId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie apparitionFeuilles(PeriodeAnnee periodeAnnee) {
        this.setApparitionFeuilles(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getFloraison() {
        return this.floraison;
    }

    public void setFloraison(PeriodeAnnee periodeAnnee) {
        this.floraison = periodeAnnee;
        this.floraisonId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie floraison(PeriodeAnnee periodeAnnee) {
        this.setFloraison(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getRecolte() {
        return this.recolte;
    }

    public void setRecolte(PeriodeAnnee periodeAnnee) {
        this.recolte = periodeAnnee;
        this.recolteId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie recolte(PeriodeAnnee periodeAnnee) {
        this.setRecolte(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getCroissance() {
        return this.croissance;
    }

    public void setCroissance(PeriodeAnnee periodeAnnee) {
        this.croissance = periodeAnnee;
        this.croissanceId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie croissance(PeriodeAnnee periodeAnnee) {
        this.setCroissance(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getMaturite() {
        return this.maturite;
    }

    public void setMaturite(PeriodeAnnee periodeAnnee) {
        this.maturite = periodeAnnee;
        this.maturiteId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie maturite(PeriodeAnnee periodeAnnee) {
        this.setMaturite(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getPlantation() {
        return this.plantation;
    }

    public void setPlantation(PeriodeAnnee periodeAnnee) {
        this.plantation = periodeAnnee;
        this.plantationId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie plantation(PeriodeAnnee periodeAnnee) {
        this.setPlantation(periodeAnnee);
        return this;
    }

    public PeriodeAnnee getRempotage() {
        return this.rempotage;
    }

    public void setRempotage(PeriodeAnnee periodeAnnee) {
        this.rempotage = periodeAnnee;
        this.rempotageId = periodeAnnee != null ? periodeAnnee.getId() : null;
    }

    public CycleDeVie rempotage(PeriodeAnnee periodeAnnee) {
        this.setRempotage(periodeAnnee);
        return this;
    }

    public Reproduction getReproduction() {
        return this.reproduction;
    }

    public void setReproduction(Reproduction reproduction) {
        this.reproduction = reproduction;
        this.reproductionId = reproduction != null ? reproduction.getId() : null;
    }

    public CycleDeVie reproduction(Reproduction reproduction) {
        this.setReproduction(reproduction);
        return this;
    }

    public Long getSemisId() {
        return this.semisId;
    }

    public void setSemisId(Long semis) {
        this.semisId = semis;
    }

    public Long getApparitionFeuillesId() {
        return this.apparitionFeuillesId;
    }

    public void setApparitionFeuillesId(Long periodeAnnee) {
        this.apparitionFeuillesId = periodeAnnee;
    }

    public Long getFloraisonId() {
        return this.floraisonId;
    }

    public void setFloraisonId(Long periodeAnnee) {
        this.floraisonId = periodeAnnee;
    }

    public Long getRecolteId() {
        return this.recolteId;
    }

    public void setRecolteId(Long periodeAnnee) {
        this.recolteId = periodeAnnee;
    }

    public Long getCroissanceId() {
        return this.croissanceId;
    }

    public void setCroissanceId(Long periodeAnnee) {
        this.croissanceId = periodeAnnee;
    }

    public Long getMaturiteId() {
        return this.maturiteId;
    }

    public void setMaturiteId(Long periodeAnnee) {
        this.maturiteId = periodeAnnee;
    }

    public Long getPlantationId() {
        return this.plantationId;
    }

    public void setPlantationId(Long periodeAnnee) {
        this.plantationId = periodeAnnee;
    }

    public Long getRempotageId() {
        return this.rempotageId;
    }

    public void setRempotageId(Long periodeAnnee) {
        this.rempotageId = periodeAnnee;
    }

    public Long getReproductionId() {
        return this.reproductionId;
    }

    public void setReproductionId(Long reproduction) {
        this.reproductionId = reproduction;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDeVie{" +
            "id=" + getId() +
            "}";
    }
}
