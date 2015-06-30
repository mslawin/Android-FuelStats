package pl.mslawin.fuelstats.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Currency;
import java.util.Locale;

import pl.mslawin.fuelstats.view.AddStat;

/**
 * Created by mslawin on 27.12.14.
 */
@DatabaseTable
public class Stat {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private float fuelAmount;

    @DatabaseField
    private float price;

    @DatabaseField
    private DateTime date;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Station station;

    @DatabaseField
    private float kilometers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(float fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public float getKilometers() {
        return kilometers;
    }

    public void setKilometers(float kilometers) {
        this.kilometers = kilometers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color='black'><b>")
                .append(station.getStationName())
                .append("</b>, ")
                .append(date.toString(AddStat.ENTER_DATE_FORMATTER))
                .append(", ")
                .append(fuelAmount)
                .append("l, ")
                .append(price)
                .append(Currency.getInstance(Locale.getDefault()).getSymbol())
                .append(", ")
                .append(kilometers)
                .append("km</font>");
        return sb.toString();
    }
}
