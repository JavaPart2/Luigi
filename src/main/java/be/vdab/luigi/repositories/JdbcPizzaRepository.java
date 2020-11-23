package be.vdab.luigi.repositories;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exceptions.PizzaNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcPizzaRepository implements PizzaRepository{
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private final RowMapper<Pizza> pizzaMapper =
            (result, rowNum) -> new Pizza(result.getInt("id"),
                    result.getString("naam"),
                    result.getBigDecimal("prijs"),
                    result.getBoolean("pikant"));
    private final RowMapper<BigDecimal> prijsMapper =
            (result, rowNum) -> result.getBigDecimal("prijs");

    public JdbcPizzaRepository(JdbcTemplate template) {
        this.template = template;
        this.insert = new SimpleJdbcInsert(template)
                .withTableName("pizzas").usingGeneratedKeyColumns("id");
    }

    @Override
    public long create(Pizza pizza) {
        var kolomWaarden = Map.of("naam", pizza.getNaam(),
                "prijs", pizza.getPrijs(),
                "pikant", pizza.isPikant());
        var id = insert.executeAndReturnKey(kolomWaarden);
        return id.longValue();
    }

    @Override
    public void update(Pizza pizza) {
        var sql = "update pizzas set naam=?, prijs=?, pikant=? where id = ?";
        if (template.update(sql, pizza.getNaam(), pizza.getPrijs(), pizza.isPikant(), pizza.getId()) == 0){
            throw new PizzaNietGevondenException();
        }

    }

    @Override
    public void delete(Pizza pizza) {
        template.update("delete from pizzas where id = ?", pizza.getId());

    }

    @Override
    public void deleteById(int id) {
        template.update("delete from pizzas where id = ?", id);
    }

    @Override
    public List<Pizza> findAll() {
        var sql = "select id, naam, prijs, pikant from pizzas order by id";
        return template.query(sql,pizzaMapper);
    }

    @Override
    public Optional<Pizza> findById(int id) {
        try {
            var sql = "select id, naam, prijs, pikant from pizzas where id = ?";
            return Optional.of(template.queryForObject(sql, pizzaMapper, id));
        }catch (IncorrectResultSizeDataAccessException ex){
            return Optional.empty();
        }
    }

    @Override
    public List<Pizza> findByPriceBetween(BigDecimal van, BigDecimal tot) {
        var sql = "select id, naam, prijs, pikant from pizzas "
                + "where prijs between ? and ? order by prijs";
        return template.query(sql, pizzaMapper, van, tot);
    }

    @Override
    public long findAantal() {
        return template.queryForObject("select count(*) from pizzas", Long.class);
    }

    @Override
    public List<BigDecimal> findUniekePrijzen() {
        return template.query("select distinct prijs from pizzas order by prijs",
                prijsMapper);
    }

    @Override
    public List<Pizza> findByPrice(BigDecimal prijs) {
        var sql = "select id, naam, prijs, pikant from pizzas where prijs = ? order by naam";
        return template.query(sql, pizzaMapper, prijs);
    }

    @Override
    public List<Pizza> findByIds(Set<Integer> ids) {
        if (ids.isEmpty()){
            return List.of();
        }
        var sql = "select id, naam, prijs, pikant from pizzas where id in ( "
                + "?,".repeat(ids.size()-1) + "?) order by id";
        return template.query(sql, pizzaMapper, ids.toArray());
    }
}
