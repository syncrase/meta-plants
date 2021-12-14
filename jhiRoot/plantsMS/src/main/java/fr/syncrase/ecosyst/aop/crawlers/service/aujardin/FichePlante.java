/**
 *
 */
package fr.syncrase.ecosyst.aop.crawlers.service.aujardin;

import fr.syncrase.ecosyst.domain.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

/**
 * @author syncrase
 *
 */
public class FichePlante {

    Plante plante = new Plante();

    public FichePlante(Document page) {
        plante.setNomsVernaculaires(getNomsVernaculaires(page));

        // description3 : les deux sections "Botanique" et "Planter et cultiver"
        // div : contient les informations "N. scientifique", "Synonyme", "Famille",
        // "Origine", "Floraison", "Fleurs", "Type", "Végétation", "Feuillage",
        // "Hauteur"
        // puis
        // "Rusticité", "Exposition", "Sol", "Acidité",
        // "Humidité",
        // "Utilisation", "Plantation", "Multiplication"
        // Chacun de ces éléments contient un tag strong ainsi qu'un span

        // Il n'y a qu'un seul article par page
        Element article = page.select("article").get(0);

//		plante.setNomLatin(getElementDeDescription(article, "N. scientifique"));

        // replace all : soit remplace et ne retourne qu'un chiffre soit ne matche pas
        // et laisse tel quel
//		plante.setRusticite(getElementDeDescription(article, "Rusticité")
//				.replaceAll("^.*([-|])[ ]{0,1}(\\d+)[ ]{0,1}°[ ]{0,1}C.*$", "$1$2"));

        plante.setExposition(getElementDeDescription(article, "Exposition"));

        plante.setCycleDeVie(getCycleDeVie(article));

//		plante.setEntretien(getElementDeDescription(article, "Floraison"));

        plante.setClassification(getClassification(article));
    }

    private Set<NomVernaculaire> getNomsVernaculaires(Document page) {
        String nomVernaculaireTmp = page.select("article h1").text();
        // Plusieurs noms séparés par une virgule
        String[] nomVernaculaires = nomVernaculaireTmp.split(", ");

        Set<NomVernaculaire> nomVernaculaireSet = new HashSet<>();
        for (String n : nomVernaculaires) {
            NomVernaculaire nomVernaculaire = new NomVernaculaire();
            nomVernaculaire.setNom(n);
            nomVernaculaireSet.add(nomVernaculaire);
        }
        return nomVernaculaireSet;
    }

    private Classification getClassification(Element article) {
        // TODO Auto-generated method stub
        Classification classification = new Classification();
//        Cronquist cronquist = new Cronquist();
        // TODO
//		cronquist.set
        return null;
    }

    private CycleDeVie getCycleDeVie(Element article) {
        // Récupération de la floraison
        /**
         * août à septembre </br>
         * fin de l'été </br>
         * tout l'été </br>
         * mai <br>
         * fin d'été </br>
         * de mai aux gelées </br>
         * août jusqu'aux gelées </br>
         * juillet, août, fruit en automne et hiver TODO </br>
         * juillet/août </br>
         * de juillet à octobre </br>
         * fin juillet à septembre </br>
         * d'août jusqu'aux gelées </br>
         * avril, mai,</br>
         * juin printemps, </br>
         * été</br>
         */
        String floraison = getElementDeDescription(article, "Floraison");
        floraison = floraison.replace("û", "u").replace("é", "e").replace("è", "e");
//		List<Mois.MoisValues> mois1 = getMois(floraison);
//		List<Mois.MoisValues> mois2 = getSaison(floraison);
//		PeriodeAnnee periodeAnnee = getPeriode(mois1, mois2);

        CycleDeVie cycleDeVie = new CycleDeVie();
//		PeriodeAnnee periodeAnnee = new PeriodeAnnee();

//		cycleDeVie.setFloraison(periodeAnnee);

        return cycleDeVie;
    }

//	private PeriodeAnnee getPeriode(List<MoisValues> mois1, List<MoisValues> mois2) {
//		Double moisMin = 13d;
//		Double moisMax = 0d;
//
//		Set<MoisValues> mois = Stream.concat(mois1.stream(), mois2.stream()).collect(Collectors.toSet());
//
//		for (MoisValues m : mois) {
//			if (m.getNumero() < moisMin) {
//				moisMin = m.getNumero();
//			}
//			if (m.getNumero() > moisMax) {
//				moisMax = m.getNumero();
//			}
//		}
//
//		PeriodeAnnee periodeAnnee = new PeriodeAnnee();
//		Mois debutFlo = new Mois();
//		debutFlo.setNumero(moisMin);
//		periodeAnnee.setDebut(debutFlo);
//
//		Mois finFlo = new Mois();
//		finFlo.setNumero(moisMax);
//		periodeAnnee.setFin(finFlo);
//
//		return periodeAnnee;
//	}

//	private List<MoisValues> getSaison(String floraison) {
//		List<MoisValues> moisTrouves = new ArrayList<>();
//		for (Mois.SaisonsValues s : Mois.SaisonsValues.values()) {
//			if (floraison.toLowerCase().contains(s.toString().toLowerCase())) {
//				moisTrouves.addAll(s.getMois());
//			}
//		}
//		return moisTrouves;
//	}
//
//	private List<MoisValues> getMois(String floraison) {
//		List<MoisValues> moisTrouves = new ArrayList<>();
//		for (Mois.MoisValues s : Mois.MoisValues.values()) {
//			if (floraison.toLowerCase().contains(s.toString().toLowerCase())) {
//				moisTrouves.add(s);
//			}
//		}
//		return moisTrouves;
//	}

    private String getElementDeDescription(Element article, String nomDeLaDescription) {
        return article.getElementsMatchingOwnText(nomDeLaDescription).get(0).parent().select("span").get(0).text();
    }

    public Plante getPlante() {
        return this.plante;
    }

}
