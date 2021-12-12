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
 * varietas  - race étant un rang zoologique informel3
 */
@ApiModel(description = "varietas  - race étant un rang zoologique informel3")
@Entity
@Table(name = "variete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Variete implements Serializable {

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

    @OneToMany(mappedBy = "variete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formes", "synonymes", "variete", "sousVariete" }, allowSetters = true)
    private Set<SousVariete> sousVarietes = new HashSet<>();

    @OneToMany(mappedBy = "variete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousVarietes", "synonymes", "sousEspece", "variete" }, allowSetters = true)
    private Set<Variete> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "varietes", "synonymes", "espece", "sousEspece" }, allowSetters = true)
    private SousEspece sousEspece;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousVarietes", "synonymes", "sousEspece", "variete" }, allowSetters = true)
    private Variete variete;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Variete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Variete nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Variete nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousVariete> getSousVarietes() {
        return this.sousVarietes;
    }

    public void setSousVarietes(Set<SousVariete> sousVarietes) {
        if (this.sousVarietes != null) {
            this.sousVarietes.forEach(i -> i.setVariete(null));
        }
        if (sousVarietes != null) {
            sousVarietes.forEach(i -> i.setVariete(this));
        }
        this.sousVarietes = sousVarietes;
    }

    public Variete sousVarietes(Set<SousVariete> sousVarietes) {
        this.setSousVarietes(sousVarietes);
        return this;
    }

    public Variete addSousVarietes(SousVariete sousVariete) {
        this.sousVarietes.add(sousVariete);
        sousVariete.setVariete(this);
        return this;
    }

    public Variete removeSousVarietes(SousVariete sousVariete) {
        this.sousVarietes.remove(sousVariete);
        sousVariete.setVariete(null);
        return this;
    }

    public Set<Variete> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Variete> varietes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setVariete(null));
        }
        if (varietes != null) {
            varietes.forEach(i -> i.setVariete(this));
        }
        this.synonymes = varietes;
    }

    public Variete synonymes(Set<Variete> varietes) {
        this.setSynonymes(varietes);
        return this;
    }

    public Variete addSynonymes(Variete variete) {
        this.synonymes.add(variete);
        variete.setVariete(this);
        return this;
    }

    public Variete removeSynonymes(Variete variete) {
        this.synonymes.remove(variete);
        variete.setVariete(null);
        return this;
    }

    public SousEspece getSousEspece() {
        return this.sousEspece;
    }

    public void setSousEspece(SousEspece sousEspece) {
        this.sousEspece = sousEspece;
    }

    public Variete sousEspece(SousEspece sousEspece) {
        this.setSousEspece(sousEspece);
        return this;
    }

    public Variete getVariete() {
        return this.variete;
    }

    public void setVariete(Variete variete) {
        this.variete = variete;
    }

    public Variete variete(Variete variete) {
        this.setVariete(variete);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variete)) {
            return false;
        }
        return id != null && id.equals(((Variete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Variete{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
