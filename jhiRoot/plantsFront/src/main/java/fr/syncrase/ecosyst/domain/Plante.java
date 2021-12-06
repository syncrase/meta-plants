package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Plante.
 */
@Table("plante")
public class Plante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("entretien")
    private String entretien;

    @Column("histoire")
    private String histoire;

    @Column("vitesse_croissance")
    private String vitesseCroissance;

    @Column("exposition")
    private String exposition;

    @Transient
    private CycleDeVie cycleDeVie;

    @Transient
    @JsonIgnoreProperties(value = { "planteRessemblant" }, allowSetters = true)
    private Set<Ressemblance> confusions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "plante" }, allowSetters = true)
    private Set<Ensoleillement> ensoleillements = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "plante" }, allowSetters = true)
    private Set<Sol> sols = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "raunkier", "cronquist", "apg1", "apg2", "apg3", "apg4" }, allowSetters = true)
    private Classification classification;

    @Transient
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Set<NomVernaculaire> nomsVernaculaires = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Temperature temperature;

    @Transient
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Racine racine;

    @Transient
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Strate strate;

    @Transient
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Feuillage feuillage;

    @Column("cycle_de_vie_id")
    private Long cycleDeVieId;

    @Column("classification_id")
    private Long classificationId;

    @Column("temperature_id")
    private Long temperatureId;

    @Column("racine_id")
    private Long racineId;

    @Column("strate_id")
    private Long strateId;

    @Column("feuillage_id")
    private Long feuillageId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntretien() {
        return this.entretien;
    }

    public Plante entretien(String entretien) {
        this.setEntretien(entretien);
        return this;
    }

    public void setEntretien(String entretien) {
        this.entretien = entretien;
    }

    public String getHistoire() {
        return this.histoire;
    }

    public Plante histoire(String histoire) {
        this.setHistoire(histoire);
        return this;
    }

    public void setHistoire(String histoire) {
        this.histoire = histoire;
    }

    public String getVitesseCroissance() {
        return this.vitesseCroissance;
    }

    public Plante vitesseCroissance(String vitesseCroissance) {
        this.setVitesseCroissance(vitesseCroissance);
        return this;
    }

    public void setVitesseCroissance(String vitesseCroissance) {
        this.vitesseCroissance = vitesseCroissance;
    }

    public String getExposition() {
        return this.exposition;
    }

    public Plante exposition(String exposition) {
        this.setExposition(exposition);
        return this;
    }

    public void setExposition(String exposition) {
        this.exposition = exposition;
    }

    public CycleDeVie getCycleDeVie() {
        return this.cycleDeVie;
    }

    public void setCycleDeVie(CycleDeVie cycleDeVie) {
        this.cycleDeVie = cycleDeVie;
        this.cycleDeVieId = cycleDeVie != null ? cycleDeVie.getId() : null;
    }

    public Plante cycleDeVie(CycleDeVie cycleDeVie) {
        this.setCycleDeVie(cycleDeVie);
        return this;
    }

    public Set<Ressemblance> getConfusions() {
        return this.confusions;
    }

    public void setConfusions(Set<Ressemblance> ressemblances) {
        if (this.confusions != null) {
            this.confusions.forEach(i -> i.setPlanteRessemblant(null));
        }
        if (ressemblances != null) {
            ressemblances.forEach(i -> i.setPlanteRessemblant(this));
        }
        this.confusions = ressemblances;
    }

    public Plante confusions(Set<Ressemblance> ressemblances) {
        this.setConfusions(ressemblances);
        return this;
    }

    public Plante addConfusions(Ressemblance ressemblance) {
        this.confusions.add(ressemblance);
        ressemblance.setPlanteRessemblant(this);
        return this;
    }

    public Plante removeConfusions(Ressemblance ressemblance) {
        this.confusions.remove(ressemblance);
        ressemblance.setPlanteRessemblant(null);
        return this;
    }

    public Set<Ensoleillement> getEnsoleillements() {
        return this.ensoleillements;
    }

    public void setEnsoleillements(Set<Ensoleillement> ensoleillements) {
        if (this.ensoleillements != null) {
            this.ensoleillements.forEach(i -> i.setPlante(null));
        }
        if (ensoleillements != null) {
            ensoleillements.forEach(i -> i.setPlante(this));
        }
        this.ensoleillements = ensoleillements;
    }

    public Plante ensoleillements(Set<Ensoleillement> ensoleillements) {
        this.setEnsoleillements(ensoleillements);
        return this;
    }

    public Plante addEnsoleillements(Ensoleillement ensoleillement) {
        this.ensoleillements.add(ensoleillement);
        ensoleillement.setPlante(this);
        return this;
    }

    public Plante removeEnsoleillements(Ensoleillement ensoleillement) {
        this.ensoleillements.remove(ensoleillement);
        ensoleillement.setPlante(null);
        return this;
    }

    public Set<Sol> getSols() {
        return this.sols;
    }

    public void setSols(Set<Sol> sols) {
        if (this.sols != null) {
            this.sols.forEach(i -> i.setPlante(null));
        }
        if (sols != null) {
            sols.forEach(i -> i.setPlante(this));
        }
        this.sols = sols;
    }

    public Plante sols(Set<Sol> sols) {
        this.setSols(sols);
        return this;
    }

    public Plante addSols(Sol sol) {
        this.sols.add(sol);
        sol.setPlante(this);
        return this;
    }

    public Plante removeSols(Sol sol) {
        this.sols.remove(sol);
        sol.setPlante(null);
        return this;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
        this.classificationId = classification != null ? classification.getId() : null;
    }

    public Plante classification(Classification classification) {
        this.setClassification(classification);
        return this;
    }

    public Set<NomVernaculaire> getNomsVernaculaires() {
        return this.nomsVernaculaires;
    }

    public void setNomsVernaculaires(Set<NomVernaculaire> nomVernaculaires) {
        this.nomsVernaculaires = nomVernaculaires;
    }

    public Plante nomsVernaculaires(Set<NomVernaculaire> nomVernaculaires) {
        this.setNomsVernaculaires(nomVernaculaires);
        return this;
    }

    public Plante addNomsVernaculaires(NomVernaculaire nomVernaculaire) {
        this.nomsVernaculaires.add(nomVernaculaire);
        nomVernaculaire.getPlantes().add(this);
        return this;
    }

    public Plante removeNomsVernaculaires(NomVernaculaire nomVernaculaire) {
        this.nomsVernaculaires.remove(nomVernaculaire);
        nomVernaculaire.getPlantes().remove(this);
        return this;
    }

    public Temperature getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
        this.temperatureId = temperature != null ? temperature.getId() : null;
    }

    public Plante temperature(Temperature temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public Racine getRacine() {
        return this.racine;
    }

    public void setRacine(Racine racine) {
        this.racine = racine;
        this.racineId = racine != null ? racine.getId() : null;
    }

    public Plante racine(Racine racine) {
        this.setRacine(racine);
        return this;
    }

    public Strate getStrate() {
        return this.strate;
    }

    public void setStrate(Strate strate) {
        this.strate = strate;
        this.strateId = strate != null ? strate.getId() : null;
    }

    public Plante strate(Strate strate) {
        this.setStrate(strate);
        return this;
    }

    public Feuillage getFeuillage() {
        return this.feuillage;
    }

    public void setFeuillage(Feuillage feuillage) {
        this.feuillage = feuillage;
        this.feuillageId = feuillage != null ? feuillage.getId() : null;
    }

    public Plante feuillage(Feuillage feuillage) {
        this.setFeuillage(feuillage);
        return this;
    }

    public Long getCycleDeVieId() {
        return this.cycleDeVieId;
    }

    public void setCycleDeVieId(Long cycleDeVie) {
        this.cycleDeVieId = cycleDeVie;
    }

    public Long getClassificationId() {
        return this.classificationId;
    }

    public void setClassificationId(Long classification) {
        this.classificationId = classification;
    }

    public Long getTemperatureId() {
        return this.temperatureId;
    }

    public void setTemperatureId(Long temperature) {
        this.temperatureId = temperature;
    }

    public Long getRacineId() {
        return this.racineId;
    }

    public void setRacineId(Long racine) {
        this.racineId = racine;
    }

    public Long getStrateId() {
        return this.strateId;
    }

    public void setStrateId(Long strate) {
        this.strateId = strate;
    }

    public Long getFeuillageId() {
        return this.feuillageId;
    }

    public void setFeuillageId(Long feuillage) {
        this.feuillageId = feuillage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plante)) {
            return false;
        }
        return id != null && id.equals(((Plante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plante{" +
            "id=" + getId() +
            ", entretien='" + getEntretien() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", vitesseCroissance='" + getVitesseCroissance() + "'" +
            ", exposition='" + getExposition() + "'" +
            "}";
    }
}
