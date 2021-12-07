package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.repository.RacineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class InsertRacines {

    private final Logger log = LoggerFactory.getLogger(InsertRacines.class);
    private RacineRepository racineRepository;

    public InsertRacines(RacineRepository racineRepository) {
        this.racineRepository = racineRepository;
    }

    public void insertAllRacines() {
        try {
            for (String type : new String[]{"pivotante", "fasciculaire", "adventice", "tracante", "contrefort", "crampon", "echasse", "aerienne", "liane", "ventouse", "pneumatophore"}) {
                getOrInsertRacine(new Racine().type(type));
            }
        } catch (Exception e) {
            log.error("Error when trying to insert in table Racine. {" + e.getMessage() + "}");
        }
    }

    private Racine getOrInsertRacine(Racine racine) {
        if (!racineRepository.exists(Example.of(racine))) {
            racineRepository.save(racine);
        } else {
            Optional<Racine> returned = racineRepository.findOne(Example.of(racine));
            if (returned.isPresent()) {
                racine = returned.get();
                log.info("Existing racine : " + racine);
            } else {
                log.error("Unable to get instance of : " + racine);
            }
        }
        return racine;
    }
}
