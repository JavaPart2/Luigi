package be.vdab.luigi.services;

import be.vdab.luigi.domain.Pizza;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PizzaService {
    long create(Pizza pizza);
    void update(Pizza pizza);
    void delete(int id);
    List<Pizza> findAll();
    Optional<Pizza> findById(int id);
    List<Pizza> findByPrijsBetween(BigDecimal van, BigDecimal tot);
    long findAantal();
    List<BigDecimal> findUniekePrijzen();
    List<Pizza> findByPrijs(BigDecimal prijs);
    List<Pizza> findByIds(Set<Integer> ids);
}
