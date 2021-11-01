package fr.syncrase.perma.plantes.modeles;

public class Plante {

	private String id;
	private String nom;
	private PeriodeAnnee semisPleineTerre;
	private PeriodeAnnee periodeSemisSousAbris;

	public Plante(String nom, PeriodeAnnee semisPleineTerre, PeriodeAnnee periodeSemisSousAbris) {
		super();
		this.nom = nom;
		this.semisPleineTerre = semisPleineTerre;
		this.periodeSemisSousAbris = periodeSemisSousAbris;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public PeriodeAnnee getSemisPleineTerre() {
		return semisPleineTerre;
	}

	public void setSemisPleineTerre(PeriodeAnnee semisPleineTerre) {
		this.semisPleineTerre = semisPleineTerre;
	}

	public PeriodeAnnee getPeriodeSemisSousAbris() {
		return periodeSemisSousAbris;
	}

	public void setPeriodeSemisSousAbris(PeriodeAnnee periodeSemisSousAbris) {
		this.periodeSemisSousAbris = periodeSemisSousAbris;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Plante [id=" + id + ", nom=" + nom + ", semisPleineTerre=" + semisPleineTerre
				+ ", periodeSemisSousAbris=" + periodeSemisSousAbris + "]";
	}

}
