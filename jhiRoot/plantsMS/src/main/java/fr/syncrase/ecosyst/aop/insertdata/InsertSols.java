package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.SolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class InsertSols {

    private final Logger log = LoggerFactory.getLogger(InsertSols.class);
    private SolRepository solRepository;

    public InsertSols(SolRepository solRepository) {
        this.solRepository = solRepository;
    }

    public void insertAllSols() {
        try {
            for (String type : new String[]{"argileuse", "calcaire", "humifere", "sableuse"}) {
                getOrInsertSol(new Sol().type(type));
            }
            for (String type : new String[]{"tresPauvre", "pauvre", "normale", "riche", "tresRiche"}) {
                getOrInsertSol(new Sol().richesse(type));
            }
        } catch (Exception e) {
            log.error("Error when trying to insert in table Feuillage. {" + e.getMessage() + "}");
        }
    }

    private Sol getOrInsertSol(Sol sol) {
        if (!solRepository.exists(Example.of(sol))) {
            solRepository.save(sol);
        } else {
            Optional<Sol> returned = solRepository.findOne(Example.of(sol));
            if (returned.isPresent()) {
                sol = returned.get();
                log.info("Existing sol : " + sol);
            } else {
                log.error("Unable to get instance of : " + sol);
            }
        }
        return sol;
    }
}
