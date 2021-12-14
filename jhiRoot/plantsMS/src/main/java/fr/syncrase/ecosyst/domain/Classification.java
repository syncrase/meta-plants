package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private RaunkierPlante raunkier;

    @OneToOne
    @JoinColumn(unique = true)
    private CronquistPlante cronquist;

    @OneToOne
    @JoinColumn(unique = true)
    private APGIPlante apg1;

    @OneToOne
    @JoinColumn(unique = true)
    private APGIIPlante apg2;

    @JsonIgnoreProperties(value = { "clades" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private APGIIIPlante apg3;

    @OneToOne
    @JoinColumn(unique = true)
    private APGIVPlante apg4;

    @JsonIgnoreProperties(
        value = {
            "classification",
            "confusions",
            "ensoleillements",
            "plantesPotageres",
            "cycleDeVie",
            "sol",
            "temperature",
            "racine",
            "strate",
            "feuillage",
            "nomsVernaculaires",
            "plante",
        },
        allowSetters = true
    )
    @OneToOne(mappedBy = "classification")
    private Plante plante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public RaunkierPlante getRaunkier() {
        return this.raunkier;
    }

    public void setRaunkier(RaunkierPlante raunkierPlante) {
        this.raunkier = raunkierPlante;
    }

    public Classification raunkier(RaunkierPlante raunkierPlante) {
        this.setRaunkier(raunkierPlante);
        return this;
    }

    public CronquistPlante getCronquist() {
        return this.cronquist;
    }

    public void setCronquist(CronquistPlante cronquistPlante) {
        this.cronquist = cronquistPlante;
    }

    public Classification cronquist(CronquistPlante cronquistPlante) {
        this.setCronquist(cronquistPlante);
        return this;
    }

    public APGIPlante getApg1() {
        return this.apg1;
    }

    public void setApg1(APGIPlante aPGIPlante) {
        this.apg1 = aPGIPlante;
    }

    public Classification apg1(APGIPlante aPGIPlante) {
        this.setApg1(aPGIPlante);
        return this;
    }

    public APGIIPlante getApg2() {
        return this.apg2;
    }

    public void setApg2(APGIIPlante aPGIIPlante) {
        this.apg2 = aPGIIPlante;
    }

    public Classification apg2(APGIIPlante aPGIIPlante) {
        this.setApg2(aPGIIPlante);
        return this;
    }

    public APGIIIPlante getApg3() {
        return this.apg3;
    }

    public void setApg3(APGIIIPlante aPGIIIPlante) {
        this.apg3 = aPGIIIPlante;
    }

    public Classification apg3(APGIIIPlante aPGIIIPlante) {
        this.setApg3(aPGIIIPlante);
        return this;
    }

    public APGIVPlante getApg4() {
        return this.apg4;
    }

    public void setApg4(APGIVPlante aPGIVPlante) {
        this.apg4 = aPGIVPlante;
    }

    public Classification apg4(APGIVPlante aPGIVPlante) {
        this.setApg4(aPGIVPlante);
        return this;
    }

    public Plante getPlante() {
        return this.plante;
    }

    public void setPlante(Plante plante) {
        if (this.plante != null) {
            this.plante.setClassification(null);
        }
        if (plante != null) {
            plante.setClassification(this);
        }
        this.plante = plante;
    }

    public Classification plante(Plante plante) {
        this.setPlante(plante);
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
