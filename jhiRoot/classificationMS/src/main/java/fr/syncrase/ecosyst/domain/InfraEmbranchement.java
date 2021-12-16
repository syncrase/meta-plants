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
 * Infraphylum
 */
@ApiModel(description = "Infraphylum")
@Entity
@Table(name = "infra_embranchement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfraEmbranchement implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "infraEmbranchement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superClasses", "synonymes", "infraEmbranchement", "microEmbranchement" }, allowSetters = true)
    private Set<MicroEmbranchement> microEmbranchements = new HashSet<>();

    @OneToMany(mappedBy = "infraEmbranchement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "microEmbranchements", "synonymes", "sousDivision", "infraEmbranchement" }, allowSetters = true)
    private Set<InfraEmbranchement> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraEmbranchements", "synonymes", "division", "sousDivision" }, allowSetters = true)
    private SousDivision sousDivision;

    @ManyToOne
    @JsonIgnoreProperties(value = { "microEmbranchements", "synonymes", "sousDivision", "infraEmbranchement" }, allowSetters = true)
    private InfraEmbranchement infraEmbranchement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfraEmbranchement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public InfraEmbranchement nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public InfraEmbranchement nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<MicroEmbranchement> getMicroEmbranchements() {
        return this.microEmbranchements;
    }

    public void setMicroEmbranchements(Set<MicroEmbranchement> microEmbranchements) {
        if (this.microEmbranchements != null) {
            this.microEmbranchements.forEach(i -> i.setInfraEmbranchement(null));
        }
        if (microEmbranchements != null) {
            microEmbranchements.forEach(i -> i.setInfraEmbranchement(this));
        }
        this.microEmbranchements = microEmbranchements;
    }

    public InfraEmbranchement microEmbranchements(Set<MicroEmbranchement> microEmbranchements) {
        this.setMicroEmbranchements(microEmbranchements);
        return this;
    }

    public InfraEmbranchement addMicroEmbranchements(MicroEmbranchement microEmbranchement) {
        this.microEmbranchements.add(microEmbranchement);
        microEmbranchement.setInfraEmbranchement(this);
        return this;
    }

    public InfraEmbranchement removeMicroEmbranchements(MicroEmbranchement microEmbranchement) {
        this.microEmbranchements.remove(microEmbranchement);
        microEmbranchement.setInfraEmbranchement(null);
        return this;
    }

    public Set<InfraEmbranchement> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<InfraEmbranchement> infraEmbranchements) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setInfraEmbranchement(null));
        }
        if (infraEmbranchements != null) {
            infraEmbranchements.forEach(i -> i.setInfraEmbranchement(this));
        }
        this.synonymes = infraEmbranchements;
    }

    public InfraEmbranchement synonymes(Set<InfraEmbranchement> infraEmbranchements) {
        this.setSynonymes(infraEmbranchements);
        return this;
    }

    public InfraEmbranchement addSynonymes(InfraEmbranchement infraEmbranchement) {
        this.synonymes.add(infraEmbranchement);
        infraEmbranchement.setInfraEmbranchement(this);
        return this;
    }

    public InfraEmbranchement removeSynonymes(InfraEmbranchement infraEmbranchement) {
        this.synonymes.remove(infraEmbranchement);
        infraEmbranchement.setInfraEmbranchement(null);
        return this;
    }

    public SousDivision getSousDivision() {
        return this.sousDivision;
    }

    public void setSousDivision(SousDivision sousDivision) {
        this.sousDivision = sousDivision;
    }

    public InfraEmbranchement sousDivision(SousDivision sousDivision) {
        this.setSousDivision(sousDivision);
        return this;
    }

    public InfraEmbranchement getInfraEmbranchement() {
        return this.infraEmbranchement;
    }

    public void setInfraEmbranchement(InfraEmbranchement infraEmbranchement) {
        this.infraEmbranchement = infraEmbranchement;
    }

    public InfraEmbranchement infraEmbranchement(InfraEmbranchement infraEmbranchement) {
        this.setInfraEmbranchement(infraEmbranchement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfraEmbranchement)) {
            return false;
        }
        return id != null && id.equals(((InfraEmbranchement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraEmbranchement{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return sousDivision;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return microEmbranchements;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
