package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Ressemblance.
 */
@Entity
@Table(name = "ressemblance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ressemblance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = "confusions", allowSetters = true)
    private Plante confusion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Ressemblance description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plante getConfusion() {
        return confusion;
    }

    public Ressemblance confusion(Plante plante) {
        this.confusion = plante;
        return this;
    }

    public void setConfusion(Plante plante) {
        this.confusion = plante;
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
        return 31;
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
