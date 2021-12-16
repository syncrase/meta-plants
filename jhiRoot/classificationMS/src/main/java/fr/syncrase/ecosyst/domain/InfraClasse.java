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
 * Infraclassis
 */
@ApiModel(description = "Infraclassis")
@Entity
@Table(name = "infra_classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfraClasse implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "infraClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordres", "synonymes", "infraClasse", "superOrdre" }, allowSetters = true)
    private Set<SuperOrdre> superOrdres = new HashSet<>();

    @OneToMany(mappedBy = "infraClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superOrdres", "synonymes", "sousClasse", "infraClasse" }, allowSetters = true)
    private Set<InfraClasse> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraClasses", "synonymes", "classe", "sousClasse" }, allowSetters = true)
    private SousClasse sousClasse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "superOrdres", "synonymes", "sousClasse", "infraClasse" }, allowSetters = true)
    private InfraClasse infraClasse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfraClasse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public InfraClasse nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public InfraClasse nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SuperOrdre> getSuperOrdres() {
        return this.superOrdres;
    }

    public void setSuperOrdres(Set<SuperOrdre> superOrdres) {
        if (this.superOrdres != null) {
            this.superOrdres.forEach(i -> i.setInfraClasse(null));
        }
        if (superOrdres != null) {
            superOrdres.forEach(i -> i.setInfraClasse(this));
        }
        this.superOrdres = superOrdres;
    }

    public InfraClasse superOrdres(Set<SuperOrdre> superOrdres) {
        this.setSuperOrdres(superOrdres);
        return this;
    }

    public InfraClasse addSuperOrdres(SuperOrdre superOrdre) {
        this.superOrdres.add(superOrdre);
        superOrdre.setInfraClasse(this);
        return this;
    }

    public InfraClasse removeSuperOrdres(SuperOrdre superOrdre) {
        this.superOrdres.remove(superOrdre);
        superOrdre.setInfraClasse(null);
        return this;
    }

    public Set<InfraClasse> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<InfraClasse> infraClasses) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setInfraClasse(null));
        }
        if (infraClasses != null) {
            infraClasses.forEach(i -> i.setInfraClasse(this));
        }
        this.synonymes = infraClasses;
    }

    public InfraClasse synonymes(Set<InfraClasse> infraClasses) {
        this.setSynonymes(infraClasses);
        return this;
    }

    public InfraClasse addSynonymes(InfraClasse infraClasse) {
        this.synonymes.add(infraClasse);
        infraClasse.setInfraClasse(this);
        return this;
    }

    public InfraClasse removeSynonymes(InfraClasse infraClasse) {
        this.synonymes.remove(infraClasse);
        infraClasse.setInfraClasse(null);
        return this;
    }

    public SousClasse getSousClasse() {
        return this.sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public InfraClasse sousClasse(SousClasse sousClasse) {
        this.setSousClasse(sousClasse);
        return this;
    }

    public InfraClasse getInfraClasse() {
        return this.infraClasse;
    }

    public void setInfraClasse(InfraClasse infraClasse) {
        this.infraClasse = infraClasse;
    }

    public InfraClasse infraClasse(InfraClasse infraClasse) {
        this.setInfraClasse(infraClasse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfraClasse)) {
            return false;
        }
        return id != null && id.equals(((InfraClasse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraClasse{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return sousClasse;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superOrdres;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
