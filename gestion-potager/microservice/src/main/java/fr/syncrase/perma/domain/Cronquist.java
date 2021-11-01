package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Cronquist.
 */
@Entity
@Table(name = "cronquist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cronquist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "regne", nullable = false)
    private String regne;

    @NotNull
    @Column(name = "sous_regne", nullable = false)
    private String sousRegne;

    @NotNull
    @Column(name = "division", nullable = false)
    private String division;

    @NotNull
    @Column(name = "classe", nullable = false)
    private String classe;

    @NotNull
    @Column(name = "sous_classe", nullable = false)
    private String sousClasse;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private String ordre;

    @NotNull
    @Column(name = "famille", nullable = false)
    private String famille;

    @NotNull
    @Column(name = "genre", nullable = false)
    private String genre;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegne() {
        return regne;
    }

    public Cronquist regne(String regne) {
        this.regne = regne;
        return this;
    }

    public void setRegne(String regne) {
        this.regne = regne;
    }

    public String getSousRegne() {
        return sousRegne;
    }

    public Cronquist sousRegne(String sousRegne) {
        this.sousRegne = sousRegne;
        return this;
    }

    public void setSousRegne(String sousRegne) {
        this.sousRegne = sousRegne;
    }

    public String getDivision() {
        return division;
    }

    public Cronquist division(String division) {
        this.division = division;
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getClasse() {
        return classe;
    }

    public Cronquist classe(String classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSousClasse() {
        return sousClasse;
    }

    public Cronquist sousClasse(String sousClasse) {
        this.sousClasse = sousClasse;
        return this;
    }

    public void setSousClasse(String sousClasse) {
        this.sousClasse = sousClasse;
    }

    public String getOrdre() {
        return ordre;
    }

    public Cronquist ordre(String ordre) {
        this.ordre = ordre;
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return famille;
    }

    public Cronquist famille(String famille) {
        this.famille = famille;
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getGenre() {
        return genre;
    }

    public Cronquist genre(String genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cronquist)) {
            return false;
        }
        return id != null && id.equals(((Cronquist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cronquist{" +
            "id=" + getId() +
            ", regne='" + getRegne() + "'" +
            ", sousRegne='" + getSousRegne() + "'" +
            ", division='" + getDivision() + "'" +
            ", classe='" + getClasse() + "'" +
            ", sousClasse='" + getSousClasse() + "'" +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
