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

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Classis
 */
@ApiModel(description = "Classis")
@Entity
@Table(name = "classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classe implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "classe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraClasses", "synonymes", "classe", "sousClasse" }, allowSetters = true)
    private Set<SousClasse> sousClasses = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousClasses", "synonymes", "superClasse", "classe" }, allowSetters = true)
    private Set<Classe> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "synonymes", "microEmbranchement", "superClasse" }, allowSetters = true)
    private SuperClasse superClasse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousClasses", "synonymes", "superClasse", "classe" }, allowSetters = true)
    private Classe classe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Classe nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Classe nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousClasse> getSousClasses() {
        return this.sousClasses;
    }

    public void setSousClasses(Set<SousClasse> sousClasses) {
        if (this.sousClasses != null) {
            this.sousClasses.forEach(i -> i.setClasse(null));
        }
        if (sousClasses != null) {
            sousClasses.forEach(i -> i.setClasse(this));
        }
        this.sousClasses = sousClasses;
    }

    public Classe sousClasses(Set<SousClasse> sousClasses) {
        this.setSousClasses(sousClasses);
        return this;
    }

    public Classe addSousClasses(SousClasse sousClasse) {
        this.sousClasses.add(sousClasse);
        sousClasse.setClasse(this);
        return this;
    }

    public Classe removeSousClasses(SousClasse sousClasse) {
        this.sousClasses.remove(sousClasse);
        sousClasse.setClasse(null);
        return this;
    }

    public Set<Classe> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Classe> classes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setClasse(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setClasse(this));
        }
        this.synonymes = classes;
    }

    public Classe synonymes(Set<Classe> classes) {
        this.setSynonymes(classes);
        return this;
    }

    public Classe addSynonymes(Classe classe) {
        this.synonymes.add(classe);
        classe.setClasse(this);
        return this;
    }

    public Classe removeSynonymes(Classe classe) {
        this.synonymes.remove(classe);
        classe.setClasse(null);
        return this;
    }

    public SuperClasse getSuperClasse() {
        return this.superClasse;
    }

    public void setSuperClasse(SuperClasse superClasse) {
        this.superClasse = superClasse;
    }

    public Classe superClasse(SuperClasse superClasse) {
        this.setSuperClasse(superClasse);
        return this;
    }

    public Classe getClasse() {
        return this.classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Classe classe(Classe classe) {
        this.setClasse(classe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classe)) {
            return false;
        }
        return id != null && id.equals(((Classe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classe{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return superClasse;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousClasses;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        throw new NotImplementedException("Cette méthode ne doit pas être utilisée!");
    }
}
