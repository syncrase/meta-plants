package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exposition.
 */
@Entity
@Table(name = "exposition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valeur")
    private String valeur;

    @Column(name = "ensoleilement")
    private Double ensoleilement;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "cycleDeVie",
            "classification",
            "confusions",
            "interactions",
            "expositions",
            "sols",
            "nomsVernaculaires",
            "temperature",
            "racine",
            "strate",
            "feuillage",
        },
        allowSetters = true
    )
    private Plante plante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exposition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Exposition valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Double getEnsoleilement() {
        return this.ensoleilement;
    }

    public Exposition ensoleilement(Double ensoleilement) {
        this.setEnsoleilement(ensoleilement);
        return this;
    }

    public void setEnsoleilement(Double ensoleilement) {
        this.ensoleilement = ensoleilement;
    }

    public Plante getPlante() {
        return this.plante;
    }

    public void setPlante(Plante plante) {
        this.plante = plante;
    }

    public Exposition plante(Plante plante) {
        this.setPlante(plante);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exposition)) {
            return false;
        }
        return id != null && id.equals(((Exposition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exposition{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", ensoleilement=" + getEnsoleilement() +
            "}";
    }
}
