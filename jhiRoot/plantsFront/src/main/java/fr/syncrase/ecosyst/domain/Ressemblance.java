package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Pour que la ressemblance soit réflexive il faut l'enregistrer 2 fois. Car si la ressemblance A ressemble à B est enregistrée, alors B ne ressemble pas à A
 */
@ApiModel(
    description = "Pour que la ressemblance soit réflexive il faut l'enregistrer 2 fois. Car si la ressemblance A ressemble à B est enregistrée, alors B ne ressemble pas à A"
)
@Table("ressemblance")
public class Ressemblance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("description")
    private String description;

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
    private Plante planteRessemblant;

    @Column("plante_ressemblant_id")
    private Long planteRessemblantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ressemblance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Ressemblance description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plante getPlanteRessemblant() {
        return this.planteRessemblant;
    }

    public void setPlanteRessemblant(Plante plante) {
        this.planteRessemblant = plante;
        this.planteRessemblantId = plante != null ? plante.getId() : null;
    }

    public Ressemblance planteRessemblant(Plante plante) {
        this.setPlanteRessemblant(plante);
        return this;
    }

    public Long getPlanteRessemblantId() {
        return this.planteRessemblantId;
    }

    public void setPlanteRessemblantId(Long plante) {
        this.planteRessemblantId = plante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ressemblance)) {
            return false;
        }
        return id != null && id.equals(((Ressemblance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ressemblance{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
