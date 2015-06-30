package pl.mslawin.fuelstats.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.fuelstats.domain.Stat;
import pl.mslawin.fuelstats.domain.Station;

/**
 * Created by mslawin on 27.12.14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "fuelstat.db";
    private static final int DATABASE_VERSION = 1;
    private static final Logger logger = Logger.getLogger(DatabaseHelper.class.getName());

    private Dao<Stat, Integer> statDao;
    private Dao<Station, Integer> stationDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Station.class);
            TableUtils.createTable(connectionSource, Stat.class);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to create tables");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Stat, Integer> getStatDao() throws SQLException {
        if (statDao == null) {
            statDao = getDao(Stat.class);
        }

        return statDao;
    }

    public Dao<Station, Integer> getStationDao() throws SQLException {
        if (stationDao == null) {
            stationDao = getDao(Station.class);
        }

        return stationDao;
    }
}
