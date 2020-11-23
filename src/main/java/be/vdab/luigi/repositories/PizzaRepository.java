package be.vdab.luigi.repositories;

import be.vdab.luigi.domain.Pizza;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PizzaRepository {
    long create(Pizza pizza);
    void update(Pizza pizza);
    void delete(Pizza pizza);
    void deleteById(int id);
    List<Pizza> findAll();
    Optional<Pizza> findById(int id);
    List<Pizza> findByPriceBetween(BigDecimal van, BigDecimal tot);
    long findAantal();
    List<BigDecimal> findUniekePrijzen();
    List<Pizza> findByPrice(BigDecimal prijs);
    List<Pizza> findByIds(Set<Integer> ids);
}
