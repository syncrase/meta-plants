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
 * Microordo
 */
@ApiModel(description = "Microordo")
@Entity
@Table(name = "micro_ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MicroOrdre implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "microOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "familles", "synonymes", "microOrdre", "superFamille" }, allowSetters = true)
    private Set<SuperFamille> superFamilles = new HashSet<>();

    @OneToMany(mappedBy = "microOrdre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "superFamilles", "synonymes", "infraOrdre", "microOrdre" }, allowSetters = true)
    private Set<MicroOrdre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "microOrdres", "synonymes", "sousOrdre", "infraOrdre" }, allowSetters = true)
    private InfraOrdre infraOrdre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "superFamilles", "synonymes", "infraOrdre", "microOrdre" }, allowSetters = true)
    private MicroOrdre microOrdre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MicroOrdre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public MicroOrdre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public MicroOrdre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SuperFamille> getSuperFamilles() {
        return this.superFamilles;
    }

    public void setSuperFamilles(Set<SuperFamille> superFamilles) {
        if (this.superFamilles != null) {
            this.superFamilles.forEach(i -> i.setMicroOrdre(null));
        }
        if (superFamilles != null) {
            superFamilles.forEach(i -> i.setMicroOrdre(this));
        }
        this.superFamilles = superFamilles;
    }

    public MicroOrdre superFamilles(Set<SuperFamille> superFamilles) {
        this.setSuperFamilles(superFamilles);
        return this;
    }

    public MicroOrdre addSuperFamilles(SuperFamille superFamille) {
        this.superFamilles.add(superFamille);
        superFamille.setMicroOrdre(this);
        return this;
    }

    public MicroOrdre removeSuperFamilles(SuperFamille superFamille) {
        this.superFamilles.remove(superFamille);
        superFamille.setMicroOrdre(null);
        return this;
    }

    public Set<MicroOrdre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<MicroOrdre> microOrdres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setMicroOrdre(null));
        }
        if (microOrdres != null) {
            microOrdres.forEach(i -> i.setMicroOrdre(this));
        }
        this.synonymes = microOrdres;
    }

    public MicroOrdre synonymes(Set<MicroOrdre> microOrdres) {
        this.setSynonymes(microOrdres);
        return this;
    }

    public MicroOrdre addSynonymes(MicroOrdre microOrdre) {
        this.synonymes.add(microOrdre);
        microOrdre.setMicroOrdre(this);
        return this;
    }

    public MicroOrdre removeSynonymes(MicroOrdre microOrdre) {
        this.synonymes.remove(microOrdre);
        microOrdre.setMicroOrdre(null);
        return this;
    }

    public InfraOrdre getInfraOrdre() {
        return this.infraOrdre;
    }

    public void setInfraOrdre(InfraOrdre infraOrdre) {
        this.infraOrdre = infraOrdre;
    }

    public MicroOrdre infraOrdre(InfraOrdre infraOrdre) {
        this.setInfraOrdre(infraOrdre);
        return this;
    }

    public MicroOrdre getMicroOrdre() {
        return this.microOrdre;
    }

    public void setMicroOrdre(MicroOrdre microOrdre) {
        this.microOrdre = microOrdre;
    }

    public MicroOrdre microOrdre(MicroOrdre microOrdre) {
        this.setMicroOrdre(microOrdre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MicroOrdre)) {
            return false;
        }
        return id != null && id.equals(((MicroOrdre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MicroOrdre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return infraOrdre;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superFamilles;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
