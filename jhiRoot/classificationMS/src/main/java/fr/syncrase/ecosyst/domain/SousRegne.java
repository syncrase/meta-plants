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
 * Subregnum
 */
@ApiModel(description = "Subregnum")
@Entity
@Table(name = "sous_regne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousRegne implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraRegnes", "synonymes", "sousRegne", "rameau" }, allowSetters = true)
    private Set<Rameau> rameaus = new HashSet<>();

    @OneToMany(mappedBy = "sousRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rameaus", "synonymes", "regne", "sousRegne" }, allowSetters = true)
    private Set<SousRegne> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousRegnes", "synonymes", "superRegne", "regne" }, allowSetters = true)
    private Regne regne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rameaus", "synonymes", "regne", "sousRegne" }, allowSetters = true)
    private SousRegne sousRegne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousRegne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousRegne nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousRegne nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Rameau> getRameaus() {
        return this.rameaus;
    }

    public void setRameaus(Set<Rameau> rameaus) {
        if (this.rameaus != null) {
            this.rameaus.forEach(i -> i.setSousRegne(null));
        }
        if (rameaus != null) {
            rameaus.forEach(i -> i.setSousRegne(this));
        }
        this.rameaus = rameaus;
    }

    public SousRegne rameaus(Set<Rameau> rameaus) {
        this.setRameaus(rameaus);
        return this;
    }

    public SousRegne addRameaus(Rameau rameau) {
        this.rameaus.add(rameau);
        rameau.setSousRegne(this);
        return this;
    }

    public SousRegne removeRameaus(Rameau rameau) {
        this.rameaus.remove(rameau);
        rameau.setSousRegne(null);
        return this;
    }

    public Set<SousRegne> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousRegne> sousRegnes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousRegne(null));
        }
        if (sousRegnes != null) {
            sousRegnes.forEach(i -> i.setSousRegne(this));
        }
        this.synonymes = sousRegnes;
    }

    public SousRegne synonymes(Set<SousRegne> sousRegnes) {
        this.setSynonymes(sousRegnes);
        return this;
    }

    public SousRegne addSynonymes(SousRegne sousRegne) {
        this.synonymes.add(sousRegne);
        sousRegne.setSousRegne(this);
        return this;
    }

    public SousRegne removeSynonymes(SousRegne sousRegne) {
        this.synonymes.remove(sousRegne);
        sousRegne.setSousRegne(null);
        return this;
    }

    public Regne getRegne() {
        return this.regne;
    }

    public void setRegne(Regne regne) {
        this.regne = regne;
    }

    public SousRegne regne(Regne regne) {
        this.setRegne(regne);
        return this;
    }

    public SousRegne getSousRegne() {
        return this.sousRegne;
    }

    public void setSousRegne(SousRegne sousRegne) {
        this.sousRegne = sousRegne;
    }

    public SousRegne sousRegne(SousRegne sousRegne) {
        this.setSousRegne(sousRegne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousRegne)) {
            return false;
        }
        return id != null && id.equals(((SousRegne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousRegne{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return regne;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return rameaus;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
