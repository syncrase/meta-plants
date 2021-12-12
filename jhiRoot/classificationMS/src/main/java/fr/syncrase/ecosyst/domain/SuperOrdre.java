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
 * Superordo
 */
@ApiModel(description = "Superordo")
@Entity
@Table(name = "super_ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperOrdre implements Serializable {

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

    @OneToMany(mappedBy = "superOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousOrdres", "synonymes", "superOrdre", "ordre" }, allowSetters = true)
    private Set<Ordre> ordres = new HashSet<>();

    @OneToMany(mappedBy = "superOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordres", "synonymes", "infraClasse", "superOrdre" }, allowSetters = true)
    private Set<SuperOrdre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "superOrdres", "synonymes", "sousClasse", "infraClasse" }, allowSetters = true)
    private InfraClasse infraClasse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ordres", "synonymes", "infraClasse", "superOrdre" }, allowSetters = true)
    private SuperOrdre superOrdre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuperOrdre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SuperOrdre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SuperOrdre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Ordre> getOrdres() {
        return this.ordres;
    }

    public void setOrdres(Set<Ordre> ordres) {
        if (this.ordres != null) {
            this.ordres.forEach(i -> i.setSuperOrdre(null));
        }
        if (ordres != null) {
            ordres.forEach(i -> i.setSuperOrdre(this));
        }
        this.ordres = ordres;
    }

    public SuperOrdre ordres(Set<Ordre> ordres) {
        this.setOrdres(ordres);
        return this;
    }

    public SuperOrdre addOrdres(Ordre ordre) {
        this.ordres.add(ordre);
        ordre.setSuperOrdre(this);
        return this;
    }

    public SuperOrdre removeOrdres(Ordre ordre) {
        this.ordres.remove(ordre);
        ordre.setSuperOrdre(null);
        return this;
    }

    public Set<SuperOrdre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SuperOrdre> superOrdres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSuperOrdre(null));
        }
        if (superOrdres != null) {
            superOrdres.forEach(i -> i.setSuperOrdre(this));
        }
        this.synonymes = superOrdres;
    }

    public SuperOrdre synonymes(Set<SuperOrdre> superOrdres) {
        this.setSynonymes(superOrdres);
        return this;
    }

    public SuperOrdre addSynonymes(SuperOrdre superOrdre) {
        this.synonymes.add(superOrdre);
        superOrdre.setSuperOrdre(this);
        return this;
    }

    public SuperOrdre removeSynonymes(SuperOrdre superOrdre) {
        this.synonymes.remove(superOrdre);
        superOrdre.setSuperOrdre(null);
        return this;
    }

    public InfraClasse getInfraClasse() {
        return this.infraClasse;
    }

    public void setInfraClasse(InfraClasse infraClasse) {
        this.infraClasse = infraClasse;
    }

    public SuperOrdre infraClasse(InfraClasse infraClasse) {
        this.setInfraClasse(infraClasse);
        return this;
    }

    public SuperOrdre getSuperOrdre() {
        return this.superOrdre;
    }

    public void setSuperOrdre(SuperOrdre superOrdre) {
        this.superOrdre = superOrdre;
    }

    public SuperOrdre superOrdre(SuperOrdre superOrdre) {
        this.setSuperOrdre(superOrdre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperOrdre)) {
            return false;
        }
        return id != null && id.equals(((SuperOrdre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperOrdre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
