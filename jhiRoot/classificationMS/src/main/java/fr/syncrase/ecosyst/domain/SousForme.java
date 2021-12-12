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
 * subforma
 */
@ApiModel(description = "subforma")
@Entity
@Table(name = "sous_forme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousForme implements Serializable {

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

    @OneToMany(mappedBy = "sousForme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "synonymes", "forme", "sousForme" }, allowSetters = true)
    private Set<SousForme> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousFormes", "synonymes", "sousVariete", "forme" }, allowSetters = true)
    private Forme forme;

    @ManyToOne
    @JsonIgnoreProperties(value = { "synonymes", "forme", "sousForme" }, allowSetters = true)
    private SousForme sousForme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousForme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousForme nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousForme nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousForme> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousForme> sousFormes) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousForme(null));
        }
        if (sousFormes != null) {
            sousFormes.forEach(i -> i.setSousForme(this));
        }
        this.synonymes = sousFormes;
    }

    public SousForme synonymes(Set<SousForme> sousFormes) {
        this.setSynonymes(sousFormes);
        return this;
    }

    public SousForme addSynonymes(SousForme sousForme) {
        this.synonymes.add(sousForme);
        sousForme.setSousForme(this);
        return this;
    }

    public SousForme removeSynonymes(SousForme sousForme) {
        this.synonymes.remove(sousForme);
        sousForme.setSousForme(null);
        return this;
    }

    public Forme getForme() {
        return this.forme;
    }

    public void setForme(Forme forme) {
        this.forme = forme;
    }

    public SousForme forme(Forme forme) {
        this.setForme(forme);
        return this;
    }

    public SousForme getSousForme() {
        return this.sousForme;
    }

    public void setSousForme(SousForme sousForme) {
        this.sousForme = sousForme;
    }

    public SousForme sousForme(SousForme sousForme) {
        this.setSousForme(sousForme);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousForme)) {
            return false;
        }
        return id != null && id.equals(((SousForme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousForme{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
