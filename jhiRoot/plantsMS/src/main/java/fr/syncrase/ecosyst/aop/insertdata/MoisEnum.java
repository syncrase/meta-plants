package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Mois;

import java.util.HashMap;
import java.util.Map;

public enum MoisEnum {
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

    private static final Map<Double, MoisEnum> BY_NUMERO = new HashMap<>();
    private static final Map<String, MoisEnum> BY_DESCRIPTION = new HashMap<>();

    static {
        for (MoisEnum e : MoisEnum.values()) {
            BY_DESCRIPTION.put(e.description, e);
            BY_NUMERO.put(e.numero, e);
        }
    }

    MoisEnum(Double numero, String description) {
        this.numero = numero;
        this.description = description;
    }

    public Double getNumero() {
        return numero;
    }

    public String getDescription() {
        return description;
    }

    public Mois getMois() {
        return new Mois().nom(description).numero(numero);
    }

    /**
     * Retourne le label du mois à partir de sa description
     *
     * @param description
     * @return
     */
    public static MoisEnum labelParDescription(String description) {

        final MoisEnum value = BY_DESCRIPTION.get(description);
        return value != null ? value : new Object() {
            public MoisEnum exception() {
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
    public static MoisEnum labelParNumero(double numero) {

        final MoisEnum value = BY_NUMERO.get(numero);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("Coef incconu : " + numero);
    }

}
