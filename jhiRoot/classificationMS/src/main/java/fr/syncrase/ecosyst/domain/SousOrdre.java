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
 * Subordo
 */
@ApiModel(description = "Subordo")
@Entity
@Table(name = "sous_ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousOrdre implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "microOrdres", "synonymes", "sousOrdre", "infraOrdre" }, allowSetters = true)
    private Set<InfraOrdre> infraOrdres = new HashSet<>();

    @OneToMany(mappedBy = "sousOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraOrdres", "synonymes", "ordre", "sousOrdre" }, allowSetters = true)
    private Set<SousOrdre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousOrdres", "synonymes", "superOrdre", "ordre" }, allowSetters = true)
    private Ordre ordre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraOrdres", "synonymes", "ordre", "sousOrdre" }, allowSetters = true)
    private SousOrdre sousOrdre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousOrdre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousOrdre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousOrdre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<InfraOrdre> getInfraOrdres() {
        return this.infraOrdres;
    }

    public void setInfraOrdres(Set<InfraOrdre> infraOrdres) {
        if (this.infraOrdres != null) {
            this.infraOrdres.forEach(i -> i.setSousOrdre(null));
        }
        if (infraOrdres != null) {
            infraOrdres.forEach(i -> i.setSousOrdre(this));
        }
        this.infraOrdres = infraOrdres;
    }

    public SousOrdre infraOrdres(Set<InfraOrdre> infraOrdres) {
        this.setInfraOrdres(infraOrdres);
        return this;
    }

    public SousOrdre addInfraOrdres(InfraOrdre infraOrdre) {
        this.infraOrdres.add(infraOrdre);
        infraOrdre.setSousOrdre(this);
        return this;
    }

    public SousOrdre removeInfraOrdres(InfraOrdre infraOrdre) {
        this.infraOrdres.remove(infraOrdre);
        infraOrdre.setSousOrdre(null);
        return this;
    }

    public Set<SousOrdre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousOrdre> sousOrdres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousOrdre(null));
        }
        if (sousOrdres != null) {
            sousOrdres.forEach(i -> i.setSousOrdre(this));
        }
        this.synonymes = sousOrdres;
    }

    public SousOrdre synonymes(Set<SousOrdre> sousOrdres) {
        this.setSynonymes(sousOrdres);
        return this;
    }

    public SousOrdre addSynonymes(SousOrdre sousOrdre) {
        this.synonymes.add(sousOrdre);
        sousOrdre.setSousOrdre(this);
        return this;
    }

    public SousOrdre removeSynonymes(SousOrdre sousOrdre) {
        this.synonymes.remove(sousOrdre);
        sousOrdre.setSousOrdre(null);
        return this;
    }

    public Ordre getOrdre() {
        return this.ordre;
    }

    public void setOrdre(Ordre ordre) {
        this.ordre = ordre;
    }

    public SousOrdre ordre(Ordre ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public SousOrdre getSousOrdre() {
        return this.sousOrdre;
    }

    public void setSousOrdre(SousOrdre sousOrdre) {
        this.sousOrdre = sousOrdre;
    }

    public SousOrdre sousOrdre(SousOrdre sousOrdre) {
        this.setSousOrdre(sousOrdre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousOrdre)) {
            return false;
        }
        return id != null && id.equals(((SousOrdre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousOrdre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return ordre;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraOrdres;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
