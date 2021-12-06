package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Allelopathie.
 */
@Table("allelopathie")
public class Allelopathie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @Column("description")
    private String description;

    @Min(value = -10)
    @Max(value = 10)
    @Column("impact")
    private Integer impact;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "cycleDeVie",
            "confusions",
            "ensoleillements",
            "sols",
            "classification",
            "nomsVernaculaires",
            "temperature",
            "racine",
            "strate",
            "feuillage",
        },
        allowSetters = true
    )
    private Plante cible;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "cycleDeVie",
            "confusions",
            "ensoleillements",
            "sols",
            "classification",
            "nomsVernaculaires",
            "temperature",
            "racine",
            "strate",
            "feuillage",
        },
        allowSetters = true
    )
    private Plante origine;

    @Column("cible_id")
    private Long cibleId;

    @Column("origine_id")
    private Long origineId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Allelopathie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Allelopathie type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Allelopathie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImpact() {
        return this.impact;
    }

    public Allelopathie impact(Integer impact) {
        this.setImpact(impact);
        return this;
    }

    public void setImpact(Integer impact) {
        this.impact = impact;
    }

    public Plante getCible() {
        return this.cible;
    }

    public void setCible(Plante plante) {
        this.cible = plante;
        this.cibleId = plante != null ? plante.getId() : null;
    }

    public Allelopathie cible(Plante plante) {
        this.setCible(plante);
        return this;
    }

    public Plante getOrigine() {
        return this.origine;
    }

    public void setOrigine(Plante plante) {
        this.origine = plante;
        this.origineId = plante != null ? plante.getId() : null;
    }

    public Allelopathie origine(Plante plante) {
        this.setOrigine(plante);
        return this;
    }

    public Long getCibleId() {
        return this.cibleId;
    }

    public void setCibleId(Long plante) {
        this.cibleId = plante;
    }

    public Long getOrigineId() {
        return this.origineId;
    }

    public void setOrigineId(Long plante) {
        this.origineId = plante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Allelopathie)) {
            return false;
        }
        return id != null && id.equals(((Allelopathie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Allelopathie{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", impact=" + getImpact() +
            "}";
    }
}
