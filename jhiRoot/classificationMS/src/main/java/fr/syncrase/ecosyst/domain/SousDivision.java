package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Sous-embranchement, Sous-division ou Subphylum, Subdivisio
 */
@ApiModel(description = "Sous-embranchement, Sous-division ou Subphylum, Subdivisio")
@Entity
@Table(name = "sous_division")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousDivision implements Serializable {

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

    @OneToMany(mappedBy = "sousDivision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "microEmbranchements", "synonymes", "sousDivision", "infraEmbranchement" }, allowSetters = true)
    private Set<InfraEmbranchement> infraEmbranchements = new HashSet<>();

    @OneToMany(mappedBy = "sousDivision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraEmbranchements", "synonymes", "division", "sousDivision" }, allowSetters = true)
    private Set<SousDivision> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousDivisions", "synonymes", "superDivision", "division" }, allowSetters = true)
    private Division division;

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraEmbranchements", "synonymes", "division", "sousDivision" }, allowSetters = true)
    private SousDivision sousDivision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousDivision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousDivision nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousDivision nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<InfraEmbranchement> getInfraEmbranchements() {
        return this.infraEmbranchements;
    }

    public void setInfraEmbranchements(Set<InfraEmbranchement> infraEmbranchements) {
        if (this.infraEmbranchements != null) {
            this.infraEmbranchements.forEach(i -> i.setSousDivision(null));
        }
        if (infraEmbranchements != null) {
            infraEmbranchements.forEach(i -> i.setSousDivision(this));
        }
        this.infraEmbranchements = infraEmbranchements;
    }

    public SousDivision infraEmbranchements(Set<InfraEmbranchement> infraEmbranchements) {
        this.setInfraEmbranchements(infraEmbranchements);
        return this;
    }

    public SousDivision addInfraEmbranchements(InfraEmbranchement infraEmbranchement) {
        this.infraEmbranchements.add(infraEmbranchement);
        infraEmbranchement.setSousDivision(this);
        return this;
    }

    public SousDivision removeInfraEmbranchements(InfraEmbranchement infraEmbranchement) {
        this.infraEmbranchements.remove(infraEmbranchement);
        infraEmbranchement.setSousDivision(null);
        return this;
    }

    public Set<SousDivision> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousDivision> sousDivisions) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousDivision(null));
        }
        if (sousDivisions != null) {
            sousDivisions.forEach(i -> i.setSousDivision(this));
        }
        this.synonymes = sousDivisions;
    }

    public SousDivision synonymes(Set<SousDivision> sousDivisions) {
        this.setSynonymes(sousDivisions);
        return this;
    }

    public SousDivision addSynonymes(SousDivision sousDivision) {
        this.synonymes.add(sousDivision);
        sousDivision.setSousDivision(this);
        return this;
    }

    public SousDivision removeSynonymes(SousDivision sousDivision) {
        this.synonymes.remove(sousDivision);
        sousDivision.setSousDivision(null);
        return this;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public SousDivision division(Division division) {
        this.setDivision(division);
        return this;
    }

    public SousDivision getSousDivision() {
        return this.sousDivision;
    }

    public void setSousDivision(SousDivision sousDivision) {
        this.sousDivision = sousDivision;
    }

    public SousDivision sousDivision(SousDivision sousDivision) {
        this.setSousDivision(sousDivision);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousDivision)) {
            return false;
        }
        return id != null && id.equals(((SousDivision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousDivision{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
