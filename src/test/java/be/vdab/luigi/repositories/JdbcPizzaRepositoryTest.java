package be.vdab.luigi.repositories;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exceptions.PizzaNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(JdbcPizzaRepository.class)
@Sql("/insertPizzas.sql")
public class JdbcPizzaRepositoryTest
        extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String PIZZAS = "pizzas";
    private final JdbcPizzaRepository repository;

    public JdbcPizzaRepositoryTest(JdbcPizzaRepository repository) {

        this.repository = repository;
    }

    @Test
    void findAantal(){
        assertThat(repository.findAantal())
                .isEqualTo(super.countRowsInTable(PIZZAS));
    }

    @Test
    void findAllPizzasSortedById(){
        assertThat(repository.findAll())
                .hasSize(super.countRowsInTable(PIZZAS))
                .extracting(pizza -> pizza.getId()).isSorted();
    }

    @Test
    void create(){
        var id = repository.create(new Pizza(0, "test2", BigDecimal.TEN, false));
        assertThat(id).isPositive();
        assertThat(super.countRowsInTableWhere(PIZZAS, "id = " + id)).isOne();
    }

    private int idVanTestPizza(){
        return super.jdbcTemplate.queryForObject(
                "select id from pizzas where naam='test'", Integer.class);
    }

    private int idVanTest2Pizza(){
        return super.jdbcTemplate.queryForObject(
                "select id from pizzas where naam='test2'", Integer.class);
    }

    @Test
    void delete(){
        var id = idVanTestPizza();
        repository.deleteById(id);
        assertThat(super.countRowsInTableWhere(PIZZAS, "id = " + id))
                .isZero();
    }

    @Test
    void findById(){
        assertThat(repository.findById(idVanTestPizza()).get().getNaam())
                .isEqualTo("test");
    }

    @Test
    void findByOnbestaandeId(){
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void update(){
        var id = idVanTestPizza();
        var pizza = new Pizza(id, "test", BigDecimal.ONE, false);
        repository.update(pizza);
        assertThat(super.jdbcTemplate.queryForObject(
                "select prijs from pizzas where id=?", BigDecimal.class, id))
                .isOne();
    }

    @Test
    void updateOnbestaandePizzaFout(){
        assertThatExceptionOfType(PizzaNietGevondenException.class)
                .isThrownBy(() -> repository.update(new Pizza(-1, "test",
                        BigDecimal.ONE, false)));
    }

    @Test
    void findByPrijsBetween(){
        assertThat(repository.findByPriceBetween(BigDecimal.ONE, BigDecimal.TEN))
                .hasSize(super.countRowsInTableWhere(PIZZAS,
                        "Prijs between 1 and 10"))
                .allSatisfy(pizza -> assertThat(pizza.getPrijs())
                        .isBetween(BigDecimal.ONE, BigDecimal.TEN))
                .extracting(pizza -> pizza.getPrijs()).isSorted();
    }

    @Test
    void findUniekePrijzenOplopend(){
        assertThat(repository.findUniekePrijzen())
                .hasSize(super.jdbcTemplate.queryForObject(
                        "select count(distinct prijs) from pizzas", Integer.class))
                .doesNotHaveDuplicates()
                .isSorted();
    }

    @Test
    void findByPrijs(){
        assertThat(repository.findByPrice(BigDecimal.TEN))
                .hasSize(super.countRowsInTableWhere(PIZZAS, "prijs = 10"))
                .extracting(pizza -> pizza.getNaam().toLowerCase())
                .isSorted();
    }

    @Test
    void findByIds(){
        int id1 = idVanTestPizza();
        int id2 = idVanTest2Pizza();
        assertThat(repository.findByIds(Set.of(id1,id2)))
                .extracting(pizza -> pizza.getId())
                .containsOnly(id1, id2)
                .isSorted();
    }

    @Test
    void findByIdsLeeg(){
        assertThat(repository.findByIds(Set.of())).isEmpty();
    }

    @Test
    void findByIdsleegOnbestaandIds(){
        assertThat(repository.findByIds(Set.of(-1))).isEmpty();
    }
}
