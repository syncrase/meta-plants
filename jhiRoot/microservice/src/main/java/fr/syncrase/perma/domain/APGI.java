package fr.syncrase.perma.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A APGI.
 */
@Entity
@Table(name = "apgi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class APGI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private String ordre;

    @NotNull
    @Column(name = "famille", nullable = false)
    private String famille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public APGI id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdre() {
        return this.ordre;
    }

    public APGI ordre(String ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return this.famille;
    }

    public APGI famille(String famille) {
        this.setFamille(famille);
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
        if (!(o instanceof APGI)) {
            return false;
        }
        return id != null && id.equals(((APGI) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGI{" +
            "id=" + getId() +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            "}";
    }
}
