package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    private Long id;

    @NotNull
    @Column(name = "nom_latin", nullable = false)
    private String nomLatin;

    @Column(name = "entretien")
    private String entretien;

    @Column(name = "histoire")
    private String histoire;

    @Column(name = "exposition")
    private String exposition;

    @Column(name = "rusticite")
    private String rusticite;

    @OneToOne
    @JoinColumn(unique = true)
    private CycleDeVie cycleDeVie;

    @OneToOne
    @JoinColumn(unique = true)
    private Classification classification;

    @OneToMany(mappedBy = "confusion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Ressemblance> confusions = new HashSet<>();

    @OneToMany(mappedBy = "plante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Allelopathie> interactions = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "plante_noms_vernaculaires",
               joinColumns = @JoinColumn(name = "plante_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "noms_vernaculaires_id", referencedColumnName = "id"))
    private Set<NomVernaculaire> nomsVernaculaires = new HashSet<>();

    @OneToOne(mappedBy = "cible")
    @JsonIgnore
    private Allelopathie allelopathieRecue;

    @OneToOne(mappedBy = "origine")
    @JsonIgnore
    private Allelopathie allelopathieProduite;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLatin() {
        return nomLatin;
    }

    public Plante nomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getEntretien() {
        return entretien;
    }

    public Plante entretien(String entretien) {
        this.entretien = entretien;
        return this;
    }

    public void setEntretien(String entretien) {
        this.entretien = entretien;
    }

    public String getHistoire() {
        return histoire;
    }

    public Plante histoire(String histoire) {
        this.histoire = histoire;
        return this;
    }

    public void setHistoire(String histoire) {
        this.histoire = histoire;
    }

    public String getExposition() {
        return exposition;
    }

    public Plante exposition(String exposition) {
        this.exposition = exposition;
        return this;
    }

    public void setExposition(String exposition) {
        this.exposition = exposition;
    }

    public String getRusticite() {
        return rusticite;
    }

    public Plante rusticite(String rusticite) {
        this.rusticite = rusticite;
        return this;
    }

    public void setRusticite(String rusticite) {
        this.rusticite = rusticite;
    }

    public CycleDeVie getCycleDeVie() {
        return cycleDeVie;
    }

    public Plante cycleDeVie(CycleDeVie cycleDeVie) {
        this.cycleDeVie = cycleDeVie;
        return this;
    }

    public void setCycleDeVie(CycleDeVie cycleDeVie) {
        this.cycleDeVie = cycleDeVie;
    }

    public Classification getClassification() {
        return classification;
    }

    public Plante classification(Classification classification) {
        this.classification = classification;
        return this;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Set<Ressemblance> getConfusions() {
        return confusions;
    }

    public Plante confusions(Set<Ressemblance> ressemblances) {
        this.confusions = ressemblances;
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

    public void setConfusions(Set<Ressemblance> ressemblances) {
        this.confusions = ressemblances;
    }

    public Set<Allelopathie> getInteractions() {
        return interactions;
    }

    public Plante interactions(Set<Allelopathie> allelopathies) {
        this.interactions = allelopathies;
        return this;
    }

    public Plante addInteractions(Allelopathie allelopathie) {
        this.interactions.add(allelopathie);
        allelopathie.setPlante(this);
        return this;
    }

    public Plante removeInteractions(Allelopathie allelopathie) {
        this.interactions.remove(allelopathie);
        allelopathie.setPlante(null);
        return this;
    }

    public void setInteractions(Set<Allelopathie> allelopathies) {
        this.interactions = allelopathies;
    }

    public Set<NomVernaculaire> getNomsVernaculaires() {
        return nomsVernaculaires;
    }

    public Plante nomsVernaculaires(Set<NomVernaculaire> nomVernaculaires) {
        this.nomsVernaculaires = nomVernaculaires;
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

    public void setNomsVernaculaires(Set<NomVernaculaire> nomVernaculaires) {
        this.nomsVernaculaires = nomVernaculaires;
    }

    public Allelopathie getAllelopathieRecue() {
        return allelopathieRecue;
    }

    public Plante allelopathieRecue(Allelopathie allelopathie) {
        this.allelopathieRecue = allelopathie;
        return this;
    }

    public void setAllelopathieRecue(Allelopathie allelopathie) {
        this.allelopathieRecue = allelopathie;
    }

    public Allelopathie getAllelopathieProduite() {
        return allelopathieProduite;
    }

    public Plante allelopathieProduite(Allelopathie allelopathie) {
        this.allelopathieProduite = allelopathie;
        return this;
    }

    public void setAllelopathieProduite(Allelopathie allelopathie) {
        this.allelopathieProduite = allelopathie;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plante{" +
            "id=" + getId() +
            ", nomLatin='" + getNomLatin() + "'" +
            ", entretien='" + getEntretien() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", exposition='" + getExposition() + "'" +
            ", rusticite='" + getRusticite() + "'" +
            "}";
    }
}
