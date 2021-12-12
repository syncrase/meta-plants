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
 * Sectio
 */
@ApiModel(description = "Sectio")
@Entity
@Table(name = "section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Section implements Serializable {

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

    @OneToMany(mappedBy = "section")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "especes", "synonymes", "section", "sousSection" }, allowSetters = true)
    private Set<SousSection> sousSections = new HashSet<>();

    @OneToMany(mappedBy = "section")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousSections", "synonymes", "sousGenre", "section" }, allowSetters = true)
    private Set<Section> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sections", "synonymes", "genre", "sousGenre" }, allowSetters = true)
    private SousGenre sousGenre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousSections", "synonymes", "sousGenre", "section" }, allowSetters = true)
    private Section section;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Section id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Section nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Section nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousSection> getSousSections() {
        return this.sousSections;
    }

    public void setSousSections(Set<SousSection> sousSections) {
        if (this.sousSections != null) {
            this.sousSections.forEach(i -> i.setSection(null));
        }
        if (sousSections != null) {
            sousSections.forEach(i -> i.setSection(this));
        }
        this.sousSections = sousSections;
    }

    public Section sousSections(Set<SousSection> sousSections) {
        this.setSousSections(sousSections);
        return this;
    }

    public Section addSousSections(SousSection sousSection) {
        this.sousSections.add(sousSection);
        sousSection.setSection(this);
        return this;
    }

    public Section removeSousSections(SousSection sousSection) {
        this.sousSections.remove(sousSection);
        sousSection.setSection(null);
        return this;
    }

    public Set<Section> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Section> sections) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSection(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setSection(this));
        }
        this.synonymes = sections;
    }

    public Section synonymes(Set<Section> sections) {
        this.setSynonymes(sections);
        return this;
    }

    public Section addSynonymes(Section section) {
        this.synonymes.add(section);
        section.setSection(this);
        return this;
    }

    public Section removeSynonymes(Section section) {
        this.synonymes.remove(section);
        section.setSection(null);
        return this;
    }

    public SousGenre getSousGenre() {
        return this.sousGenre;
    }

    public void setSousGenre(SousGenre sousGenre) {
        this.sousGenre = sousGenre;
    }

    public Section sousGenre(SousGenre sousGenre) {
        this.setSousGenre(sousGenre);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Section section(Section section) {
        this.setSection(section);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return id != null && id.equals(((Section) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
