package pl.mslawin.fuelstats.view;

import android.support.v4.app.Fragment;

import pl.mslawin.fuelstats.R;

/**
 * Created by mslawin on 27.12.14.
 */
public enum Section {

    ADD_STAT(0, R.string.section_addStat, AddStat.class),
    VIEW_BEST_STATIONS(1, R.string.section_bestStations, BestStations.class),
    SHOW_STATS(2, R.string.section_show_stats, ShowStatFragment.class);

    private int sectionNumber;
    private int sectionTitle;
    private Class<? extends Fragment> sectionClass;

    private Section(int sectionNumber, int sectionTitle, Class<? extends Fragment> sectionClass) {
        this.sectionNumber = sectionNumber;
        this.sectionTitle = sectionTitle;
        this.sectionClass = sectionClass;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public int getSectionTitle() {
        return sectionTitle;
    }

    public Class<? extends Fragment> getSectionClass() {
        return sectionClass;
    }

    public static Section getForNumber(int sectionNumber) {
        for (Section section : values()) {
            if (section.sectionNumber == sectionNumber) {
                return section;
            }
        }
        return null;
    }
}
