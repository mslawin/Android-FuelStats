package pl.mslawin.fuelstats.domain.statistic;

/**
* Created by mslawin on 10.02.15.
*/
public class FuelConsumption {
    private float amount;
    private float kilometersAmount;

    public void addAmount(float amountToAdd) {
        this.amount += amountToAdd;
    }

    public void addKilometers(float amountToAdd) {
        this.kilometersAmount += amountToAdd;
    }

    public float getAmount() {
        return amount;
    }

    public float getKilometersAmount() {
        return kilometersAmount;
    }
}
