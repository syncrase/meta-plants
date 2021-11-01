package fr.syncrase.perma.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mois.
 */
@Entity
@Table(name = "mois")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mois implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "numero", nullable = false)
	private Double numero;

	@NotNull
	@Column(name = "nom", nullable = false)
	private String nom;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getNumero() {
		return numero;
	}

	public Mois numero(Double numero) {
		this.numero = numero;
		return this;
	}

	public void setNumero(Double numero) {
		this.numero = numero;
	}

	public String getNom() {
		return nom;
	}

	public Mois nom(String nom) {
		this.nom = nom;
		return this;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Mois)) {
			return false;
		}
		return id != null && id.equals(((Mois) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Mois{" + "id=" + getId() + ", numero=" + getNumero() + ", nom='" + getNom() + "'" + "}";
	}

	public static enum MoisValues {
		JANVIER(1d, "Janvier"), MIJANVIER(1.5, "Mi-Janvier"), //
		FEVRIER(2d, "Février"), MIFEVRIER(2.5, "Mi-Février"), //
		MARS(3d, "Mars"), MIMARS(3.5, "Mi-Mars"), //
		AVRIL(4d, "Avril"), MIAVRIL(4.5, "Mi-Avril"), //
		MAI(5d, "Mai"), MIMAI(5.5, "Mi-Mai"), //
		JUIN(6d, "Juin"), MIJUIN(6.5, "Mi-Juin"), //
		JUILLET(7d, "Juillet"), MIJUILLET(7.5, "Mi-Juillet"), //
		AOUT(8d, "Août"), MIAOUT(8.5, "Mi-Août"), //
		SEPTEMBRE(9d, "Septembre"), MISEPTEMBRE(9.5, "Mi-Septembre"), //
		OCTOBRE(10d, "Octobre"), MIOCTOBRE(10.5, "Mi-Octobre"), //
		NOVEMBRE(11d, "Novembre"), MINOVEMBRE(11.5, "Mi-Novembre"), //
		DECEMBRE(12d, "Décembre"), MIDECEMBRE(12.5, "Mi-Décembre");

		private final Double numero;
		private final String description;

		private static final Map<Double, MoisValues> BY_NUMERO = new HashMap<>();
		private static final Map<String, MoisValues> BY_DESCRIPTION = new HashMap<>();

		static {
			for (MoisValues e : MoisValues.values()) {
				BY_DESCRIPTION.put(e.description, e);
				BY_NUMERO.put(e.numero, e);
			}
		}

		MoisValues(Double numero, String description) {
			this.numero = numero;
			this.description = description;
		}

		public Double getNumero() {
			return numero;
		}

		public String getDescription() {
			return description;
		}

		/**
		 * Retourne le label du mois à partir de sa description
		 * 
		 * @param description
		 * @return
		 */
		public static MoisValues labelParDescription(String description) {

			final MoisValues value = BY_DESCRIPTION.get(description);
			return value != null ? value : new Object() {
				public MoisValues exception() {
					throw new IllegalArgumentException("Symbole incconu : ");
				}
			}.exception();
		}

		/**
		 * Retourne le label du à partir de son numéro
		 * 
		 * @param numero
		 * @return
		 */
		public static MoisValues labelParNumero(double numero) {

			final MoisValues value = BY_NUMERO.get(numero);
			if (value != null) {
				return value;
			}
			throw new IllegalArgumentException("Coef incconu : " + numero);
		}

	}

	public static enum SaisonsValues {
		PRINTEMPS(new ArrayList<MoisValues>() {
			private static final long serialVersionUID = -3184964959346242565L;
			{
				add(MoisValues.MARS);
				add(MoisValues.JUIN);
			}
		}), ETE(new ArrayList<MoisValues>() {
			private static final long serialVersionUID = -6190977342517298174L;
			{
				add(MoisValues.JUIN);
				add(MoisValues.SEPTEMBRE);
			}
		}), AUTOMNE(new ArrayList<MoisValues>() {
			private static final long serialVersionUID = 7156670232528731696L;
			{
				add(MoisValues.SEPTEMBRE);
				add(MoisValues.DECEMBRE);
			}
		}), HIVER(new ArrayList<MoisValues>() {
			private static final long serialVersionUID = -9214035359223638760L;
			{
				add(MoisValues.DECEMBRE);
				add(MoisValues.MARS);
			}
		});

		List<MoisValues> mois;

		SaisonsValues(List<MoisValues> is) {
			mois = is;
		}

		public List<MoisValues> getMois() {
			return mois;
		}

	}
}
