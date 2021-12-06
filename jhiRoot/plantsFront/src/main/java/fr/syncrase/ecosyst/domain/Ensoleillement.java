package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Ensoleillement.
 */
@Table("ensoleillement")
public class Ensoleillement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("orientation")
    private String orientation;

    @Column("ensoleilement")
    private Double ensoleilement;

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
    private Plante plante;

    @Column("plante_id")
    private Long planteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ensoleillement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrientation() {
        return this.orientation;
    }

    public Ensoleillement orientation(String orientation) {
        this.setOrientation(orientation);
        return this;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Double getEnsoleilement() {
        return this.ensoleilement;
    }

    public Ensoleillement ensoleilement(Double ensoleilement) {
        this.setEnsoleilement(ensoleilement);
        return this;
    }

    public void setEnsoleilement(Double ensoleilement) {
        this.ensoleilement = ensoleilement;
    }

    public Plante getPlante() {
        return this.plante;
    }

    public void setPlante(Plante plante) {
        this.plante = plante;
        this.planteId = plante != null ? plante.getId() : null;
    }

    public Ensoleillement plante(Plante plante) {
        this.setPlante(plante);
        return this;
    }

    public Long getPlanteId() {
        return this.planteId;
    }

    public void setPlanteId(Long plante) {
        this.planteId = plante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ensoleillement)) {
            return false;
        }
        return id != null && id.equals(((Ensoleillement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ensoleillement{" +
            "id=" + getId() +
            ", orientation='" + getOrientation() + "'" +
            ", ensoleilement=" + getEnsoleilement() +
            "}";
    }
}
