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
 * Microphylum
 */
@ApiModel(description = "Microphylum")
@Entity
@Table(name = "micro_embranchement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MicroEmbranchement implements Serializable {

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

    @OneToMany(mappedBy = "microEmbranchement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classes", "synonymes", "microEmbranchement", "superClasse" }, allowSetters = true)
    private Set<SuperClasse> superClasses = new HashSet<>();

    @OneToMany(mappedBy = "microEmbranchement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superClasses", "synonymes", "infraEmbranchement", "microEmbranchement" }, allowSetters = true)
    private Set<MicroEmbranchement> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "microEmbranchements", "synonymes", "sousDivision", "infraEmbranchement" }, allowSetters = true)
    private InfraEmbranchement infraEmbranchement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "superClasses", "synonymes", "infraEmbranchement", "microEmbranchement" }, allowSetters = true)
    private MicroEmbranchement microEmbranchement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MicroEmbranchement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public MicroEmbranchement nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public MicroEmbranchement nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SuperClasse> getSuperClasses() {
        return this.superClasses;
    }

    public void setSuperClasses(Set<SuperClasse> superClasses) {
        if (this.superClasses != null) {
            this.superClasses.forEach(i -> i.setMicroEmbranchement(null));
        }
        if (superClasses != null) {
            superClasses.forEach(i -> i.setMicroEmbranchement(this));
        }
        this.superClasses = superClasses;
    }

    public MicroEmbranchement superClasses(Set<SuperClasse> superClasses) {
        this.setSuperClasses(superClasses);
        return this;
    }

    public MicroEmbranchement addSuperClasses(SuperClasse superClasse) {
        this.superClasses.add(superClasse);
        superClasse.setMicroEmbranchement(this);
        return this;
    }

    public MicroEmbranchement removeSuperClasses(SuperClasse superClasse) {
        this.superClasses.remove(superClasse);
        superClasse.setMicroEmbranchement(null);
        return this;
    }

    public Set<MicroEmbranchement> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<MicroEmbranchement> microEmbranchements) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setMicroEmbranchement(null));
        }
        if (microEmbranchements != null) {
            microEmbranchements.forEach(i -> i.setMicroEmbranchement(this));
        }
        this.synonymes = microEmbranchements;
    }

    public MicroEmbranchement synonymes(Set<MicroEmbranchement> microEmbranchements) {
        this.setSynonymes(microEmbranchements);
        return this;
    }

    public MicroEmbranchement addSynonymes(MicroEmbranchement microEmbranchement) {
        this.synonymes.add(microEmbranchement);
        microEmbranchement.setMicroEmbranchement(this);
        return this;
    }

    public MicroEmbranchement removeSynonymes(MicroEmbranchement microEmbranchement) {
        this.synonymes.remove(microEmbranchement);
        microEmbranchement.setMicroEmbranchement(null);
        return this;
    }

    public InfraEmbranchement getInfraEmbranchement() {
        return this.infraEmbranchement;
    }

    public void setInfraEmbranchement(InfraEmbranchement infraEmbranchement) {
        this.infraEmbranchement = infraEmbranchement;
    }

    public MicroEmbranchement infraEmbranchement(InfraEmbranchement infraEmbranchement) {
        this.setInfraEmbranchement(infraEmbranchement);
        return this;
    }

    public MicroEmbranchement getMicroEmbranchement() {
        return this.microEmbranchement;
    }

    public void setMicroEmbranchement(MicroEmbranchement microEmbranchement) {
        this.microEmbranchement = microEmbranchement;
    }

    public MicroEmbranchement microEmbranchement(MicroEmbranchement microEmbranchement) {
        this.setMicroEmbranchement(microEmbranchement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MicroEmbranchement)) {
            return false;
        }
        return id != null && id.equals(((MicroEmbranchement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MicroEmbranchement{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
