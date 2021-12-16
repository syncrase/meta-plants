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
 * Super-embranchement, Super-division ou Superphylum, Superdivisio
 */
@ApiModel(description = "Super-embranchement, Super-division ou Superphylum, Superdivisio")
@Entity
@Table(name = "super_division")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperDivision implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "superDivision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousDivisions", "synonymes", "superDivision", "division" }, allowSetters = true)
    private Set<Division> divisions = new HashSet<>();

    @OneToMany(mappedBy = "superDivision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "divisions", "synonymes", "infraRegne", "superDivision" }, allowSetters = true)
    private Set<SuperDivision> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "superDivisions", "synonymes", "rameau", "infraRegne" }, allowSetters = true)
    private InfraRegne infraRegne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "divisions", "synonymes", "infraRegne", "superDivision" }, allowSetters = true)
    private SuperDivision superDivision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuperDivision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SuperDivision nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SuperDivision nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Division> getDivisions() {
        return this.divisions;
    }

    public void setDivisions(Set<Division> divisions) {
        if (this.divisions != null) {
            this.divisions.forEach(i -> i.setSuperDivision(null));
        }
        if (divisions != null) {
            divisions.forEach(i -> i.setSuperDivision(this));
        }
        this.divisions = divisions;
    }

    public SuperDivision divisions(Set<Division> divisions) {
        this.setDivisions(divisions);
        return this;
    }

    public SuperDivision addDivisions(Division division) {
        this.divisions.add(division);
        division.setSuperDivision(this);
        return this;
    }

    public SuperDivision removeDivisions(Division division) {
        this.divisions.remove(division);
        division.setSuperDivision(null);
        return this;
    }

    public Set<SuperDivision> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SuperDivision> superDivisions) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSuperDivision(null));
        }
        if (superDivisions != null) {
            superDivisions.forEach(i -> i.setSuperDivision(this));
        }
        this.synonymes = superDivisions;
    }

    public SuperDivision synonymes(Set<SuperDivision> superDivisions) {
        this.setSynonymes(superDivisions);
        return this;
    }

    public SuperDivision addSynonymes(SuperDivision superDivision) {
        this.synonymes.add(superDivision);
        superDivision.setSuperDivision(this);
        return this;
    }

    public SuperDivision removeSynonymes(SuperDivision superDivision) {
        this.synonymes.remove(superDivision);
        superDivision.setSuperDivision(null);
        return this;
    }

    public InfraRegne getInfraRegne() {
        return this.infraRegne;
    }

    public void setInfraRegne(InfraRegne infraRegne) {
        this.infraRegne = infraRegne;
    }

    public SuperDivision infraRegne(InfraRegne infraRegne) {
        this.setInfraRegne(infraRegne);
        return this;
    }

    public SuperDivision getSuperDivision() {
        return this.superDivision;
    }

    public void setSuperDivision(SuperDivision superDivision) {
        this.superDivision = superDivision;
    }

    public SuperDivision superDivision(SuperDivision superDivision) {
        this.setSuperDivision(superDivision);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperDivision)) {
            return false;
        }
        return id != null && id.equals(((SuperDivision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperDivision{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return infraRegne;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return divisions;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
