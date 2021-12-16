package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Embranchement, Division ou Phylum, Divisio 2
 */
@ApiModel(description = "Embranchement, Division ou Phylum, Divisio 2")
@Entity
@Table(name = "division")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Division implements Serializable, CronquistRank {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_fr", nullable = false)
    private String nomFr;

    @Column(name = "nom_latin")
    private String nomLatin;

    @OneToMany(mappedBy = "division")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraEmbranchements", "synonymes", "division", "sousDivision" }, allowSetters = true)
    private Set<SousDivision> sousDivisions = new HashSet<>();

    @OneToMany(mappedBy = "division")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousDivisions", "synonymes", "superDivision", "division" }, allowSetters = true)
    private Set<Division> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "divisions", "synonymes", "infraRegne", "superDivision" }, allowSetters = true)
    private SuperDivision superDivision;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousDivisions", "synonymes", "superDivision", "division" }, allowSetters = true)
    private Division division;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Division id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Division nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Division nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousDivision> getSousDivisions() {
        return this.sousDivisions;
    }

    public void setSousDivisions(Set<SousDivision> sousDivisions) {
        if (this.sousDivisions != null) {
            this.sousDivisions.forEach(i -> i.setDivision(null));
        }
        if (sousDivisions != null) {
            sousDivisions.forEach(i -> i.setDivision(this));
        }
        this.sousDivisions = sousDivisions;
    }

    public Division sousDivisions(Set<SousDivision> sousDivisions) {
        this.setSousDivisions(sousDivisions);
        return this;
    }

    public Division addSousDivisions(SousDivision sousDivision) {
        this.sousDivisions.add(sousDivision);
        sousDivision.setDivision(this);
        return this;
    }

    public Division removeSousDivisions(SousDivision sousDivision) {
        this.sousDivisions.remove(sousDivision);
        sousDivision.setDivision(null);
        return this;
    }

    public Set<Division> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Division> divisions) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setDivision(null));
        }
        if (divisions != null) {
            divisions.forEach(i -> i.setDivision(this));
        }
        this.synonymes = divisions;
    }

    public Division synonymes(Set<Division> divisions) {
        this.setSynonymes(divisions);
        return this;
    }

    public Division addSynonymes(Division division) {
        this.synonymes.add(division);
        division.setDivision(this);
        return this;
    }

    public Division removeSynonymes(Division division) {
        this.synonymes.remove(division);
        division.setDivision(null);
        return this;
    }

    public SuperDivision getSuperDivision() {
        return this.superDivision;
    }

    public void setSuperDivision(SuperDivision superDivision) {
        this.superDivision = superDivision;
    }

    public Division superDivision(SuperDivision superDivision) {
        this.setSuperDivision(superDivision);
        return this;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Division division(Division division) {
        this.setDivision(division);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Division)) {
            return false;
        }
        return id != null && id.equals(((Division) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Division{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return superDivision;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousDivisions;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
