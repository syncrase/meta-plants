package fr.syncrase.perma.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
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

    public Classification(Cronquist cronquist) {
		this.cronquist = cronquist;
	}

	public Classification() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
        return this.id;
    }

    public Classification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Raunkier getRaunkier() {
        return this.raunkier;
    }

    public void setRaunkier(Raunkier raunkier) {
        this.raunkier = raunkier;
    }

    public Classification raunkier(Raunkier raunkier) {
        this.setRaunkier(raunkier);
        return this;
    }

    public Cronquist getCronquist() {
        return this.cronquist;
    }

    public void setCronquist(Cronquist cronquist) {
        this.cronquist = cronquist;
    }

    public Classification cronquist(Cronquist cronquist) {
        this.setCronquist(cronquist);
        return this;
    }

    public APGI getApg1() {
        return this.apg1;
    }

    public void setApg1(APGI aPGI) {
        this.apg1 = aPGI;
    }

    public Classification apg1(APGI aPGI) {
        this.setApg1(aPGI);
        return this;
    }

    public APGII getApg2() {
        return this.apg2;
    }

    public void setApg2(APGII aPGII) {
        this.apg2 = aPGII;
    }

    public Classification apg2(APGII aPGII) {
        this.setApg2(aPGII);
        return this;
    }

    public APGIII getApg3() {
        return this.apg3;
    }

    public void setApg3(APGIII aPGIII) {
        this.apg3 = aPGIII;
    }

    public Classification apg3(APGIII aPGIII) {
        this.setApg3(aPGIII);
        return this;
    }

    public APGIV getApg4() {
        return this.apg4;
    }

    public void setApg4(APGIV aPGIV) {
        this.apg4 = aPGIV;
    }

    public Classification apg4(APGIV aPGIV) {
        this.setApg4(aPGIV);
        return this;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classification{" +
            "id=" + getId() +
            "}";
    }
}
