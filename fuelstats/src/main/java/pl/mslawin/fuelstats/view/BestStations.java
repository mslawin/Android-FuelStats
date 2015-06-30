package pl.mslawin.fuelstats.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.fuelstats.R;
import pl.mslawin.fuelstats.domain.statistic.FuelStatistics;
import pl.mslawin.fuelstats.service.StatService;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BestStations.BestStationsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BestStations#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BestStations extends Fragment {

    private static final Logger logger = Logger.getLogger(BestStations.class.getName());
    private BestStationsInteractionListener mListener;

    private final StatService statService;

    public static BestStations newInstance() {
        return new BestStations();
    }

    private TextView bestStationsFuelLabel;
    private TextView bestStationsPriceLabel;
    private TextView bestStationsFuel;
    private TextView bestStationsPrice;
    private TextView allStationsFuel;
    private TextView allStationsKilometers;
    private TextView allStationsPrice;

    public BestStations() {
        // Required empty public constructor
        statService = new StatService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_stations, container, false);
        this.bestStationsFuelLabel = (TextView) view.findViewById(R.id.bestStationsFuelLabel);
        this.bestStationsFuelLabel.setText(Html.fromHtml(getString(R.string.bestStations_bestFuel)));

        this.bestStationsPriceLabel = (TextView) view.findViewById(R.id.bestStationsPriceLabel);
        this.bestStationsPriceLabel.setText(Html.fromHtml(getString(R.string.bestStations_bestPrice)));

        this.bestStationsFuel = (TextView) view.findViewById(R.id.bestStationsFuel);
        this.bestStationsPrice = (TextView) view.findViewById(R.id.bestStationsPrice);

        this.allStationsFuel = (TextView) view.findViewById(R.id.allStationsFuel);
        this.allStationsKilometers = (TextView) view.findViewById(R.id.allStationsKilometers);
        this.allStationsPrice = (TextView) view.findViewById(R.id.allStationsPrice);

        try {
            this.bestStationsFuel.setText(Html.fromHtml(statService.getBestStatsFuel(view.getContext())));
            this.bestStationsPrice.setText(Html.fromHtml(statService.getBestStatsPrice(view.getContext())));

            FuelStatistics fuelStatistics = statService.getFuelStatistics(view.getContext());
            this.allStationsFuel.setText(Float.toString(fuelStatistics.getFuelAmount()) + "l");
            this.allStationsKilometers.setText(Float.toString(fuelStatistics.getKilometersAmount()) + "km");
            this.allStationsPrice.setText(Float.toString(fuelStatistics.getPrice()) +
                    Currency.getInstance(Locale.getDefault()).getCurrencyCode());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to get best stations");
            Toast.makeText(view.getContext(), getString(R.string.bestStations_getStationsError),
                    Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (BestStationsInteractionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BestStationsInteractionListener {
        // TODO: Update argument type and name
        public void bestStationsInteraction(Uri uri);
    }
}