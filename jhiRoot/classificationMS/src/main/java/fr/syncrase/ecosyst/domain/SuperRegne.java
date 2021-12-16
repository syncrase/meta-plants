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
 * Super-règne, Empire, Domaine (Superregnum, Imperium, Dominium)
 */
@ApiModel(description = "Super-règne, Empire, Domaine (Superregnum, Imperium, Dominium)")
@Entity
@Table(name = "super_regne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperRegne implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "superRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousRegnes", "synonymes", "superRegne", "regne" }, allowSetters = true)
    private Set<Regne> regnes = new HashSet<>();

    @OneToMany(mappedBy = "superRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "regnes", "synonymes", "superRegne" }, allowSetters = true)
    private Set<SuperRegne> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "regnes", "synonymes", "superRegne" }, allowSetters = true)
    private SuperRegne superRegne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuperRegne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SuperRegne nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SuperRegne nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Regne> getRegnes() {
        return this.regnes;
    }

    public void setRegnes(Set<Regne> regnes) {
        if (this.regnes != null) {
            this.regnes.forEach(i -> i.setSuperRegne(null));
        }
        if (regnes != null) {
            regnes.forEach(i -> i.setSuperRegne(this));
        }
        this.regnes = regnes;
    }

    public SuperRegne regnes(Set<Regne> regnes) {
        this.setRegnes(regnes);
        return this;
    }

    public SuperRegne addRegnes(Regne regne) {
        this.regnes.add(regne);
        regne.setSuperRegne(this);
        return this;
    }

    public SuperRegne removeRegnes(Regne regne) {
        this.regnes.remove(regne);
        regne.setSuperRegne(null);
        return this;
    }

    public Set<SuperRegne> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SuperRegne> superRegnes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSuperRegne(null));
        }
        if (superRegnes != null) {
            superRegnes.forEach(i -> i.setSuperRegne(this));
        }
        this.synonymes = superRegnes;
    }

    public SuperRegne synonymes(Set<SuperRegne> superRegnes) {
        this.setSynonymes(superRegnes);
        return this;
    }

    public SuperRegne addSynonymes(SuperRegne superRegne) {
        this.synonymes.add(superRegne);
        superRegne.setSuperRegne(this);
        return this;
    }

    public SuperRegne removeSynonymes(SuperRegne superRegne) {
        this.synonymes.remove(superRegne);
        superRegne.setSuperRegne(null);
        return this;
    }

    public SuperRegne getSuperRegne() {
        return this.superRegne;
    }

    public void setSuperRegne(SuperRegne superRegne) {
        this.superRegne = superRegne;
    }

    public SuperRegne superRegne(SuperRegne superRegne) {
        this.setSuperRegne(superRegne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperRegne)) {
            return false;
        }
        return id != null && id.equals(((SuperRegne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperRegne{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return null;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return regnes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
