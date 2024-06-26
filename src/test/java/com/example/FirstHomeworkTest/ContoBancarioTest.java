package com.example.FirstHomeworkTest;

import com.example.FirstHomework.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;

public class ContoBancarioTest {

	// Casi di test eccezionali
	@Test
    public void testNumeroContoNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario(null, 1000);
        });
    }

    @Test
    public void testNumeroContoVuoto() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario("", 1000);
        });
    }

    @Test
    public void testSaldoNegativo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario("123456789", -1000);
        });
    }

    

    @Test
    public void testTassoInteresseNegativo() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.calcolaInteressi(-1);
        });
    }

    @Test
    public void testDestinazioneNull() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.trasferimento(null, 100);
        });
    }

    // Casi di test per il costrutture ContoBancario
    @Test
    public void testNumeroContoNullSaldoPositivo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario(null, 1000);
        });
    }

    @Test
    public void testNumeroContoValidoSaldoNegativo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario("123456789", -1000);
        });
    }

    @Test
    public void testNumeroContoValidoSaldoPositivo() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        Assertions.assertEquals(1000, conto.getSaldo());
    }

   

    // Casi di test Deposito e prelievo
    @Test
    public void testImportoNegativoOZeroDeposito() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.deposito(-100);
        });
    }

    @Test
    public void testImportoPositivoInferioreOUgualeAlSaldoDeposito() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        conto.deposito(500);
        Assertions.assertEquals(1500, conto.getSaldo());
    }

    @Test
    public void testPrelievoConSaldoInsufficiente() {
        ContoBancario conto = new ContoBancario("123456789", 1000); // Saldo iniziale di 1000
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.prelievo(1500); // Tentativo di prelevare 1500, che è superiore al saldo disponibile
        });
    }

    @Test
    public void testPrelievoImportoNegativo() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        assertThrows(IllegalArgumentException.class, () -> {
            conto.prelievo(-100);
        });
    }

    @Test
    public void testPrelievoSuccesso() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        double nuovoSaldo = conto.prelievo(500);
        assertEquals(500, nuovoSaldo);
        assertEquals(500, conto.getSaldo()); // Verify that the balance is updated correctly
    }
   
    
    // Casi di test Trasferimento
   @Test
    public void testDestinazioneValidaImportoPositivoInferioreOUgualeAlSaldoTrasferimento() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        ContoBancario destinazione = new ContoBancario("987654321", 0);
        conto.trasferimento(destinazione, 500);
        Assertions.assertEquals(500, conto.getSaldo());
        Assertions.assertEquals(500, destinazione.getSaldo());
    }

    @Test
    public void testDestinazioneValidaImportoSuperioreAlSaldoTrasferimento() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        ContoBancario destinazione = new ContoBancario("987654321", 0);
        Assertions.assertFalse(conto.trasferimento(destinazione, 1500));
    }
    
    // Casi di Test CalcolaInteressi
    @Test
    public void testTassoInteresseNegativoOZeroCalcolaInteressi() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.calcolaInteressi(-1);
        });
    }

   
    
    //test cancellaInteressi
    @Test
    public void testCancellaInteressi() {
        // Crea un nuovo conto bancario con un saldo iniziale
        ContoBancario conto = new ContoBancario("123456789", 1000);
        
        // Calcola gli interessi con un tasso del 5%
        conto.calcolaInteressi(5);
        
        // Verifica che la mappa degli interessi non sia vuota
        assertFalse(conto.getInteressi().isEmpty());
        
        // Cancella tutti gli interessi
        conto.cancellaInteressi();
        
        // Verifica che la mappa degli interessi sia vuota
        assertTrue(conto.getInteressi().isEmpty());
    }

    
    //test get interessi
    @Test
    public void testGetInteressi() {
        // Crea un nuovo conto bancario con un saldo iniziale
        ContoBancario conto = new ContoBancario("123456789", 1000);
        
        // Verifica che la mappa degli interessi sia inizialmente vuota
        assertTrue(conto.getInteressi().isEmpty());
        
        // Calcola gli interessi con un tasso del 5%
        conto.calcolaInteressi(5);
        
        // Verifica che la mappa degli interessi non sia vuota
        assertFalse(conto.getInteressi().isEmpty());
        
        // Verifica che gli interessi siano stati calcolati correttamente
        assertEquals(50.0, conto.getInteressi().get("123456789"));
    }

    
    // T19 Test del NumeroConto con spazi bianchi
    @Test
    public void testNumeroContoConSpaziBianchi() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario(" 123456789", 1000);
        });
    }
    
    // T20 Test NumeroConto con caratteri speciali
    @Test
    public void testNumeroContoConCaratteriSpeciali() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContoBancario("123456789!", 1000);
        });
    }
    
    // T21 Test dei rollback delle transazioni
    @Test
    public void testRollbackTrasferimento() {
        ContoBancario conto = new ContoBancario("123456789", 1000);
        ContoBancario destinazione = new ContoBancario("987654321", 500);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            conto.trasferimento(destinazione, -100);  // Importo negativo, dovrebbe fallire
        });

        // Verifica che i saldi rimangano invariati
        Assertions.assertEquals(1000, conto.getSaldo());
        Assertions.assertEquals(500, destinazione.getSaldo());
    }
}