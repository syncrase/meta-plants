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
 * subvarietas  - sous-race étant un rang zoologique informel3
 */
@ApiModel(description = "subvarietas  - sous-race étant un rang zoologique informel3")
@Entity
@Table(name = "sous_variete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousVariete implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousVariete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousFormes", "synonymes", "sousVariete", "forme" }, allowSetters = true)
    private Set<Forme> formes = new HashSet<>();

    @OneToMany(mappedBy = "sousVariete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formes", "synonymes", "variete", "sousVariete" }, allowSetters = true)
    private Set<SousVariete> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousVarietes", "synonymes", "sousEspece", "variete" }, allowSetters = true)
    private Variete variete;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formes", "synonymes", "variete", "sousVariete" }, allowSetters = true)
    private SousVariete sousVariete;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousVariete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousVariete nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousVariete nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Forme> getFormes() {
        return this.formes;
    }

    public void setFormes(Set<Forme> formes) {
        if (this.formes != null) {
            this.formes.forEach(i -> i.setSousVariete(null));
        }
        if (formes != null) {
            formes.forEach(i -> i.setSousVariete(this));
        }
        this.formes = formes;
    }

    public SousVariete formes(Set<Forme> formes) {
        this.setFormes(formes);
        return this;
    }

    public SousVariete addFormes(Forme forme) {
        this.formes.add(forme);
        forme.setSousVariete(this);
        return this;
    }

    public SousVariete removeFormes(Forme forme) {
        this.formes.remove(forme);
        forme.setSousVariete(null);
        return this;
    }

    public Set<SousVariete> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousVariete> sousVarietes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousVariete(null));
        }
        if (sousVarietes != null) {
            sousVarietes.forEach(i -> i.setSousVariete(this));
        }
        this.synonymes = sousVarietes;
    }

    public SousVariete synonymes(Set<SousVariete> sousVarietes) {
        this.setSynonymes(sousVarietes);
        return this;
    }

    public SousVariete addSynonymes(SousVariete sousVariete) {
        this.synonymes.add(sousVariete);
        sousVariete.setSousVariete(this);
        return this;
    }

    public SousVariete removeSynonymes(SousVariete sousVariete) {
        this.synonymes.remove(sousVariete);
        sousVariete.setSousVariete(null);
        return this;
    }

    public Variete getVariete() {
        return this.variete;
    }

    public void setVariete(Variete variete) {
        this.variete = variete;
    }

    public SousVariete variete(Variete variete) {
        this.setVariete(variete);
        return this;
    }

    public SousVariete getSousVariete() {
        return this.sousVariete;
    }

    public void setSousVariete(SousVariete sousVariete) {
        this.sousVariete = sousVariete;
    }

    public SousVariete sousVariete(SousVariete sousVariete) {
        this.setSousVariete(sousVariete);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousVariete)) {
            return false;
        }
        return id != null && id.equals(((SousVariete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousVariete{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return variete;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return formes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
