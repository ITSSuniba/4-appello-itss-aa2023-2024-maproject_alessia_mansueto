package com.example.SecondHomework;

import com.example.SecondHomework.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.jqwik.api.*;
import net.jqwik.api.constraints.CharRange;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;


public class ContoBancarioTest {

	  @Property(tries = 100)
	    @Report(Reporting.GENERATED)
	    @StatisticsReport(format = Histogram.class)
	    void deposito_nonNegativo(
	            @ForAll("numeroConto") String numeroConto,
	            @ForAll @Positive double saldo,
	            @ForAll double importo) {

	        ContoBancario conto = new ContoBancario(numeroConto, saldo);
	        double saldoPrecedente = conto.getSaldo();

	        if (importo <= 0) {
	            assertThrows(IllegalArgumentException.class, () -> conto.deposito(importo));
	            Statistics.collect("Negative Deposit", importo);
	        } else {
	            double saldoDopoDeposito = conto.deposito(importo);
	            assertEquals(saldoPrecedente + importo, saldoDopoDeposito);
		        Statistics.collect("Importo di deposito", importo);
	        }
	    }

	@Property(tries = 100)
	@Report(Reporting.GENERATED)
	@StatisticsReport(format = Histogram.class)
	void prelievo_nonNegativo(
	        @ForAll("numeroConto") String numeroConto,
	        @ForAll @Positive double saldo,
	        @ForAll double importo) {

	    ContoBancario conto = new ContoBancario(numeroConto, saldo);
	    double saldoPrecedente = conto.getSaldo();

	    if (importo <= 0) {
	        // Se l'importo è negativo o zero, lancia un'eccezione IllegalArgumentException
	        assertThrows(IllegalArgumentException.class, () -> conto.prelievo(importo));
	        Statistics.collect("Negative Withdrawal");
	    } else if (saldo >= importo) {
	        // Se l'importo è positivo e il saldo è sufficiente, effettua il prelievo
	        double saldoDopoPrelievo = conto.prelievo(importo);
	        assertEquals(saldoPrecedente - importo, saldoDopoPrelievo);
	        Statistics.collect("Valid Withdrawal");
	    } else {
	        // Se l'importo è positivo ma il saldo è insufficiente, lancia un'eccezione IllegalArgumentException
	        assertThrows(IllegalArgumentException.class, () -> conto.prelievo(importo));
	        Statistics.collect("Insufficient Balance");
	    }
	}


    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void trasferimento_valido(
            @ForAll("numeroConto") String numeroContoSorgente,
            @ForAll("numeroConto") String numeroContoDestinazione,
            @ForAll @Positive double saldoSorgente,
            @ForAll @Positive double saldoDestinazione,
            @ForAll double importo) {

        ContoBancario contoSorgente = new ContoBancario(numeroContoSorgente, saldoSorgente);
        ContoBancario contoDestinazione = new ContoBancario(numeroContoDestinazione, saldoDestinazione);

        if (importo <= 0 || contoDestinazione == null) {
            assertThrows(IllegalArgumentException.class, () -> contoSorgente.trasferimento(contoDestinazione, importo));
            Statistics.collect("Trasferimento negativo", importo);
        } else {
            boolean trasferito = contoSorgente.trasferimento(contoDestinazione, importo);

            if (trasferito) {
                assertEquals(saldoSorgente - importo, contoSorgente.getSaldo(), 0.001);
                assertEquals(saldoDestinazione + importo, contoDestinazione.getSaldo(), 0.001);
                Statistics.collect("Trasferimento positivo", importo);
            } else {
                assertEquals(saldoSorgente, contoSorgente.getSaldo(), 0.001);
                assertEquals(saldoDestinazione, contoDestinazione.getSaldo(), 0.001);
                Statistics.collect("Trasferimento fallito", importo);
            }
        }
    }

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void calcolaInteressi_valido(
            @ForAll("numeroConto") String numeroConto,
            @ForAll @Positive double saldo,
            @ForAll double tassoInteresse) {

        ContoBancario conto = new ContoBancario(numeroConto, saldo);

        if (tassoInteresse <= 0) {
            assertThrows(IllegalArgumentException.class, () -> conto.calcolaInteressi(tassoInteresse));
            Statistics.collect("Negative Interest Rate");
        } else {
            double interesseCalcolato = conto.calcolaInteressi(tassoInteresse);

            assertTrue(interesseCalcolato >= 0);
            assertTrue(conto.getInteressi().containsKey(numeroConto));
            assertEquals(interesseCalcolato, conto.getInteressi().get(numeroConto), 0.001);
            Statistics.collect("Valid Interest Calculation");
        }
    }

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void testCostruttore(
            @ForAll("numeroConto") String numeroConto,
            @ForAll double saldo) {

        if (numeroConto == null || !numeroConto.matches("\\d{9}")) {
            assertThrows(IllegalArgumentException.class, () -> new ContoBancario(numeroConto, saldo));
            Statistics.collect("Invalid Account Number");
        } else if (saldo < 0) {
            assertThrows(IllegalArgumentException.class, () -> new ContoBancario(numeroConto, saldo));
            Statistics.collect("Negative Initial Balance");
        } else {
            ContoBancario conto = new ContoBancario(numeroConto, saldo);
            assertEquals(numeroConto, conto.NumeroConto);
            assertEquals(saldo, conto.getSaldo());
            Statistics.collect("Valid Account Creation");
       }
    }

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void testCancellaInteressi(
            @ForAll("numeroConto") String numeroConto,
            @ForAll @Positive double saldo,
            @ForAll @Positive double tassoInteresse) {

        ContoBancario conto = new ContoBancario(numeroConto, saldo);
        conto.calcolaInteressi(tassoInteresse);
        assertFalse(conto.getInteressi().isEmpty());

        conto.cancellaInteressi();
        assertTrue(conto.getInteressi().isEmpty());
        Statistics.collect("Interest Cleared");
    }

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void testNullDestinationAccount(
            @ForAll("numeroConto") String numeroContoSorgente,
            @ForAll @Positive double saldoSorgente,
            @ForAll double importo) {

        ContoBancario contoSorgente = new ContoBancario(numeroContoSorgente, saldoSorgente);
        assertThrows(IllegalArgumentException.class, () -> contoSorgente.trasferimento(null, importo));
        Statistics.collect("Null Destination Account");
    }

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void testInvalidAccountNumber(
            @ForAll("invalidAccountNumber") String numeroConto,
            @ForAll @Positive double saldo) {

        assertThrows(IllegalArgumentException.class, () -> new ContoBancario(numeroConto, saldo));
        Statistics.collect("Invalid Account Number");
    }

    @Provide
    Arbitrary<String> invalidAccountNumber() {
        return Arbitraries.strings().withCharRange('0', '9').ofMinLength(1).ofMaxLength(8);
    }
    
    @Provide
    Arbitrary<String> numeroConto() {
        return Arbitraries.strings().withCharRange('0', '9').ofLength(9);
    }

    @Provide
    Arbitrary<Double> positiveOrZero() {
        return Arbitraries.doubles().greaterOrEqual(0);
    }

    @Provide
    Arbitrary<Double> positive() {
        return Arbitraries.doubles().greaterThan(0);
    }
}