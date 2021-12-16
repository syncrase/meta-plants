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
 * subspecies  - dernier rang zoologique officiel3
 */
@ApiModel(description = "subspecies  - dernier rang zoologique officiel3")
@Entity
@Table(name = "sous_espece")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousEspece implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousEspece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousVarietes", "synonymes", "sousEspece", "variete" }, allowSetters = true)
    private Set<Variete> varietes = new HashSet<>();

    @OneToMany(mappedBy = "sousEspece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "varietes", "synonymes", "espece", "sousEspece" }, allowSetters = true)
    private Set<SousEspece> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousEspeces", "synonymes", "sousSection", "espece" }, allowSetters = true)
    private Espece espece;

    @ManyToOne
    @JsonIgnoreProperties(value = { "varietes", "synonymes", "espece", "sousEspece" }, allowSetters = true)
    private SousEspece sousEspece;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousEspece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousEspece nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousEspece nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Variete> getVarietes() {
        return this.varietes;
    }

    public void setVarietes(Set<Variete> varietes) {
        if (this.varietes != null) {
            this.varietes.forEach(i -> i.setSousEspece(null));
        }
        if (varietes != null) {
            varietes.forEach(i -> i.setSousEspece(this));
        }
        this.varietes = varietes;
    }

    public SousEspece varietes(Set<Variete> varietes) {
        this.setVarietes(varietes);
        return this;
    }

    public SousEspece addVarietes(Variete variete) {
        this.varietes.add(variete);
        variete.setSousEspece(this);
        return this;
    }

    public SousEspece removeVarietes(Variete variete) {
        this.varietes.remove(variete);
        variete.setSousEspece(null);
        return this;
    }

    public Set<SousEspece> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousEspece> sousEspeces) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousEspece(null));
        }
        if (sousEspeces != null) {
            sousEspeces.forEach(i -> i.setSousEspece(this));
        }
        this.synonymes = sousEspeces;
    }

    public SousEspece synonymes(Set<SousEspece> sousEspeces) {
        this.setSynonymes(sousEspeces);
        return this;
    }

    public SousEspece addSynonymes(SousEspece sousEspece) {
        this.synonymes.add(sousEspece);
        sousEspece.setSousEspece(this);
        return this;
    }

    public SousEspece removeSynonymes(SousEspece sousEspece) {
        this.synonymes.remove(sousEspece);
        sousEspece.setSousEspece(null);
        return this;
    }

    public Espece getEspece() {
        return this.espece;
    }

    public void setEspece(Espece espece) {
        this.espece = espece;
    }

    public SousEspece espece(Espece espece) {
        this.setEspece(espece);
        return this;
    }

    public SousEspece getSousEspece() {
        return this.sousEspece;
    }

    public void setSousEspece(SousEspece sousEspece) {
        this.sousEspece = sousEspece;
    }

    public SousEspece sousEspece(SousEspece sousEspece) {
        this.setSousEspece(sousEspece);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousEspece)) {
            return false;
        }
        return id != null && id.equals(((SousEspece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousEspece{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return espece;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return varietes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
