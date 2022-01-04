package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.CronquistRank;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CronquistUtils {

    /**
     * Deux rangs taxonomiques sont séparés par d'autres rangs dont on ne connait pas forcément le nom.<br>
     * Une valeur par défaut permet de lier ces deux rangs avec des rangs vides.<br>
     * Si ultérieurement ces rangs sont déterminés, les valeurs par défaut sont mises à jour
     */
    public static final String DEFAULT_NAME_FOR_CONNECTOR_RANK = null;

    public static boolean isRangIntermediaire(@NotNull CronquistRank rankToInsert) {
        return Objects.equals(rankToInsert.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK);
    }
}
