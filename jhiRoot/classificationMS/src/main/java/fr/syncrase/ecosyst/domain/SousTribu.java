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
 * Subtribus
 */
@ApiModel(description = "Subtribus")
@Entity
@Table(name = "sous_tribu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousTribu implements Serializable, CronquistRank {

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

    @OneToMany(mappedBy = "sousTribu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousGenres", "synonymes", "sousTribu", "genre" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "sousTribu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "genres", "synonymes", "tribu", "sousTribu" }, allowSetters = true)
    private Set<SousTribu> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousTribuses", "synonymes", "sousFamille", "tribu" }, allowSetters = true)
    private Tribu tribu;

    @ManyToOne
    @JsonIgnoreProperties(value = { "genres", "synonymes", "tribu", "sousTribu" }, allowSetters = true)
    private SousTribu sousTribu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousTribu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public SousTribu nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public SousTribu nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        if (this.genres != null) {
            this.genres.forEach(i -> i.setSousTribu(null));
        }
        if (genres != null) {
            genres.forEach(i -> i.setSousTribu(this));
        }
        this.genres = genres;
    }

    public SousTribu genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public SousTribu addGenres(Genre genre) {
        this.genres.add(genre);
        genre.setSousTribu(this);
        return this;
    }

    public SousTribu removeGenres(Genre genre) {
        this.genres.remove(genre);
        genre.setSousTribu(null);
        return this;
    }

    public Set<SousTribu> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<SousTribu> sousTribus) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setSousTribu(null));
        }
        if (sousTribus != null) {
            sousTribus.forEach(i -> i.setSousTribu(this));
        }
        this.synonymes = sousTribus;
    }

    public SousTribu synonymes(Set<SousTribu> sousTribus) {
        this.setSynonymes(sousTribus);
        return this;
    }

    public SousTribu addSynonymes(SousTribu sousTribu) {
        this.synonymes.add(sousTribu);
        sousTribu.setSousTribu(this);
        return this;
    }

    public SousTribu removeSynonymes(SousTribu sousTribu) {
        this.synonymes.remove(sousTribu);
        sousTribu.setSousTribu(null);
        return this;
    }

    public Tribu getTribu() {
        return this.tribu;
    }

    public void setTribu(Tribu tribu) {
        this.tribu = tribu;
    }

    public SousTribu tribu(Tribu tribu) {
        this.setTribu(tribu);
        return this;
    }

    public SousTribu getSousTribu() {
        return this.sousTribu;
    }

    public void setSousTribu(SousTribu sousTribu) {
        this.sousTribu = sousTribu;
    }

    public SousTribu sousTribu(SousTribu sousTribu) {
        this.setSousTribu(sousTribu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousTribu)) {
            return false;
        }
        return id != null && id.equals(((SousTribu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousTribu{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }

    @Override
    public CronquistRank getParent() {
        return tribu;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return genres;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        return null;
    }
}
