package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Germination.
 */
@Entity
@Table(name = "germination")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Germination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "temps_de_germination")
    private String tempsDeGermination;

    @Column(name = "condition_de_germination")
    private String conditionDeGermination;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTempsDeGermination() {
        return tempsDeGermination;
    }

    public Germination tempsDeGermination(String tempsDeGermination) {
        this.tempsDeGermination = tempsDeGermination;
        return this;
    }

    public void setTempsDeGermination(String tempsDeGermination) {
        this.tempsDeGermination = tempsDeGermination;
    }

    public String getConditionDeGermination() {
        return conditionDeGermination;
    }

    public Germination conditionDeGermination(String conditionDeGermination) {
        this.conditionDeGermination = conditionDeGermination;
        return this;
    }

    public void setConditionDeGermination(String conditionDeGermination) {
        this.conditionDeGermination = conditionDeGermination;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Germination)) {
            return false;
        }
        return id != null && id.equals(((Germination) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Germination{" +
            "id=" + getId() +
            ", tempsDeGermination='" + getTempsDeGermination() + "'" +
            ", conditionDeGermination='" + getConditionDeGermination() + "'" +
            "}";
    }
}
