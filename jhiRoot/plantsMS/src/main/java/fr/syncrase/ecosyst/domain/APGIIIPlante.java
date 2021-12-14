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
 * Peut contenir plusieurs clades
 */
@ApiModel(description = "Peut contenir plusieurs clades")
@Entity
@Table(name = "apgiii_plante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class APGIIIPlante implements Serializable {

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

    @NotNull
    @Column(name = "sous_famille", nullable = false)
    private String sousFamille;

    @Column(name = "tribu")
    private String tribu;

    @Column(name = "sous_tribu")
    private String sousTribu;

    @NotNull
    @Column(name = "genre", nullable = false)
    private String genre;

    @ManyToMany
    @JoinTable(
        name = "rel_apgiii_plante__clades",
        joinColumns = @JoinColumn(name = "apgiii_plante_id"),
        inverseJoinColumns = @JoinColumn(name = "clades_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apgiiis" }, allowSetters = true)
    private Set<Clade> clades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public APGIIIPlante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdre() {
        return this.ordre;
    }

    public APGIIIPlante ordre(String ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return this.famille;
    }

    public APGIIIPlante famille(String famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getSousFamille() {
        return this.sousFamille;
    }

    public APGIIIPlante sousFamille(String sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    public void setSousFamille(String sousFamille) {
        this.sousFamille = sousFamille;
    }

    public String getTribu() {
        return this.tribu;
    }

    public APGIIIPlante tribu(String tribu) {
        this.setTribu(tribu);
        return this;
    }

    public void setTribu(String tribu) {
        this.tribu = tribu;
    }

    public String getSousTribu() {
        return this.sousTribu;
    }

    public APGIIIPlante sousTribu(String sousTribu) {
        this.setSousTribu(sousTribu);
        return this;
    }

    public void setSousTribu(String sousTribu) {
        this.sousTribu = sousTribu;
    }

    public String getGenre() {
        return this.genre;
    }

    public APGIIIPlante genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Clade> getClades() {
        return this.clades;
    }

    public void setClades(Set<Clade> clades) {
        this.clades = clades;
    }

    public APGIIIPlante clades(Set<Clade> clades) {
        this.setClades(clades);
        return this;
    }

    public APGIIIPlante addClades(Clade clade) {
        this.clades.add(clade);
        clade.getApgiiis().add(this);
        return this;
    }

    public APGIIIPlante removeClades(Clade clade) {
        this.clades.remove(clade);
        clade.getApgiiis().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APGIIIPlante)) {
            return false;
        }
        return id != null && id.equals(((APGIIIPlante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIIIPlante{" +
            "id=" + getId() +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            ", sousFamille='" + getSousFamille() + "'" +
            ", tribu='" + getTribu() + "'" +
            ", sousTribu='" + getSousTribu() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
