package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Feuillage.
 */
@Table("feuillage")
public class Feuillage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("type")
    private String type;

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
    private Set<Plante> plantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feuillage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Feuillage type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Plante> getPlantes() {
        return this.plantes;
    }

    public void setPlantes(Set<Plante> plantes) {
        if (this.plantes != null) {
            this.plantes.forEach(i -> i.setFeuillage(null));
        }
        if (plantes != null) {
            plantes.forEach(i -> i.setFeuillage(this));
        }
        this.plantes = plantes;
    }

    public Feuillage plantes(Set<Plante> plantes) {
        this.setPlantes(plantes);
        return this;
    }

    public Feuillage addPlante(Plante plante) {
        this.plantes.add(plante);
        plante.setFeuillage(this);
        return this;
    }

    public Feuillage removePlante(Plante plante) {
        this.plantes.remove(plante);
        plante.setFeuillage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feuillage)) {
            return false;
        }
        return id != null && id.equals(((Feuillage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feuillage{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
