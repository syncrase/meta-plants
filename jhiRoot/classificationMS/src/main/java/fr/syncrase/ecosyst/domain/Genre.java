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
 * Genus
 */
@ApiModel(description = "Genus")
@Entity
@Table(name = "genre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Genre implements Serializable {

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

    @OneToMany(mappedBy = "genre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "synonymes", "genre", "sousGenre" }, allowSetters = true)
    private Set<SousGenre> sousGenres = new HashSet<>();

    @OneToMany(mappedBy = "genre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousGenres", "synonymes", "sousTribu", "genre" }, allowSetters = true)
    private Set<Genre> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "genres", "synonymes", "tribu", "sousTribu" }, allowSetters = true)
    private SousTribu sousTribu;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousGenres", "synonymes", "sousTribu", "genre" }, allowSetters = true)
    private Genre genre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Genre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Genre nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Genre nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousGenre> getSousGenres() {
        return this.sousGenres;
    }

    public void setSousGenres(Set<SousGenre> sousGenres) {
        if (this.sousGenres != null) {
            this.sousGenres.forEach(i -> i.setGenre(null));
        }
        if (sousGenres != null) {
            sousGenres.forEach(i -> i.setGenre(this));
        }
        this.sousGenres = sousGenres;
    }

    public Genre sousGenres(Set<SousGenre> sousGenres) {
        this.setSousGenres(sousGenres);
        return this;
    }

    public Genre addSousGenres(SousGenre sousGenre) {
        this.sousGenres.add(sousGenre);
        sousGenre.setGenre(this);
        return this;
    }

    public Genre removeSousGenres(SousGenre sousGenre) {
        this.sousGenres.remove(sousGenre);
        sousGenre.setGenre(null);
        return this;
    }

    public Set<Genre> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Genre> genres) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setGenre(null));
        }
        if (genres != null) {
            genres.forEach(i -> i.setGenre(this));
        }
        this.synonymes = genres;
    }

    public Genre synonymes(Set<Genre> genres) {
        this.setSynonymes(genres);
        return this;
    }

    public Genre addSynonymes(Genre genre) {
        this.synonymes.add(genre);
        genre.setGenre(this);
        return this;
    }

    public Genre removeSynonymes(Genre genre) {
        this.synonymes.remove(genre);
        genre.setGenre(null);
        return this;
    }

    public SousTribu getSousTribu() {
        return this.sousTribu;
    }

    public void setSousTribu(SousTribu sousTribu) {
        this.sousTribu = sousTribu;
    }

    public Genre sousTribu(SousTribu sousTribu) {
        this.setSousTribu(sousTribu);
        return this;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre genre(Genre genre) {
        this.setGenre(genre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        return id != null && id.equals(((Genre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Genre{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
