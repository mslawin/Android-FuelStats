package pl.mslawin.fuelstats.domain.statistic;

/**
 * Created by mslawin on 10.02.15.
 */
public class FuelStatistics {

    private final float fuelAmount;
    private final float kilometersAmount;
    private final float price;

    public FuelStatistics(float fuelAmount, float kilometersAmount, float price) {
        this.fuelAmount = fuelAmount;
        this.kilometersAmount = kilometersAmount;
        this.price = price;
    }

    public float getFuelAmount() {
        return fuelAmount;
    }

    public float getKilometersAmount() {
        return kilometersAmount;
    }

    public float getPrice() {
        return price;
    }
}
