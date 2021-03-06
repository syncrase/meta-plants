package fr.syncrase.ecosyst.domain.crawler.wikipedia;

import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ScrapedRank {
    private final String taxonName;
    private final String classificationLevel;
    private final String url;


    public ScrapedRank(String classificationLevel, String rankName, String url) throws InvalidRankName {
        if (!checkNameValidity(classificationLevel, rankName)) {
            throw new InvalidRankName("Le rang " + rankName + " ne peut être un " + classificationLevel);
        }
        this.classificationLevel = classificationLevel;
        this.taxonName = rankName;
        this.url = url;
    }

    public String getClassificationLevel() {
        return classificationLevel;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Vérification basique de la concordance entre le rang et le nom du rankName
     *
     * @param rankName            Nom du rankName dont le suffixe doit correspondre au rang
     * @param classificationLevel rang du rankName
     * @return S'il y a une incohérence, retourne null
     */
    @Contract(pure = true)
    private boolean checkNameValidity(String classificationLevel, @NotNull String rankName) {
        return (!rankName.endsWith(RankName.ORDRE.getSuffix()) || Arrays.asList(new String[]{"Ordre"}).contains(classificationLevel)) &&
            (!rankName.endsWith(RankName.FAMILLE.getSuffix()) || Arrays.asList(new String[]{"Famille"}).contains(classificationLevel));
    }

}
