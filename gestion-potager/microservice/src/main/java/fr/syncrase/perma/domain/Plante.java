package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plante.
 */
@Entity
@Table(name = "plante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_latin", nullable = false)
    private String nomLatin;

    @Column(name = "entretien")
    private String entretien;

    @Column(name = "histoire")
    private String histoire;

    @Column(name = "vitesse")
    private String vitesse;

    @JsonIgnoreProperties(
        value = {
            "semis", "apparitionFeuilles", "floraison", "recolte", "croissance", "maturite", "plantation", "rempotage", "reproduction",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private CycleDeVie cycleDeVie;

    @JsonIgnoreProperties(value = { "raunkier", "cronquist", "apg1", "apg2", "apg3", "apg4" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Classification classification;

    @OneToMany(mappedBy = "confusion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "confusion" }, allowSetters = true)
    private Set<Ressemblance> confusions = new HashSet<>();

    @OneToMany(mappedBy = "interaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cible", "origine", "interaction" }, allowSetters = true)
    private Set<Allelopathie> interactions = new HashSet<>();

    @OneToMany(mappedBy = "plante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plante" }, allowSetters = true)
    private Set<Exposition> expositions = new HashSet<>();

    @OneToMany(mappedBy = "plante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plante" }, allowSetters = true)
    private Set<Sol> sols = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_plante__noms_vernaculaires",
        joinColumns = @JoinColumn(name = "plante_id"),
        inverseJoinColumns = @JoinColumn(name = "noms_vernaculaires_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Set<NomVernaculaire> nomsVernaculaires = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Temperature temperature;

    @ManyToOne
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Racine racine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Strate strate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "plantes" }, allowSetters = true)
    private Feuillage feuillage;

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

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Plante nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
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

    public String getVitesse() {
        return this.vitesse;
    }

    public Plante vitesse(String vitesse) {
        this.setVitesse(vitesse);
        return this;
    }

    public void setVitesse(String vitesse) {
        this.vitesse = vitesse;
    }

    public CycleDeVie getCycleDeVie() {
        return this.cycleDeVie;
    }

    public void setCycleDeVie(CycleDeVie cycleDeVie) {
        this.cycleDeVie = cycleDeVie;
    }

    public Plante cycleDeVie(CycleDeVie cycleDeVie) {
        this.setCycleDeVie(cycleDeVie);
        return this;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Plante classification(Classification classification) {
        this.setClassification(classification);
        return this;
    }

    public Set<Ressemblance> getConfusions() {
        return this.confusions;
    }

    public void setConfusions(Set<Ressemblance> ressemblances) {
        if (this.confusions != null) {
            this.confusions.forEach(i -> i.setConfusion(null));
        }
        if (ressemblances != null) {
            ressemblances.forEach(i -> i.setConfusion(this));
        }
        this.confusions = ressemblances;
    }

    public Plante confusions(Set<Ressemblance> ressemblances) {
        this.setConfusions(ressemblances);
        return this;
    }

    public Plante addConfusions(Ressemblance ressemblance) {
        this.confusions.add(ressemblance);
        ressemblance.setConfusion(this);
        return this;
    }

    public Plante removeConfusions(Ressemblance ressemblance) {
        this.confusions.remove(ressemblance);
        ressemblance.setConfusion(null);
        return this;
    }

    public Set<Allelopathie> getInteractions() {
        return this.interactions;
    }

    public void setInteractions(Set<Allelopathie> allelopathies) {
        if (this.interactions != null) {
            this.interactions.forEach(i -> i.setInteraction(null));
        }
        if (allelopathies != null) {
            allelopathies.forEach(i -> i.setInteraction(this));
        }
        this.interactions = allelopathies;
    }

    public Plante interactions(Set<Allelopathie> allelopathies) {
        this.setInteractions(allelopathies);
        return this;
    }

    public Plante addInteractions(Allelopathie allelopathie) {
        this.interactions.add(allelopathie);
        allelopathie.setInteraction(this);
        return this;
    }

    public Plante removeInteractions(Allelopathie allelopathie) {
        this.interactions.remove(allelopathie);
        allelopathie.setInteraction(null);
        return this;
    }

    public Set<Exposition> getExpositions() {
        return this.expositions;
    }

    public void setExpositions(Set<Exposition> expositions) {
        if (this.expositions != null) {
            this.expositions.forEach(i -> i.setPlante(null));
        }
        if (expositions != null) {
            expositions.forEach(i -> i.setPlante(this));
        }
        this.expositions = expositions;
    }

    public Plante expositions(Set<Exposition> expositions) {
        this.setExpositions(expositions);
        return this;
    }

    public Plante addExpositions(Exposition exposition) {
        this.expositions.add(exposition);
        exposition.setPlante(this);
        return this;
    }

    public Plante removeExpositions(Exposition exposition) {
        this.expositions.remove(exposition);
        exposition.setPlante(null);
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
    }

    public Plante feuillage(Feuillage feuillage) {
        this.setFeuillage(feuillage);
        return this;
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
            ", nomLatin='" + getNomLatin() + "'" +
            ", entretien='" + getEntretien() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", vitesse='" + getVitesse() + "'" +
            "}";
    }
}
