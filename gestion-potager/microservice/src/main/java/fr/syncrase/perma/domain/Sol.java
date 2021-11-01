package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Sol.
 */
@Entity
@Table(name = "sol")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "acidite")
    private Double acidite;

    @Column(name = "type")
    private String type;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAcidite() {
        return acidite;
    }

    public Sol acidite(Double acidite) {
        this.acidite = acidite;
        return this;
    }

    public void setAcidite(Double acidite) {
        this.acidite = acidite;
    }

    public String getType() {
        return type;
    }

    public Sol type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sol)) {
            return false;
        }
        return id != null && id.equals(((Sol) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sol{" +
            "id=" + getId() +
            ", acidite=" + getAcidite() +
            ", type='" + getType() + "'" +
            "}";
    }
}
