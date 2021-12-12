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
 * forma  dernier rang en mycologie
 */
@ApiModel(description = "forma  dernier rang en mycologie")
@Entity
@Table(name = "forme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forme implements Serializable {

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

    @OneToMany(mappedBy = "forme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "synonymes", "forme", "sousForme" }, allowSetters = true)
    private Set<SousForme> sousFormes = new HashSet<>();

    @OneToMany(mappedBy = "forme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousFormes", "synonymes", "sousVariete", "forme" }, allowSetters = true)
    private Set<Forme> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "formes", "synonymes", "variete", "sousVariete" }, allowSetters = true)
    private SousVariete sousVariete;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousFormes", "synonymes", "sousVariete", "forme" }, allowSetters = true)
    private Forme forme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Forme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Forme nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Forme nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousForme> getSousFormes() {
        return this.sousFormes;
    }

    public void setSousFormes(Set<SousForme> sousFormes) {
        if (this.sousFormes != null) {
            this.sousFormes.forEach(i -> i.setForme(null));
        }
        if (sousFormes != null) {
            sousFormes.forEach(i -> i.setForme(this));
        }
        this.sousFormes = sousFormes;
    }

    public Forme sousFormes(Set<SousForme> sousFormes) {
        this.setSousFormes(sousFormes);
        return this;
    }

    public Forme addSousFormes(SousForme sousForme) {
        this.sousFormes.add(sousForme);
        sousForme.setForme(this);
        return this;
    }

    public Forme removeSousFormes(SousForme sousForme) {
        this.sousFormes.remove(sousForme);
        sousForme.setForme(null);
        return this;
    }

    public Set<Forme> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Forme> formes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setForme(null));
        }
        if (formes != null) {
            formes.forEach(i -> i.setForme(this));
        }
        this.synonymes = formes;
    }

    public Forme synonymes(Set<Forme> formes) {
        this.setSynonymes(formes);
        return this;
    }

    public Forme addSynonymes(Forme forme) {
        this.synonymes.add(forme);
        forme.setForme(this);
        return this;
    }

    public Forme removeSynonymes(Forme forme) {
        this.synonymes.remove(forme);
        forme.setForme(null);
        return this;
    }

    public SousVariete getSousVariete() {
        return this.sousVariete;
    }

    public void setSousVariete(SousVariete sousVariete) {
        this.sousVariete = sousVariete;
    }

    public Forme sousVariete(SousVariete sousVariete) {
        this.setSousVariete(sousVariete);
        return this;
    }

    public Forme getForme() {
        return this.forme;
    }

    public void setForme(Forme forme) {
        this.forme = forme;
    }

    public Forme forme(Forme forme) {
        this.setForme(forme);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forme)) {
            return false;
        }
        return id != null && id.equals(((Forme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forme{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
