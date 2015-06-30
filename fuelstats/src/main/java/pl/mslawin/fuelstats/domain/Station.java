package pl.mslawin.fuelstats.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mslawin on 27.12.14.
 */

@DatabaseTable
public class Station {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String stationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
