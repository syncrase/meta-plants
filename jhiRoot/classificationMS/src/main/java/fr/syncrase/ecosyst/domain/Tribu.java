package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tribus
 */
@ApiModel(description = "Tribus")
@Entity
@Table(name = "tribu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tribu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_fr", nullable = false)
    private String nomFr;

    @Column(name = "nom_latin")
    private String nomLatin;

    @OneToMany(mappedBy = "tribu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "genres", "synonymes", "tribu", "sousTribu" }, allowSetters = true)
    private Set<SousTribu> sousTribuses = new HashSet<>();

    @OneToMany(mappedBy = "tribu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sousTribuses", "synonymes", "sousFamille", "tribu" }, allowSetters = true)
    private Set<Tribu> synonymes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tribuses", "synonymes", "famille", "sousFamille" }, allowSetters = true)
    private SousFamille sousFamille;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sousTribuses", "synonymes", "sousFamille", "tribu" }, allowSetters = true)
    private Tribu tribu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tribu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Tribu nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Tribu nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Set<SousTribu> getSousTribuses() {
        return this.sousTribuses;
    }

    public void setSousTribuses(Set<SousTribu> sousTribus) {
        if (this.sousTribuses != null) {
            this.sousTribuses.forEach(i -> i.setTribu(null));
        }
        if (sousTribus != null) {
            sousTribus.forEach(i -> i.setTribu(this));
        }
        this.sousTribuses = sousTribus;
    }

    public Tribu sousTribuses(Set<SousTribu> sousTribus) {
        this.setSousTribuses(sousTribus);
        return this;
    }

    public Tribu addSousTribus(SousTribu sousTribu) {
        this.sousTribuses.add(sousTribu);
        sousTribu.setTribu(this);
        return this;
    }

    public Tribu removeSousTribus(SousTribu sousTribu) {
        this.sousTribuses.remove(sousTribu);
        sousTribu.setTribu(null);
        return this;
    }

    public Set<Tribu> getSynonymes() {
        return this.synonymes;
    }

    public void setSynonymes(Set<Tribu> tribus) {
        if (this.synonymes != null) {
            this.synonymes.forEach(i -> i.setTribu(null));
        }
        if (tribus != null) {
            tribus.forEach(i -> i.setTribu(this));
        }
        this.synonymes = tribus;
    }

    public Tribu synonymes(Set<Tribu> tribus) {
        this.setSynonymes(tribus);
        return this;
    }

    public Tribu addSynonymes(Tribu tribu) {
        this.synonymes.add(tribu);
        tribu.setTribu(this);
        return this;
    }

    public Tribu removeSynonymes(Tribu tribu) {
        this.synonymes.remove(tribu);
        tribu.setTribu(null);
        return this;
    }

    public SousFamille getSousFamille() {
        return this.sousFamille;
    }

    public void setSousFamille(SousFamille sousFamille) {
        this.sousFamille = sousFamille;
    }

    public Tribu sousFamille(SousFamille sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    public Tribu getTribu() {
        return this.tribu;
    }

    public void setTribu(Tribu tribu) {
        this.tribu = tribu;
    }

    public Tribu tribu(Tribu tribu) {
        this.setTribu(tribu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tribu)) {
            return false;
        }
        return id != null && id.equals(((Tribu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tribu{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
