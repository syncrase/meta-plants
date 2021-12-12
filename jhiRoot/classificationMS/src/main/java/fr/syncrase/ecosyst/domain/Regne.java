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
 * (Regnum)
 */
@ApiModel(description = "(Regnum)")
@Entity
@Table(name = "regne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Regne implements Serializable {

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

    @OneToMany(mappedBy = "regne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rameaus", "synonymes", "regne", "sousRegne" }, allowSetters = true)
    private Set<SousRegne> sousRegnes = new HashSet<>();

    @OneToMany(mappedBy = "regne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousRegnes", "synonymes", "superRegne", "regne" }, allowSetters = true)
    private Set<Regne> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "regnes", "synonymes", "superRegne" }, allowSetters = true)
    private SuperRegne superRegne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousRegnes", "synonymes", "superRegne", "regne" }, allowSetters = true)
    private Regne regne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Regne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Regne nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Regne nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousRegne> getSousRegnes() {
        return this.sousRegnes;
    }

    public void setSousRegnes(Set<SousRegne> sousRegnes) {
        if (this.sousRegnes != null) {
            this.sousRegnes.forEach(i -> i.setRegne(null));
        }
        if (sousRegnes != null) {
            sousRegnes.forEach(i -> i.setRegne(this));
        }
        this.sousRegnes = sousRegnes;
    }

    public Regne sousRegnes(Set<SousRegne> sousRegnes) {
        this.setSousRegnes(sousRegnes);
        return this;
    }

    public Regne addSousRegnes(SousRegne sousRegne) {
        this.sousRegnes.add(sousRegne);
        sousRegne.setRegne(this);
        return this;
    }

    public Regne removeSousRegnes(SousRegne sousRegne) {
        this.sousRegnes.remove(sousRegne);
        sousRegne.setRegne(null);
        return this;
    }

    public Set<Regne> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Regne> regnes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setRegne(null));
        }
        if (regnes != null) {
            regnes.forEach(i -> i.setRegne(this));
        }
        this.synonymes = regnes;
    }

    public Regne synonymes(Set<Regne> regnes) {
        this.setSynonymes(regnes);
        return this;
    }

    public Regne addSynonymes(Regne regne) {
        this.synonymes.add(regne);
        regne.setRegne(this);
        return this;
    }

    public Regne removeSynonymes(Regne regne) {
        this.synonymes.remove(regne);
        regne.setRegne(null);
        return this;
    }

    public SuperRegne getSuperRegne() {
        return this.superRegne;
    }

    public void setSuperRegne(SuperRegne superRegne) {
        this.superRegne = superRegne;
    }

    public Regne superRegne(SuperRegne superRegne) {
        this.setSuperRegne(superRegne);
        return this;
    }

    public Regne getRegne() {
        return this.regne;
    }

    public void setRegne(Regne regne) {
        this.regne = regne;
    }

    public Regne regne(Regne regne) {
        this.setRegne(regne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regne)) {
            return false;
        }
        return id != null && id.equals(((Regne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regne{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
