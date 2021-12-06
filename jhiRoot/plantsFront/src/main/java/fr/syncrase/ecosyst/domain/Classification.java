package fr.syncrase.ecosyst.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Classification.
 */
@Table("classification")
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_latin")
    private String nomLatin;

    @Transient
    private Raunkier raunkier;

    @Transient
    private Cronquist cronquist;

    @Transient
    private APGI apg1;

    @Transient
    private APGII apg2;

    @Transient
    private APGIII apg3;

    @Transient
    private APGIV apg4;

    @Column("raunkier_id")
    private Long raunkierId;

    @Column("cronquist_id")
    private Long cronquistId;

    @Column("apg1_id")
    private Long apg1Id;

    @Column("apg2_id")
    private Long apg2Id;

    @Column("apg3_id")
    private Long apg3Id;

    @Column("apg4_id")
    private Long apg4Id;

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

    public String getNomLatin() {
        return this.nomLatin;
    }

    public Classification nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public Raunkier getRaunkier() {
        return this.raunkier;
    }

    public void setRaunkier(Raunkier raunkier) {
        this.raunkier = raunkier;
        this.raunkierId = raunkier != null ? raunkier.getId() : null;
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
        this.cronquistId = cronquist != null ? cronquist.getId() : null;
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
        this.apg1Id = aPGI != null ? aPGI.getId() : null;
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
        this.apg2Id = aPGII != null ? aPGII.getId() : null;
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
        this.apg3Id = aPGIII != null ? aPGIII.getId() : null;
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
        this.apg4Id = aPGIV != null ? aPGIV.getId() : null;
    }

    public Classification apg4(APGIV aPGIV) {
        this.setApg4(aPGIV);
        return this;
    }

    public Long getRaunkierId() {
        return this.raunkierId;
    }

    public void setRaunkierId(Long raunkier) {
        this.raunkierId = raunkier;
    }

    public Long getCronquistId() {
        return this.cronquistId;
    }

    public void setCronquistId(Long cronquist) {
        this.cronquistId = cronquist;
    }

    public Long getApg1Id() {
        return this.apg1Id;
    }

    public void setApg1Id(Long aPGI) {
        this.apg1Id = aPGI;
    }

    public Long getApg2Id() {
        return this.apg2Id;
    }

    public void setApg2Id(Long aPGII) {
        this.apg2Id = aPGII;
    }

    public Long getApg3Id() {
        return this.apg3Id;
    }

    public void setApg3Id(Long aPGIII) {
        this.apg3Id = aPGIII;
    }

    public Long getApg4Id() {
        return this.apg4Id;
    }

    public void setApg4Id(Long aPGIV) {
        this.apg4Id = aPGIV;
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
            ", nomLatin='" + getNomLatin() + "'" +
            "}";
    }
}
