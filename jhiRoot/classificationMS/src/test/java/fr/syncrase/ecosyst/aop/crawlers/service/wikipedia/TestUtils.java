package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.entities.IClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import fr.syncrase.ecosyst.domain.classification.entities.IUrl;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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
        return corylopsisClassification.getRang(ordre).getNomsWrappers().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
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
     * @param classification1 classification à comparer
     * @param classification2 classification à comparer
     */
    public void assertThatClassificationsAreTheSame(
        @NotNull CronquistClassificationBranch classification1,
        @NotNull CronquistClassificationBranch classification2
                                                   ) {
        assert classification1.getRangDeBase().getRankName().equals(classification2.getRangDeBase().getRankName());
        @NotNull RankName lowestRank = classification1.getRangDeBase().getRankName();

        assertThatClassificationsAreTheSame(classification1, classification2, RankName.SUPERREGNE, lowestRank);
    }

    public void assertThatClassificationsAreTheSame(
        @NotNull CronquistClassificationBranch neriifoliaClassification,
        @NotNull CronquistClassificationBranch selaginaceaeClassification,
        @NotNull RankName highestRank,
        @NotNull RankName lowestRank
                                                   ) {

        for (RankName rank = highestRank; Objects.requireNonNull(rank).isHighestRankOf(lowestRank); rank = rank.getRangInferieur()) {
            assertThatIsTheSameRank(neriifoliaClassification.getRang(rank), selaginaceaeClassification.getRang(rank));
        }
    }

    private void assertThatIsTheSameRank(@NotNull ICronquistRank rang, @NotNull ICronquistRank rang1) {
        assertEquals("Les Ids doivent être les mêmes", rang.getId(), rang1.getId());
        assertEquals("Les deux rang doivent être au même endroit de la classification", rang.getRankName(), rang1.getRankName());
        assertTrue("Tous les noms doivent être en commun",
                   rang.getNomsWrappers().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet())
                       .containsAll(
                           rang1.getNomsWrappers().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet())
                                   )
                  );
        assertEquals("Les deux rangs doivent avoir le même nombre de noms", rang.getNomsWrappers().size(), rang1.getNomsWrappers().size());
        assertTrue("Toutes les urls doivent être en commun",
                   rang.getIUrls().stream().map(IUrl::getUrl).collect(Collectors.toSet())
                       .containsAll(
                           rang1.getIUrls().stream().map(IUrl::getUrl).collect(Collectors.toSet())
                                   )
                  );
        assertEquals("Les deux rangs doivent avoir le même nombre d'urls", rang.getIUrls().size(), rang1.getIUrls().size());
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

    public void checkThatEachRankOwnsAsManyUrlAsNames(@NotNull CronquistClassificationBranch classification) {
        ICronquistRank cronquistRank;
        for (Map.Entry<RankName, ICronquistRank> rankEntry : classification.getClassificationBranch().entrySet()) {
            cronquistRank = rankEntry.getValue();
            if (cronquistRank.isRangSignificatif()) {
                assertEquals("Un rang significatif doit posséder autant de noms que d'urls", cronquistRank.getNomsWrappers().size(), cronquistRank.getIUrls().size());
            }
        }

    }

    public void assertThatEachLinkNameHasOnlyOneName(@NotNull CronquistClassificationBranch classification) {
        ICronquistRank cronquistRank;
        for (Map.Entry<RankName, ICronquistRank> rankEntry : classification.getClassificationBranch().entrySet()) {
            cronquistRank = rankEntry.getValue();
            if (cronquistRank.isRangDeLiaison()) {
                assertEquals("Un rang de liaison ne doit posséder qu'un seul nom", 1, cronquistRank.getNomsWrappers().size());
            }
        }
    }

    public Set<Long> getRanksId(
        CronquistClassificationBranch classification,
                                @NotNull RankName highestRank,
                                @NotNull RankName lowestRank
                               ) {
        Set<Long> idsDesRangsDeLiaisonQuiDoiventDisparaitrent = new HashSet<>();
        for (RankName rank = highestRank; Objects.requireNonNull(rank).isHighestRankOrEqualOf(lowestRank); rank = rank.getRangInferieur()) {
            idsDesRangsDeLiaisonQuiDoiventDisparaitrent.add(classification.getRang(rank).getId());
        }
        return idsDesRangsDeLiaisonQuiDoiventDisparaitrent;
    }
}
