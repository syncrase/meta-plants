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
 * Subfamilia
 */
@ApiModel(description = "Subfamilia")
@Entity
@Table(name = "sous_famille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousFamille implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousFamille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousTribuses", "synonymes", "sousFamille", "tribu" }, allowSetters = true)
    private Set<Tribu> tribuses = new HashSet<>();

    @OneToMany(mappedBy = "sousFamille")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tribuses", "synonymes", "famille", "sousFamille" }, allowSetters = true)
    private Set<SousFamille> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousFamilles", "synonymes", "superFamille", "famille" }, allowSetters = true)
    private Famille famille;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tribuses", "synonymes", "famille", "sousFamille" }, allowSetters = true)
    private SousFamille sousFamille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousFamille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousFamille nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousFamille nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Tribu> getTribuses() {
        return this.tribuses;
    }

    public void setTribuses(Set<Tribu> tribus) {
        if (this.tribuses != null) {
            this.tribuses.forEach(i -> i.setSousFamille(null));
        }
        if (tribus != null) {
            tribus.forEach(i -> i.setSousFamille(this));
        }
        this.tribuses = tribus;
    }

    public SousFamille tribuses(Set<Tribu> tribus) {
        this.setTribuses(tribus);
        return this;
    }

    public SousFamille addTribus(Tribu tribu) {
        this.tribuses.add(tribu);
        tribu.setSousFamille(this);
        return this;
    }

    public SousFamille removeTribus(Tribu tribu) {
        this.tribuses.remove(tribu);
        tribu.setSousFamille(null);
        return this;
    }

    public Set<SousFamille> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousFamille> sousFamilles) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousFamille(null));
        }
        if (sousFamilles != null) {
            sousFamilles.forEach(i -> i.setSousFamille(this));
        }
        this.synonymes = sousFamilles;
    }

    public SousFamille synonymes(Set<SousFamille> sousFamilles) {
        this.setSynonymes(sousFamilles);
        return this;
    }

    public SousFamille addSynonymes(SousFamille sousFamille) {
        this.synonymes.add(sousFamille);
        sousFamille.setSousFamille(this);
        return this;
    }

    public SousFamille removeSynonymes(SousFamille sousFamille) {
        this.synonymes.remove(sousFamille);
        sousFamille.setSousFamille(null);
        return this;
    }

    public Famille getFamille() {
        return this.famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public SousFamille famille(Famille famille) {
        this.setFamille(famille);
        return this;
    }

    public SousFamille getSousFamille() {
        return this.sousFamille;
    }

    public void setSousFamille(SousFamille sousFamille) {
        this.sousFamille = sousFamille;
    }

    public SousFamille sousFamille(SousFamille sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousFamille)) {
            return false;
        }
        return id != null && id.equals(((SousFamille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousFamille{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return famille;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return tribuses;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
