package pl.mslawin.fuelstats.service;

import java.util.Comparator;
import java.util.Map;

import pl.mslawin.fuelstats.domain.statistic.FuelConsumption;

/**
 * Created by mslawin on 31.01.15.
 */
public class StatComparator {

    public static Comparator<String> fuelConsumptionComparator(final Map<String, FuelConsumption> baseMap) {
        return new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                FuelConsumption lhsFuelConsumption = baseMap.get(lhs);
                FuelConsumption rhsFuelConsumption = baseMap.get(rhs);
                float lhsVal = (lhsFuelConsumption.getAmount() * 100) / lhsFuelConsumption.getKilometersAmount();
                float rhsVal = (rhsFuelConsumption.getAmount() * 100) / rhsFuelConsumption.getKilometersAmount();

                if (lhsVal < rhsVal) {
                    return -1;
                }
                if (lhsVal > rhsVal) {
                    return 1;
                }
                return 0;
            }
        };
    }
}
