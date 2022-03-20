package fr.syncrase.ecosyst.domain.enumeration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * The CronquistTaxonomikRanks enumeration.
 */
public enum CronquistTaxonomikRanks {
    SUPERREGNE("SuperRegne"),
    REGNE("Regne"),
    SOUSREGNE("SousRegne"),
    RAMEAU("Rameau"),
    INFRAREGNE("InfraRegne"),
    SUPEREMBRANCHEMENT("SuperEmbranchement"),
    EMBRANCHEMENT("Embranchement"),
    SOUSEMBRANCHEMENT("SousEmbranchement"),
    INFRAEMBRANCHEMENT("InfraEmbranchement"),
    MICROEMBRANCHEMENT("MicroEmbranchement"),
    SUPERCLASSE("SuperClasse"),
    CLASSE("Classe"),
    SOUSCLASSE("SousClasse"),
    INFRACLASSE("InfraClasse"),
    SUPERORDRE("SuperOrdre"),
    ORDRE("Ordre"),
    SOUSORDRE("SousOrdre"),
    INFRAORDRE("InfraOrdre"),
    MICROORDRE("MicroOrdre"),
    SUPERFAMILLE("SuperFamille"),
    FAMILLE("Famille"),
    SOUSFAMILLE("SousFamille"),
    TRIBU("Tribu"),
    SOUSTRIBU("SousTribu"),
    GENRE("Genre"),
    SOUSGENRE("SousGenre"),
    SECTION("Section"),
    SOUSSECTION("SousSection"),
    ESPECE("Espece"),
    SOUSESPECE("SousEspece"),
    VARIETE("Variete"),
    SOUSVARIETE("SousVariete"),
    FORME("Forme"),
    SOUSFORME("SousForme");

    private static CronquistTaxonomikRanks[] allRanks = values();
    private final String value;

    CronquistTaxonomikRanks(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Contract(pure = true)
    public @Nullable CronquistTaxonomikRanks getRangSuperieur() {
        int parentOrdinal = this.ordinal() - 1;
        if (parentOrdinal > -1) {
            return allRanks[parentOrdinal];
        } else {
            return null;
        }
    }

    @Contract(pure = true)
    public @Nullable CronquistTaxonomikRanks getRangInferieur() {
        int childOrdinal = this.ordinal() + 1;
        if (childOrdinal < allRanks.length) {
            return allRanks[childOrdinal];
        } else {
            return null;
        }
    }
}
