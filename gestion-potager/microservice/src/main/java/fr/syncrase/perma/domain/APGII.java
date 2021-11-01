package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A APGII.
 */
@Entity
@Table(name = "apgii")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class APGII implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private String ordre;

    @NotNull
    @Column(name = "famille", nullable = false)
    private String famille;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdre() {
        return ordre;
    }

    public APGII ordre(String ordre) {
        this.ordre = ordre;
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return famille;
    }

    public APGII famille(String famille) {
        this.famille = famille;
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APGII)) {
            return false;
        }
        return id != null && id.equals(((APGII) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGII{" +
            "id=" + getId() +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            "}";
    }
}
