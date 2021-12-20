package fr.syncrase.ecosyst.service.criteria;

import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.CronquistRank} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.CronquistRankResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cronquist-ranks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CronquistRankCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CronquistTaxonomikRanks
     */
    public static class CronquistTaxonomikRanksFilter extends Filter<CronquistTaxonomikRanks> {

        public CronquistTaxonomikRanksFilter() {}

        public CronquistTaxonomikRanksFilter(CronquistTaxonomikRanksFilter filter) {
            super(filter);
        }

        @Override
        public CronquistTaxonomikRanksFilter copy() {
            return new CronquistTaxonomikRanksFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CronquistTaxonomikRanksFilter rank;

    private StringFilter nomFr;

    private StringFilter nomLantin;

    private LongFilter childrenId;

    private LongFilter urlsId;

    private LongFilter synonymesId;

    private LongFilter parentId;

    private LongFilter cronquistRankId;

    private Boolean distinct;

    public CronquistRankCriteria() {}

    public CronquistRankCriteria(CronquistRankCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rank = other.rank == null ? null : other.rank.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLantin = other.nomLantin == null ? null : other.nomLantin.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.urlsId = other.urlsId == null ? null : other.urlsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.cronquistRankId = other.cronquistRankId == null ? null : other.cronquistRankId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CronquistRankCriteria copy() {
        return new CronquistRankCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public CronquistTaxonomikRanksFilter getRank() {
        return rank;
    }

    public CronquistTaxonomikRanksFilter rank() {
        if (rank == null) {
            rank = new CronquistTaxonomikRanksFilter();
        }
        return rank;
    }

    public void setRank(CronquistTaxonomikRanksFilter rank) {
        this.rank = rank;
    }

    public StringFilter getNomFr() {
        return nomFr;
    }

    public StringFilter nomFr() {
        if (nomFr == null) {
            nomFr = new StringFilter();
        }
        return nomFr;
    }

    public void setNomFr(StringFilter nomFr) {
        this.nomFr = nomFr;
    }

    public StringFilter getNomLantin() {
        return nomLantin;
    }

    public StringFilter nomLantin() {
        if (nomLantin == null) {
            nomLantin = new StringFilter();
        }
        return nomLantin;
    }

    public void setNomLantin(StringFilter nomLantin) {
        this.nomLantin = nomLantin;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public LongFilter getUrlsId() {
        return urlsId;
    }

    public LongFilter urlsId() {
        if (urlsId == null) {
            urlsId = new LongFilter();
        }
        return urlsId;
    }

    public void setUrlsId(LongFilter urlsId) {
        this.urlsId = urlsId;
    }

    public LongFilter getSynonymesId() {
        return synonymesId;
    }

    public LongFilter synonymesId() {
        if (synonymesId == null) {
            synonymesId = new LongFilter();
        }
        return synonymesId;
    }

    public void setSynonymesId(LongFilter synonymesId) {
        this.synonymesId = synonymesId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getCronquistRankId() {
        return cronquistRankId;
    }

    public LongFilter cronquistRankId() {
        if (cronquistRankId == null) {
            cronquistRankId = new LongFilter();
        }
        return cronquistRankId;
    }

    public void setCronquistRankId(LongFilter cronquistRankId) {
        this.cronquistRankId = cronquistRankId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CronquistRankCriteria that = (CronquistRankCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rank, that.rank) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLantin, that.nomLantin) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(urlsId, that.urlsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(cronquistRankId, that.cronquistRankId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rank, nomFr, nomLantin, childrenId, urlsId, synonymesId, parentId, cronquistRankId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistRankCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rank != null ? "rank=" + rank + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLantin != null ? "nomLantin=" + nomLantin + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (urlsId != null ? "urlsId=" + urlsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (cronquistRankId != null ? "cronquistRankId=" + cronquistRankId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
