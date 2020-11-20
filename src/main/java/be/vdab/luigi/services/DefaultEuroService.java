package be.vdab.luigi.services;

import be.vdab.luigi.exceptions.KoersClientException;
import be.vdab.luigi.restclients.KoersClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DefaultEuroService implements EuroService {
    private final KoersClient[] koersClients;
    public DefaultEuroService(KoersClient[] koersClients) {

        this.koersClients = koersClients;
    }

    @Override
    public BigDecimal naarDollar(BigDecimal euro){
        for(var client : koersClients){
            return euro.multiply(client.getDollarKoers())
                    .setScale(2, RoundingMode.HALF_UP);
        }
        throw new KoersClientException("Geen dollar gevonden");
    }
}
