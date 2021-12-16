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
 * Subsectio
 */
@ApiModel(description = "Subsectio")
@Entity
@Table(name = "sous_section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousSection implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousSection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousEspeces", "synonymes", "sousSection", "espece" }, allowSetters = true)
    private Set<Espece> especes = new HashSet<>();

    @OneToMany(mappedBy = "sousSection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "especes", "synonymes", "section", "sousSection" }, allowSetters = true)
    private Set<SousSection> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousSections", "synonymes", "sousGenre", "section" }, allowSetters = true)
    private Section section;

    @ManyToOne
    @JsonIgnoreProperties(value = { "especes", "synonymes", "section", "sousSection" }, allowSetters = true)
    private SousSection sousSection;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousSection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousSection nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousSection nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Espece> getEspeces() {
        return this.especes;
    }

    public void setEspeces(Set<Espece> especes) {
        if (this.especes != null) {
            this.especes.forEach(i -> i.setSousSection(null));
        }
        if (especes != null) {
            especes.forEach(i -> i.setSousSection(this));
        }
        this.especes = especes;
    }

    public SousSection especes(Set<Espece> especes) {
        this.setEspeces(especes);
        return this;
    }

    public SousSection addEspeces(Espece espece) {
        this.especes.add(espece);
        espece.setSousSection(this);
        return this;
    }

    public SousSection removeEspeces(Espece espece) {
        this.especes.remove(espece);
        espece.setSousSection(null);
        return this;
    }

    public Set<SousSection> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousSection> sousSections) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousSection(null));
        }
        if (sousSections != null) {
            sousSections.forEach(i -> i.setSousSection(this));
        }
        this.synonymes = sousSections;
    }

    public SousSection synonymes(Set<SousSection> sousSections) {
        this.setSynonymes(sousSections);
        return this;
    }

    public SousSection addSynonymes(SousSection sousSection) {
        this.synonymes.add(sousSection);
        sousSection.setSousSection(this);
        return this;
    }

    public SousSection removeSynonymes(SousSection sousSection) {
        this.synonymes.remove(sousSection);
        sousSection.setSousSection(null);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public SousSection section(Section section) {
        this.setSection(section);
        return this;
    }

    public SousSection getSousSection() {
        return this.sousSection;
    }

    public void setSousSection(SousSection sousSection) {
        this.sousSection = sousSection;
    }

    public SousSection sousSection(SousSection sousSection) {
        this.setSousSection(sousSection);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousSection)) {
            return false;
        }
        return id != null && id.equals(((SousSection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousSection{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return section;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return especes;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
