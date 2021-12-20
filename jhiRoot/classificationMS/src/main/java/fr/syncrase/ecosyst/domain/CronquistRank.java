package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CronquistRank.
 */
@Entity
@Table(name = "cronquist_rank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronquistRank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private CronquistTaxonomikRanks rank;

    @Column(name = "nom_fr", unique = true)
    private String nomFr;

    @Column(name = "nom_lantin", unique = true)
    private String nomLantin;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "children", "urls", "synonymes", "parent", "cronquistRank" }, allowSetters = true)
    private Set<CronquistRank> children = new HashSet<>();

    @OneToMany(mappedBy = "cronquistRank")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cronquistRank" }, allowSetters = true)
    private Set<Url> urls = new HashSet<>();

    @OneToMany(mappedBy = "cronquistRank")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "children", "urls", "synonymes", "parent", "cronquistRank" }, allowSetters = true)
    private Set<CronquistRank> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "children", "urls", "synonymes", "parent", "cronquistRank" }, allowSetters = true)
    private CronquistRank parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "children", "urls", "synonymes", "parent", "cronquistRank" }, allowSetters = true)
    private CronquistRank cronquistRank;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CronquistRank id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CronquistTaxonomikRanks getRank() {
        return this.rank;
    }

    public CronquistRank rank(CronquistTaxonomikRanks rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(CronquistTaxonomikRanks rank) {
        this.rank = rank;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public CronquistRank nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLantin() {
        return this.nomLantin;
    }

    public CronquistRank nomLantin(String nomLantin) {
        this.setNomLantin(nomLantin);
        return this;
    }

    public void setNomLantin(String nomLantin) {
        this.nomLantin = nomLantin;
    }

    public Set<CronquistRank> getChildren() {
        return this.children;
    }

    public void setChildren(Set<CronquistRank> cronquistRanks) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (cronquistRanks != null) {
            cronquistRanks.forEach(i -> i.setParent(this));
        }
        this.children = cronquistRanks;
    }

    public CronquistRank children(Set<CronquistRank> cronquistRanks) {
        this.setChildren(cronquistRanks);
        return this;
    }

    public CronquistRank addChildren(CronquistRank cronquistRank) {
        this.children.add(cronquistRank);
        cronquistRank.setParent(this);
        return this;
    }

    public CronquistRank removeChildren(CronquistRank cronquistRank) {
        this.children.remove(cronquistRank);
        cronquistRank.setParent(null);
        return this;
    }

    public Set<Url> getUrls() {
        return this.urls;
    }

    public void setUrls(Set<Url> urls) {
        if (this.urls != null) {
            this.urls.forEach(i -> i.setCronquistRank(null));
        }
        if (urls != null) {
            urls.forEach(i -> i.setCronquistRank(this));
        }
        this.urls = urls;
    }

    public CronquistRank urls(Set<Url> urls) {
        this.setUrls(urls);
        return this;
    }

    public CronquistRank addUrls(Url url) {
        this.urls.add(url);
        url.setCronquistRank(this);
        return this;
    }

    public CronquistRank removeUrls(Url url) {
        this.urls.remove(url);
        url.setCronquistRank(null);
        return this;
    }

    public Set<CronquistRank> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<CronquistRank> cronquistRanks) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setCronquistRank(null));
        }
        if (cronquistRanks != null) {
            cronquistRanks.forEach(i -> i.setCronquistRank(this));
        }
        this.synonymes = cronquistRanks;
    }

    public CronquistRank synonymes(Set<CronquistRank> cronquistRanks) {
        this.setSynonymes(cronquistRanks);
        return this;
    }

    public CronquistRank addSynonymes(CronquistRank cronquistRank) {
        this.synonymes.add(cronquistRank);
        cronquistRank.setCronquistRank(this);
        return this;
    }

    public CronquistRank removeSynonymes(CronquistRank cronquistRank) {
        this.synonymes.remove(cronquistRank);
        cronquistRank.setCronquistRank(null);
        return this;
    }

    public CronquistRank getParent() {
        return this.parent;
    }

    public void setParent(CronquistRank cronquistRank) {
        this.parent = cronquistRank;
    }

    public CronquistRank parent(CronquistRank cronquistRank) {
        this.setParent(cronquistRank);
        return this;
    }

    public CronquistRank getCronquistRank() {
        return this.cronquistRank;
    }

    public void setCronquistRank(CronquistRank cronquistRank) {
        this.cronquistRank = cronquistRank;
    }

    public CronquistRank cronquistRank(CronquistRank cronquistRank) {
        this.setCronquistRank(cronquistRank);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronquistRank)) {
            return false;
        }
        return id != null && id.equals(((CronquistRank) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistRank{" +
            "id=" + getId() +
            ", rank='" + getRank() + "'" +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLantin='" + getNomLantin() + "'" +
            "}";
    }
}
