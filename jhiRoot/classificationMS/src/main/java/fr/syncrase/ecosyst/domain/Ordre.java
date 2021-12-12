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
 * Ordo
 */
@ApiModel(description = "Ordo")
@Entity
@Table(name = "ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ordre implements Serializable {

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

    @OneToMany(mappedBy = "ordre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraOrdres", "synonymes", "ordre", "sousOrdre" }, allowSetters = true)
    private Set<SousOrdre> sousOrdres = new HashSet<>();

    @OneToMany(mappedBy = "ordre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousOrdres", "synonymes", "superOrdre", "ordre" }, allowSetters = true)
    private Set<Ordre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ordres", "synonymes", "infraClasse", "superOrdre" }, allowSetters = true)
    private SuperOrdre superOrdre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousOrdres", "synonymes", "superOrdre", "ordre" }, allowSetters = true)
    private Ordre ordre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ordre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Ordre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Ordre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousOrdre> getSousOrdres() {
        return this.sousOrdres;
    }

    public void setSousOrdres(Set<SousOrdre> sousOrdres) {
        if (this.sousOrdres != null) {
            this.sousOrdres.forEach(i -> i.setOrdre(null));
        }
        if (sousOrdres != null) {
            sousOrdres.forEach(i -> i.setOrdre(this));
        }
        this.sousOrdres = sousOrdres;
    }

    public Ordre sousOrdres(Set<SousOrdre> sousOrdres) {
        this.setSousOrdres(sousOrdres);
        return this;
    }

    public Ordre addSousOrdres(SousOrdre sousOrdre) {
        this.sousOrdres.add(sousOrdre);
        sousOrdre.setOrdre(this);
        return this;
    }

    public Ordre removeSousOrdres(SousOrdre sousOrdre) {
        this.sousOrdres.remove(sousOrdre);
        sousOrdre.setOrdre(null);
        return this;
    }

    public Set<Ordre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Ordre> ordres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setOrdre(null));
        }
        if (ordres != null) {
            ordres.forEach(i -> i.setOrdre(this));
        }
        this.synonymes = ordres;
    }

    public Ordre synonymes(Set<Ordre> ordres) {
        this.setSynonymes(ordres);
        return this;
    }

    public Ordre addSynonymes(Ordre ordre) {
        this.synonymes.add(ordre);
        ordre.setOrdre(this);
        return this;
    }

    public Ordre removeSynonymes(Ordre ordre) {
        this.synonymes.remove(ordre);
        ordre.setOrdre(null);
        return this;
    }

    public SuperOrdre getSuperOrdre() {
        return this.superOrdre;
    }

    public void setSuperOrdre(SuperOrdre superOrdre) {
        this.superOrdre = superOrdre;
    }

    public Ordre superOrdre(SuperOrdre superOrdre) {
        this.setSuperOrdre(superOrdre);
        return this;
    }

    public Ordre getOrdre() {
        return this.ordre;
    }

    public void setOrdre(Ordre ordre) {
        this.ordre = ordre;
    }

    public Ordre ordre(Ordre ordre) {
        this.setOrdre(ordre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ordre)) {
            return false;
        }
        return id != null && id.equals(((Ordre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ordre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
