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
 * Superclassis
 */
@ApiModel(description = "Superclassis")
@Entity
@Table(name = "super_classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperClasse implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "superClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousClasses", "synonymes", "superClasse", "classe" }, allowSetters = true)
    private Set<Classe> classes = new HashSet<>();

    @OneToMany(mappedBy = "superClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classes", "synonymes", "microEmbranchement", "superClasse" }, allowSetters = true)
    private Set<SuperClasse> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "superClasses", "synonymes", "infraEmbranchement", "microEmbranchement" }, allowSetters = true)
    private MicroEmbranchement microEmbranchement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "synonymes", "microEmbranchement", "superClasse" }, allowSetters = true)
    private SuperClasse superClasse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuperClasse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SuperClasse nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SuperClasse nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setSuperClasse(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setSuperClasse(this));
        }
        this.classes = classes;
    }

    public SuperClasse classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public SuperClasse addClasses(Classe classe) {
        this.classes.add(classe);
        classe.setSuperClasse(this);
        return this;
    }

    public SuperClasse removeClasses(Classe classe) {
        this.classes.remove(classe);
        classe.setSuperClasse(null);
        return this;
    }

    public Set<SuperClasse> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SuperClasse> superClasses) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSuperClasse(null));
        }
        if (superClasses != null) {
            superClasses.forEach(i -> i.setSuperClasse(this));
        }
        this.synonymes = superClasses;
    }

    public SuperClasse synonymes(Set<SuperClasse> superClasses) {
        this.setSynonymes(superClasses);
        return this;
    }

    public SuperClasse addSynonymes(SuperClasse superClasse) {
        this.synonymes.add(superClasse);
        superClasse.setSuperClasse(this);
        return this;
    }

    public SuperClasse removeSynonymes(SuperClasse superClasse) {
        this.synonymes.remove(superClasse);
        superClasse.setSuperClasse(null);
        return this;
    }

    public MicroEmbranchement getMicroEmbranchement() {
        return this.microEmbranchement;
    }

    public void setMicroEmbranchement(MicroEmbranchement microEmbranchement) {
        this.microEmbranchement = microEmbranchement;
    }

    public SuperClasse microEmbranchement(MicroEmbranchement microEmbranchement) {
        this.setMicroEmbranchement(microEmbranchement);
        return this;
    }

    public SuperClasse getSuperClasse() {
        return this.superClasse;
    }

    public void setSuperClasse(SuperClasse superClasse) {
        this.superClasse = superClasse;
    }

    public SuperClasse superClasse(SuperClasse superClasse) {
        this.setSuperClasse(superClasse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperClasse)) {
            return false;
        }
        return id != null && id.equals(((SuperClasse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperClasse{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return microEmbranchement;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return classes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
