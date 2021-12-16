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
 * Infraregnum
 */
@ApiModel(description = "Infraregnum")
@Entity
@Table(name = "infra_regne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfraRegne implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "infraRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "divisions", "synonymes", "infraRegne", "superDivision" }, allowSetters = true)
    private Set<SuperDivision> superDivisions = new HashSet<>();

    @OneToMany(mappedBy = "infraRegne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superDivisions", "synonymes", "rameau", "infraRegne" }, allowSetters = true)
    private Set<InfraRegne> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "infraRegnes", "synonymes", "sousRegne", "rameau" }, allowSetters = true)
    private Rameau rameau;

    @ManyToOne
    @JsonIgnoreProperties(value = { "superDivisions", "synonymes", "rameau", "infraRegne" }, allowSetters = true)
    private InfraRegne infraRegne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfraRegne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public InfraRegne nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public InfraRegne nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SuperDivision> getSuperDivisions() {
        return this.superDivisions;
    }

    public void setSuperDivisions(Set<SuperDivision> superDivisions) {
        if (this.superDivisions != null) {
            this.superDivisions.forEach(i -> i.setInfraRegne(null));
        }
        if (superDivisions != null) {
            superDivisions.forEach(i -> i.setInfraRegne(this));
        }
        this.superDivisions = superDivisions;
    }

    public InfraRegne superDivisions(Set<SuperDivision> superDivisions) {
        this.setSuperDivisions(superDivisions);
        return this;
    }

    public InfraRegne addSuperDivisions(SuperDivision superDivision) {
        this.superDivisions.add(superDivision);
        superDivision.setInfraRegne(this);
        return this;
    }

    public InfraRegne removeSuperDivisions(SuperDivision superDivision) {
        this.superDivisions.remove(superDivision);
        superDivision.setInfraRegne(null);
        return this;
    }

    public Set<InfraRegne> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<InfraRegne> infraRegnes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setInfraRegne(null));
        }
        if (infraRegnes != null) {
            infraRegnes.forEach(i -> i.setInfraRegne(this));
        }
        this.synonymes = infraRegnes;
    }

    public InfraRegne synonymes(Set<InfraRegne> infraRegnes) {
        this.setSynonymes(infraRegnes);
        return this;
    }

    public InfraRegne addSynonymes(InfraRegne infraRegne) {
        this.synonymes.add(infraRegne);
        infraRegne.setInfraRegne(this);
        return this;
    }

    public InfraRegne removeSynonymes(InfraRegne infraRegne) {
        this.synonymes.remove(infraRegne);
        infraRegne.setInfraRegne(null);
        return this;
    }

    public Rameau getRameau() {
        return this.rameau;
    }

    public void setRameau(Rameau rameau) {
        this.rameau = rameau;
    }

    public InfraRegne rameau(Rameau rameau) {
        this.setRameau(rameau);
        return this;
    }

    public InfraRegne getInfraRegne() {
        return this.infraRegne;
    }

    public void setInfraRegne(InfraRegne infraRegne) {
        this.infraRegne = infraRegne;
    }

    public InfraRegne infraRegne(InfraRegne infraRegne) {
        this.setInfraRegne(infraRegne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfraRegne)) {
            return false;
        }
        return id != null && id.equals(((InfraRegne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraRegne{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return rameau;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superDivisions;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
