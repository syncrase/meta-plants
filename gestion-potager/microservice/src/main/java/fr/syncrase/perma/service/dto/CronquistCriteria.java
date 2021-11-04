package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.perma.domain.Cronquist} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.CronquistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cronquists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CronquistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter regne;

    private StringFilter sousRegne;

    private StringFilter division;

    private StringFilter classe;

    private StringFilter sousClasse;

    private StringFilter ordre;

    private StringFilter famille;

    private StringFilter genre;

    public CronquistCriteria() {
    }

    public CronquistCriteria(CronquistCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.regne = other.regne == null ? null : other.regne.copy();
        this.sousRegne = other.sousRegne == null ? null : other.sousRegne.copy();
        this.division = other.division == null ? null : other.division.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.sousClasse = other.sousClasse == null ? null : other.sousClasse.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.famille = other.famille == null ? null : other.famille.copy();
        this.genre = other.genre == null ? null : other.genre.copy();
    }

    @Override
    public CronquistCriteria copy() {
        return new CronquistCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRegne() {
        return regne;
    }

    public void setRegne(StringFilter regne) {
        this.regne = regne;
    }

    public StringFilter getSousRegne() {
        return sousRegne;
    }

    public void setSousRegne(StringFilter sousRegne) {
        this.sousRegne = sousRegne;
    }

    public StringFilter getDivision() {
        return division;
    }

    public void setDivision(StringFilter division) {
        this.division = division;
    }

    public StringFilter getClasse() {
        return classe;
    }

    public void setClasse(StringFilter classe) {
        this.classe = classe;
    }

    public StringFilter getSousClasse() {
        return sousClasse;
    }

    public void setSousClasse(StringFilter sousClasse) {
        this.sousClasse = sousClasse;
    }

    public StringFilter getOrdre() {
        return ordre;
    }

    public void setOrdre(StringFilter ordre) {
        this.ordre = ordre;
    }

    public StringFilter getFamille() {
        return famille;
    }

    public void setFamille(StringFilter famille) {
        this.famille = famille;
    }

    public StringFilter getGenre() {
        return genre;
    }

    public void setGenre(StringFilter genre) {
        this.genre = genre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CronquistCriteria that = (CronquistCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(regne, that.regne) &&
            Objects.equals(sousRegne, that.sousRegne) &&
            Objects.equals(division, that.division) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(sousClasse, that.sousClasse) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(famille, that.famille) &&
            Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        regne,
        sousRegne,
        division,
        classe,
        sousClasse,
        ordre,
        famille,
        genre
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (regne != null ? "regne=" + regne + ", " : "") +
                (sousRegne != null ? "sousRegne=" + sousRegne + ", " : "") +
                (division != null ? "division=" + division + ", " : "") +
                (classe != null ? "classe=" + classe + ", " : "") +
                (sousClasse != null ? "sousClasse=" + sousClasse + ", " : "") +
                (ordre != null ? "ordre=" + ordre + ", " : "") +
                (famille != null ? "famille=" + famille + ", " : "") +
                (genre != null ? "genre=" + genre + ", " : "") +
            "}";
    }

}