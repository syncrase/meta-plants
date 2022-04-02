package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.IUrl;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils {


    @NotNull
    public LinkedMap<RankName, ICronquistRank> transformToMapOfRanksByName(@NotNull Collection<ICronquistRank> corylopsisRanks) {
        LinkedMap<RankName, ICronquistRank> corylopsisClassification = new LinkedMap<>();
        corylopsisRanks.forEach(cronquistRank -> corylopsisClassification.put(cronquistRank.getRankName(), cronquistRank));
        return corylopsisClassification;
    }


    @NotNull
    public Set<String> getRankNames(
        CronquistClassificationBranch corylopsisClassification,
        RankName ordre
                                   ) {
        return corylopsisClassification.getRang(ordre).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
    }

    /**
     * Vérifie que les classifications sont les mêmes.
     * Les deux classifications :
     * <ul>
     *     <li>doivent commencer au même rang</li>
     *     <li>ne doivent pas être null</li>
     * </ul>
     * La comparaison est du rang de base jusqu'au super-règne
     *
     * @param neriifoliaClassification   classification à comparer
     * @param selaginaceaeClassification classification à comparer
     */
    public void assertThatClassificationsAreTheSame(
        @NotNull CronquistClassificationBranch neriifoliaClassification,
        @NotNull CronquistClassificationBranch selaginaceaeClassification
                                                   ) {
        assert neriifoliaClassification.getRangDeBase().getRankName().equals(selaginaceaeClassification.getRangDeBase().getRankName());

        @NotNull RankName lowestRank = neriifoliaClassification.getRangDeBase().getRankName();
        for (RankName rank = RankName.SUPERREGNE; Objects.requireNonNull(rank).isHighestRankOf(lowestRank); rank = rank.getRangInferieur()) {
            isTheSameRank(neriifoliaClassification.getRang(rank), selaginaceaeClassification.getRang(rank));
        }
    }

    private void isTheSameRank(@NotNull ICronquistRank rang, @NotNull ICronquistRank rang1) {
        assertEquals("Les Ids doivent être les mêmes", rang.getId(), rang1.getId());
        assertEquals("Les deux rang doivent être au même endroit de la classification", rang.getRankName(), rang1.getRankName());
        assertTrue("Tous les noms doivent être en commun",
                   rang.getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet())
                       .containsAll(
                           rang1.getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet())
                                   )
                  );
        assertEquals("Les deux rangs doivent avoir le même nombre de noms", rang.getNoms().size(), rang1.getNoms().size());
        assertTrue("Toutes les urls doivent être en commun",
                   rang.getUrls().stream().map(IUrl::getUrl).collect(Collectors.toSet())
                       .containsAll(
                           rang1.getUrls().stream().map(IUrl::getUrl).collect(Collectors.toSet())
                                   )
                  );
        assertEquals("Les deux rangs doivent avoir le même nombre d'urls", rang.getUrls().size(), rang1.getUrls().size());
    }


    public void checkThatRankContainsOnlyTheseNames(
        CronquistClassificationBranch selaginaceaeClassification,
        RankName rang,
        @NotNull Set<String> names
                                                   ) {
        String nomDeLaPlanteDeBase = String.join(", ",
                                                 getRankNames(selaginaceaeClassification, selaginaceaeClassification.getRangDeBase().getRankName())
                                                );
        String neDoitContenirQueNNomsDeRangs = "%s ne doit contenir que %n ordres";
        String doitPossederLesNomsDeRang = "L'ordre de %s doit posséder les noms {%s} mais possède les noms {%s}";

        Set<String> selaginaceaeNames = getRankNames(selaginaceaeClassification, rang);
        assertEquals(
            String.format(neDoitContenirQueNNomsDeRangs, nomDeLaPlanteDeBase, names.size()),
            names.size(),
            selaginaceaeNames.size()
                    );
        assertTrue(
            String.format(
                doitPossederLesNomsDeRang,
                nomDeLaPlanteDeBase,
                String.join(", ", names),
                String.join(", ", selaginaceaeNames)
                         ),
            selaginaceaeNames.containsAll(names)
                  );
    }

}
