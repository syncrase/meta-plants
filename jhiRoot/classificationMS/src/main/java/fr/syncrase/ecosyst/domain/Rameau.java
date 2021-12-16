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
 * Ramus, « branch » en anglais
 */
@ApiModel(description = "Ramus, « branch » en anglais")
@Entity
@Table(name = "rameau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rameau implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "rameau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superDivisions", "synonymes", "rameau", "infraRegne" }, allowSetters = true)
    private Set<InfraRegne> infraRegnes = new HashSet<>();

    @OneToMany(mappedBy = "rameau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "infraRegnes", "synonymes", "sousRegne", "rameau" }, allowSetters = true)
    private Set<Rameau> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "rameaus", "synonymes", "regne", "sousRegne" }, allowSetters = true)
    private SousRegne sousRegne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraRegnes", "synonymes", "sousRegne", "rameau" }, allowSetters = true)
    private Rameau rameau;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rameau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Rameau nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Rameau nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<InfraRegne> getInfraRegnes() {
        return this.infraRegnes;
    }

    public void setInfraRegnes(Set<InfraRegne> infraRegnes) {
        if (this.infraRegnes != null) {
            this.infraRegnes.forEach(i -> i.setRameau(null));
        }
        if (infraRegnes != null) {
            infraRegnes.forEach(i -> i.setRameau(this));
        }
        this.infraRegnes = infraRegnes;
    }

    public Rameau infraRegnes(Set<InfraRegne> infraRegnes) {
        this.setInfraRegnes(infraRegnes);
        return this;
    }

    public Rameau addInfraRegnes(InfraRegne infraRegne) {
        this.infraRegnes.add(infraRegne);
        infraRegne.setRameau(this);
        return this;
    }

    public Rameau removeInfraRegnes(InfraRegne infraRegne) {
        this.infraRegnes.remove(infraRegne);
        infraRegne.setRameau(null);
        return this;
    }

    public Set<Rameau> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Rameau> rameaus) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setRameau(null));
        }
        if (rameaus != null) {
            rameaus.forEach(i -> i.setRameau(this));
        }
        this.synonymes = rameaus;
    }

    public Rameau synonymes(Set<Rameau> rameaus) {
        this.setSynonymes(rameaus);
        return this;
    }

    public Rameau addSynonymes(Rameau rameau) {
        this.synonymes.add(rameau);
        rameau.setRameau(this);
        return this;
    }

    public Rameau removeSynonymes(Rameau rameau) {
        this.synonymes.remove(rameau);
        rameau.setRameau(null);
        return this;
    }

    public SousRegne getSousRegne() {
        return this.sousRegne;
    }

    public void setSousRegne(SousRegne sousRegne) {
        this.sousRegne = sousRegne;
    }

    public Rameau sousRegne(SousRegne sousRegne) {
        this.setSousRegne(sousRegne);
        return this;
    }

    public Rameau getRameau() {
        return this.rameau;
    }

    public void setRameau(Rameau rameau) {
        this.rameau = rameau;
    }

    public Rameau rameau(Rameau rameau) {
        this.setRameau(rameau);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rameau)) {
            return false;
        }
        return id != null && id.equals(((Rameau) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rameau{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return sousRegne;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraRegnes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
