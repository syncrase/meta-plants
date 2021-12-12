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
 * Subgenus
 */
@ApiModel(description = "Subgenus")
@Entity
@Table(name = "sous_genre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousGenre implements Serializable {

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

    @OneToMany(mappedBy = "sousGenre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousSections", "synonymes", "sousGenre", "section" }, allowSetters = true)
    private Set<Section> sections = new HashSet<>();

    @OneToMany(mappedBy = "sousGenre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "synonymes", "genre", "sousGenre" }, allowSetters = true)
    private Set<SousGenre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousGenres", "synonymes", "sousTribu", "genre" }, allowSetters = true)
    private Genre genre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sections", "synonymes", "genre", "sousGenre" }, allowSetters = true)
    private SousGenre sousGenre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousGenre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousGenre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousGenre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Section> getSections() {
        return this.sections;
    }

    public void setSections(Set<Section> sections) {
        if (this.sections != null) {
            this.sections.forEach(i -> i.setSousGenre(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setSousGenre(this));
        }
        this.sections = sections;
    }

    public SousGenre sections(Set<Section> sections) {
        this.setSections(sections);
        return this;
    }

    public SousGenre addSections(Section section) {
        this.sections.add(section);
        section.setSousGenre(this);
        return this;
    }

    public SousGenre removeSections(Section section) {
        this.sections.remove(section);
        section.setSousGenre(null);
        return this;
    }

    public Set<SousGenre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousGenre> sousGenres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousGenre(null));
        }
        if (sousGenres != null) {
            sousGenres.forEach(i -> i.setSousGenre(this));
        }
        this.synonymes = sousGenres;
    }

    public SousGenre synonymes(Set<SousGenre> sousGenres) {
        this.setSynonymes(sousGenres);
        return this;
    }

    public SousGenre addSynonymes(SousGenre sousGenre) {
        this.synonymes.add(sousGenre);
        sousGenre.setSousGenre(this);
        return this;
    }

    public SousGenre removeSynonymes(SousGenre sousGenre) {
        this.synonymes.remove(sousGenre);
        sousGenre.setSousGenre(null);
        return this;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public SousGenre genre(Genre genre) {
        this.setGenre(genre);
        return this;
    }

    public SousGenre getSousGenre() {
        return this.sousGenre;
    }

    public void setSousGenre(SousGenre sousGenre) {
        this.sousGenre = sousGenre;
    }

    public SousGenre sousGenre(SousGenre sousGenre) {
        this.setSousGenre(sousGenre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousGenre)) {
            return false;
        }
        return id != null && id.equals(((SousGenre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousGenre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
