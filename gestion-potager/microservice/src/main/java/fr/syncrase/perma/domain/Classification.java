package fr.syncrase.perma.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Classification.
 */
@Entity
@Table(name = "classification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Raunkier raunkier;

    @OneToOne
    @JoinColumn(unique = true)
    private Cronquist cronquist;

    @OneToOne
    @JoinColumn(unique = true)
    private APGI apg1;

    @OneToOne
    @JoinColumn(unique = true)
    private APGII apg2;

    @OneToOne
    @JoinColumn(unique = true)
    private APGIII apg3;

    @OneToOne
    @JoinColumn(unique = true)
    private APGIV apg4;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Raunkier getRaunkier() {
        return raunkier;
    }

    public Classification raunkier(Raunkier raunkier) {
        this.raunkier = raunkier;
        return this;
    }

    public void setRaunkier(Raunkier raunkier) {
        this.raunkier = raunkier;
    }

    public Cronquist getCronquist() {
        return cronquist;
    }

    public Classification cronquist(Cronquist cronquist) {
        this.cronquist = cronquist;
        return this;
    }

    public void setCronquist(Cronquist cronquist) {
        this.cronquist = cronquist;
    }

    public APGI getApg1() {
        return apg1;
    }

    public Classification apg1(APGI aPGI) {
        this.apg1 = aPGI;
        return this;
    }

    public void setApg1(APGI aPGI) {
        this.apg1 = aPGI;
    }

    public APGII getApg2() {
        return apg2;
    }

    public Classification apg2(APGII aPGII) {
        this.apg2 = aPGII;
        return this;
    }

    public void setApg2(APGII aPGII) {
        this.apg2 = aPGII;
    }

    public APGIII getApg3() {
        return apg3;
    }

    public Classification apg3(APGIII aPGIII) {
        this.apg3 = aPGIII;
        return this;
    }

    public void setApg3(APGIII aPGIII) {
        this.apg3 = aPGIII;
    }

    public APGIV getApg4() {
        return apg4;
    }

    public Classification apg4(APGIV aPGIV) {
        this.apg4 = aPGIV;
        return this;
    }

    public void setApg4(APGIV aPGIV) {
        this.apg4 = aPGIV;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classification)) {
            return false;
        }
        return id != null && id.equals(((Classification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classification{" +
            "id=" + getId() +
            "}";
    }
}
