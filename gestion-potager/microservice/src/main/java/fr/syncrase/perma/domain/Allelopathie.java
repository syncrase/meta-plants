package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Allelopathie.
 */
@Entity
@Table(name = "allelopathie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Allelopathie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private Plante cible;

    @OneToOne
    @JoinColumn(unique = true)
    private Plante origine;

    @ManyToOne
    @JsonIgnoreProperties(value = "interactions", allowSetters = true)
    private Plante plante;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Allelopathie type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Allelopathie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plante getCible() {
        return cible;
    }

    public Allelopathie cible(Plante plante) {
        this.cible = plante;
        return this;
    }

    public void setCible(Plante plante) {
        this.cible = plante;
    }

    public Plante getOrigine() {
        return origine;
    }

    public Allelopathie origine(Plante plante) {
        this.origine = plante;
        return this;
    }

    public void setOrigine(Plante plante) {
        this.origine = plante;
    }

    public Plante getPlante() {
        return plante;
    }

    public Allelopathie plante(Plante plante) {
        this.plante = plante;
        return this;
    }

    public void setPlante(Plante plante) {
        this.plante = plante;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Allelopathie{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
