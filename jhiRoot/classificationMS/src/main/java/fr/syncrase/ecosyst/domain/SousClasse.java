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
 * Subclassis
 */
@ApiModel(description = "Subclassis")
@Entity
@Table(name = "sous_classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousClasse implements Serializable {

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

    @OneToMany(mappedBy = "sousClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superOrdres", "synonymes", "sousClasse", "infraClasse" }, allowSetters = true)
    private Set<InfraClasse> infraClasses = new HashSet<>();

    @OneToMany(mappedBy = "sousClasse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraClasses", "synonymes", "classe", "sousClasse" }, allowSetters = true)
    private Set<SousClasse> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousClasses", "synonymes", "superClasse", "classe" }, allowSetters = true)
    private Classe classe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraClasses", "synonymes", "classe", "sousClasse" }, allowSetters = true)
    private SousClasse sousClasse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousClasse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousClasse nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousClasse nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<InfraClasse> getInfraClasses() {
        return this.infraClasses;
    }

    public void setInfraClasses(Set<InfraClasse> infraClasses) {
        if (this.infraClasses != null) {
            this.infraClasses.forEach(i -> i.setSousClasse(null));
        }
        if (infraClasses != null) {
            infraClasses.forEach(i -> i.setSousClasse(this));
        }
        this.infraClasses = infraClasses;
    }

    public SousClasse infraClasses(Set<InfraClasse> infraClasses) {
        this.setInfraClasses(infraClasses);
        return this;
    }

    public SousClasse addInfraClasses(InfraClasse infraClasse) {
        this.infraClasses.add(infraClasse);
        infraClasse.setSousClasse(this);
        return this;
    }

    public SousClasse removeInfraClasses(InfraClasse infraClasse) {
        this.infraClasses.remove(infraClasse);
        infraClasse.setSousClasse(null);
        return this;
    }

    public Set<SousClasse> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousClasse> sousClasses) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousClasse(null));
        }
        if (sousClasses != null) {
            sousClasses.forEach(i -> i.setSousClasse(this));
        }
        this.synonymes = sousClasses;
    }

    public SousClasse synonymes(Set<SousClasse> sousClasses) {
        this.setSynonymes(sousClasses);
        return this;
    }

    public SousClasse addSynonymes(SousClasse sousClasse) {
        this.synonymes.add(sousClasse);
        sousClasse.setSousClasse(this);
        return this;
    }

    public SousClasse removeSynonymes(SousClasse sousClasse) {
        this.synonymes.remove(sousClasse);
        sousClasse.setSousClasse(null);
        return this;
    }

    public Classe getClasse() {
        return this.classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public SousClasse classe(Classe classe) {
        this.setClasse(classe);
        return this;
    }

    public SousClasse getSousClasse() {
        return this.sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public SousClasse sousClasse(SousClasse sousClasse) {
        this.setSousClasse(sousClasse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousClasse)) {
            return false;
        }
        return id != null && id.equals(((SousClasse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousClasse{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
