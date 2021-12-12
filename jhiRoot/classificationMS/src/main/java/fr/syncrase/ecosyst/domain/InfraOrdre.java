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
 * Infraordo
 */
@ApiModel(description = "Infraordo")
@Entity
@Table(name = "infra_ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfraOrdre implements Serializable {

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

    @OneToMany(mappedBy = "infraOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superFamilles", "synonymes", "infraOrdre", "microOrdre" }, allowSetters = true)
    private Set<MicroOrdre> microOrdres = new HashSet<>();

    @OneToMany(mappedBy = "infraOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "microOrdres", "synonymes", "sousOrdre", "infraOrdre" }, allowSetters = true)
    private Set<InfraOrdre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraOrdres", "synonymes", "ordre", "sousOrdre" }, allowSetters = true)
    private SousOrdre sousOrdre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "microOrdres", "synonymes", "sousOrdre", "infraOrdre" }, allowSetters = true)
    private InfraOrdre infraOrdre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfraOrdre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public InfraOrdre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public InfraOrdre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<MicroOrdre> getMicroOrdres() {
        return this.microOrdres;
    }

    public void setMicroOrdres(Set<MicroOrdre> microOrdres) {
        if (this.microOrdres != null) {
            this.microOrdres.forEach(i -> i.setInfraOrdre(null));
        }
        if (microOrdres != null) {
            microOrdres.forEach(i -> i.setInfraOrdre(this));
        }
        this.microOrdres = microOrdres;
    }

    public InfraOrdre microOrdres(Set<MicroOrdre> microOrdres) {
        this.setMicroOrdres(microOrdres);
        return this;
    }

    public InfraOrdre addMicroOrdres(MicroOrdre microOrdre) {
        this.microOrdres.add(microOrdre);
        microOrdre.setInfraOrdre(this);
        return this;
    }

    public InfraOrdre removeMicroOrdres(MicroOrdre microOrdre) {
        this.microOrdres.remove(microOrdre);
        microOrdre.setInfraOrdre(null);
        return this;
    }

    public Set<InfraOrdre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<InfraOrdre> infraOrdres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setInfraOrdre(null));
        }
        if (infraOrdres != null) {
            infraOrdres.forEach(i -> i.setInfraOrdre(this));
        }
        this.synonymes = infraOrdres;
    }

    public InfraOrdre synonymes(Set<InfraOrdre> infraOrdres) {
        this.setSynonymes(infraOrdres);
        return this;
    }

    public InfraOrdre addSynonymes(InfraOrdre infraOrdre) {
        this.synonymes.add(infraOrdre);
        infraOrdre.setInfraOrdre(this);
        return this;
    }

    public InfraOrdre removeSynonymes(InfraOrdre infraOrdre) {
        this.synonymes.remove(infraOrdre);
        infraOrdre.setInfraOrdre(null);
        return this;
    }

    public SousOrdre getSousOrdre() {
        return this.sousOrdre;
    }

    public void setSousOrdre(SousOrdre sousOrdre) {
        this.sousOrdre = sousOrdre;
    }

    public InfraOrdre sousOrdre(SousOrdre sousOrdre) {
        this.setSousOrdre(sousOrdre);
        return this;
    }

    public InfraOrdre getInfraOrdre() {
        return this.infraOrdre;
    }

    public void setInfraOrdre(InfraOrdre infraOrdre) {
        this.infraOrdre = infraOrdre;
    }

    public InfraOrdre infraOrdre(InfraOrdre infraOrdre) {
        this.setInfraOrdre(infraOrdre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfraOrdre)) {
            return false;
        }
        return id != null && id.equals(((InfraOrdre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraOrdre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
