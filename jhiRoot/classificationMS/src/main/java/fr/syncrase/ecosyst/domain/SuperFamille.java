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
 * Superfamilia
 */
@ApiModel(description = "Superfamilia")
@Entity
@Table(name = "super_famille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperFamille implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "superFamille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousFamilles", "synonymes", "superFamille", "famille" }, allowSetters = true)
    private Set<Famille> familles = new HashSet<>();

    @OneToMany(mappedBy = "superFamille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "familles", "synonymes", "microOrdre", "superFamille" }, allowSetters = true)
    private Set<SuperFamille> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "superFamilles", "synonymes", "infraOrdre", "microOrdre" }, allowSetters = true)
    private MicroOrdre microOrdre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "familles", "synonymes", "microOrdre", "superFamille" }, allowSetters = true)
    private SuperFamille superFamille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuperFamille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SuperFamille nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SuperFamille nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Famille> getFamilles() {
        return this.familles;
    }

    public void setFamilles(Set<Famille> familles) {
        if (this.familles != null) {
            this.familles.forEach(i -> i.setSuperFamille(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setSuperFamille(this));
        }
        this.familles = familles;
    }

    public SuperFamille familles(Set<Famille> familles) {
        this.setFamilles(familles);
        return this;
    }

    public SuperFamille addFamilles(Famille famille) {
        this.familles.add(famille);
        famille.setSuperFamille(this);
        return this;
    }

    public SuperFamille removeFamilles(Famille famille) {
        this.familles.remove(famille);
        famille.setSuperFamille(null);
        return this;
    }

    public Set<SuperFamille> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SuperFamille> superFamilles) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSuperFamille(null));
        }
        if (superFamilles != null) {
            superFamilles.forEach(i -> i.setSuperFamille(this));
        }
        this.synonymes = superFamilles;
    }

    public SuperFamille synonymes(Set<SuperFamille> superFamilles) {
        this.setSynonymes(superFamilles);
        return this;
    }

    public SuperFamille addSynonymes(SuperFamille superFamille) {
        this.synonymes.add(superFamille);
        superFamille.setSuperFamille(this);
        return this;
    }

    public SuperFamille removeSynonymes(SuperFamille superFamille) {
        this.synonymes.remove(superFamille);
        superFamille.setSuperFamille(null);
        return this;
    }

    public MicroOrdre getMicroOrdre() {
        return this.microOrdre;
    }

    public void setMicroOrdre(MicroOrdre microOrdre) {
        this.microOrdre = microOrdre;
    }

    public SuperFamille microOrdre(MicroOrdre microOrdre) {
        this.setMicroOrdre(microOrdre);
        return this;
    }

    public SuperFamille getSuperFamille() {
        return this.superFamille;
    }

    public void setSuperFamille(SuperFamille superFamille) {
        this.superFamille = superFamille;
    }

    public SuperFamille superFamille(SuperFamille superFamille) {
        this.setSuperFamille(superFamille);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperFamille)) {
            return false;
        }
        return id != null && id.equals(((SuperFamille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperFamille{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return microOrdre;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return familles;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
