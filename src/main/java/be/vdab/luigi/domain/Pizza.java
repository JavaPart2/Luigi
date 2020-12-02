package be.vdab.luigi.domain;

import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

public class Pizza {
    private final int id;
    private final String naam;
    @NumberFormat(pattern = "#,##0.00")
    private final BigDecimal prijs;
    private final boolean pikant;

    public Pizza(int id, String naam, BigDecimal prijs, boolean pikant) {
        this.id = id;
        this.naam = naam;
        this.prijs = prijs;
        this.pikant = pikant;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public boolean isPikant() {
        return pikant;
    }
}
