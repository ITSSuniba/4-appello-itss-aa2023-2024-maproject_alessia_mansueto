package com.example.FirstHomework;
import java.util.HashMap;
import java.util.Map;

public class ContoBancario{
    public String NumeroConto;
    public double saldo;
    public Map<String, Double> interessi;

    //Costruttore per la classe ContoBancario
    public ContoBancario(String NumeroConto, double saldo) {
        // Controllo di validità per il numero di conto
        if (NumeroConto == null ||!NumeroConto.matches("\\d{9}")) { // il numero di conto deve essere composto da 9 cifre
            throw new IllegalArgumentException("Il numero di conto non è valido. Deve essere composto da 9 cifre.");
        }
        this.NumeroConto = NumeroConto;
        // Controllo di validità per il saldo
        if (saldo < 0) {
            throw new IllegalArgumentException("Il saldo iniziale non può essere negativo.");
        }
        this.NumeroConto = NumeroConto;
        this.saldo = saldo;
        this.interessi = new HashMap<>();
    }

  //Metodo per effettuare un deposito sul conto bancario
    public double deposito(double importo){
    	 if (importo <= 0) {
            throw new IllegalArgumentException("Importo di deposito non può essere negativo.");
        }
        saldo += importo;
        return saldo;
    }

    //Metodo per effettuare un prelievo dal conto bancario
    public double prelievo(double importo) {
    	 if (importo <= 0) {
            throw new IllegalArgumentException("Importo di prelievo non può essere negativo o zero.");
        }
        if (saldo >= importo) {
            saldo -= importo;
            return saldo;
        } else {
            throw new IllegalArgumentException("Saldo insufficiente per effettuare il prelievo.");
        }
    }

    //Metodo per trasferire denaro da questo conto a un altro conto bancario
    public boolean trasferimento(ContoBancario destinazione, double importo) {
        if (destinazione == null) {
            throw new IllegalArgumentException("Il conto di destinazione non può essere null.");
        }
        if (importo <= 0) {
            throw new IllegalArgumentException("Importo di trasferimento deve essere positivo.");
        }
        if (saldo >= importo) {
            saldo -= importo;
            destinazione.deposito(importo);
            return true; // Restituisci true se il trasferimento è avvenuto con successo
        } else {
            return false; // Restituisci false se il trasferimento non è riuscito
        }
    }

    //Metodo per calcolare gli interessi sul saldo del contoo bancario
    public double calcolaInteressi(double tassoInteresse) {
        if (tassoInteresse <= 0) {
            throw new IllegalArgumentException("Il tasso di interesse non può essere negativo.");
        }
        double interesse = saldo * tassoInteresse / 100;
        interessi.put(NumeroConto, interesse);
        return interesse;
    }

    public void cancellaInteressi() {
        interessi.clear();
    }

    //Metodo per ottenere il saldo attuale del conto bancario
    public double getSaldo(){
        return saldo;
    }

    //Metodo per ottenere gli interessi calcolati per il conto bancario
    public Map<String, Double> getInteressi() {
        return interessi;
    }

}