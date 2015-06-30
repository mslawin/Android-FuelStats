package pl.mslawin.fuelstats.service;


import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.joda.time.DateTime;
import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.collect.Collections2;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import pl.mslawin.fuelstats.dao.DatabaseHelper;
import pl.mslawin.fuelstats.domain.Stat;
import pl.mslawin.fuelstats.domain.Station;
import pl.mslawin.fuelstats.domain.statistic.FuelConsumption;
import pl.mslawin.fuelstats.domain.statistic.FuelStatistics;

/**
 * Created by mslawin on 27.12.14.
 */
public class StatService {

    private DatabaseHelper helper;

    public void saveStat(String stationName, DateTime dateTime, float fuelAmount, float price,
                         float kilometers, Context context) throws SQLException {
        initHelper(context);

        Stat stat = new Stat();
        List<Station> stations = helper.getStationDao().queryForAll();
        if (CollectionUtils.isEmpty(stations)) {
            stat.setStation(createNewStation(stationName, helper));
        } else {
            for (Station station : stations) {
                if (station.getStationName().equalsIgnoreCase(stationName)) {
                    stat.setStation(station);
                } else {
                    stat.setStation(createNewStation(stationName, helper));
                }
            }
        }
        Dao<Stat, Integer> statDao = helper.getStatDao();
        QueryBuilder<Stat, Integer> queryBuilder = statDao.queryBuilder();
        queryBuilder.limit(1L);
        queryBuilder.orderBy("kilometers", true);
        List<Stat> lastStat = statDao.query(queryBuilder.prepare());
        if (CollectionUtils.isNotEmpty(lastStat)) {
            lastStat.get(0).setKilometers(kilometers);
            statDao.update(lastStat.get(0));
        }
        stat.setFuelAmount(fuelAmount);
        stat.setPrice(price);
        stat.setDate(dateTime);
        statDao.create(stat);
    }

    private Station createNewStation(String stationName, DatabaseHelper helper) throws SQLException {
        Station station = new Station();
        station.setStationName(stationName);
        helper.getStationDao().create(station);
        return station;
    }

    public List<Stat> getAllStats(Context context) throws SQLException {
        initHelper(context);

        List<Stat> allStats = helper.getStatDao().queryForAll();
        Iterator<Stat> statIterator = allStats.iterator();
        while (statIterator.hasNext()) {
            Stat currentStat = statIterator.next();
            if (currentStat.getKilometers() == 0) {
                statIterator.remove();
            }
        }
        return allStats;
    }

    public String getBestStatsFuel(Context context) throws SQLException {
        return getBestStats(context, true, "l");
    }

    public String getBestStatsPrice(Context context) throws SQLException {

        return getBestStats(context, false, Currency.getInstance(Locale.getDefault()).getSymbol());
    }

    private String getBestStats(Context context, boolean addFuel, String unit) throws SQLException {
        List<Stat> stats = getAllStats(context);
        Map<String, FuelConsumption> bestStationsFuelConsumption = new HashMap<String, FuelConsumption>();

        for (Stat stat : stats) {
            String stationName = stat.getStation().getStationName();
            if (!bestStationsFuelConsumption.containsKey(stationName)) {
                bestStationsFuelConsumption.put(stationName, new FuelConsumption());
            }

            bestStationsFuelConsumption.get(stationName).addAmount(addFuel ? stat.getFuelAmount()
                    : stat.getPrice());
            bestStationsFuelConsumption.get(stationName).addKilometers(stat.getKilometers());
        }

        Map<String, FuelConsumption> sortedBestStationsFuelConsumption =
                new TreeMap<String, FuelConsumption>(StatComparator.fuelConsumptionComparator(bestStationsFuelConsumption));
        sortedBestStationsFuelConsumption.putAll(bestStationsFuelConsumption);

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, FuelConsumption> fuelConsumptionEntry : sortedBestStationsFuelConsumption.entrySet()) {
            stringBuilder
                    .append("<b>")
                    .append(fuelConsumptionEntry.getKey())
                    .append("</b>: ")
                    .append((fuelConsumptionEntry.getValue().getAmount() * 100)
                            / fuelConsumptionEntry.getValue().getKilometersAmount())
                    .append(unit)
                    .append(", ");
        }

        return stringBuilder.toString();
    }

    public boolean removeStat(Stat stat, Context context) throws SQLException {
        initHelper(context);
        return helper.getStatDao().delete(stat) == 1;
    }

    public FuelStatistics getFuelStatistics(Context context) throws SQLException {
        float fuelAmount = 0;
        float kilometersAmount = 0;
        float price = 0;
        List<Stat> allStats = getAllStats(context);

        for (Stat stat : allStats) {
            fuelAmount += stat.getFuelAmount();
            kilometersAmount += stat.getKilometers();
            price += stat.getPrice();
        }
        return new FuelStatistics(fuelAmount, kilometersAmount, price);
    }

    public List<String> getStations(Context context) throws SQLException {
        initHelper(context);
        List<String> stationNames = Lists.newArrayList(Collections2.transform(helper.getStationDao().queryForAll(), new Function<Station, String>() {
            @Override
            public String apply(Station station) {
                return station.getStationName();
            }
        }));
        Collections.sort(stationNames);
        return stationNames;
    }

    private void initHelper(Context context) {
        if (helper == null || !helper.isOpen()) {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
    }
}