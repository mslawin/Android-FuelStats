package pl.mslawin.fuelstats.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.fuelstats.R;
import pl.mslawin.fuelstats.service.StatService;
import pl.mslawin.fuelstats.view.listener.EnterDateListener;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddStat.AddStatInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddStat#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddStat extends Fragment {

    private static final Logger logger = Logger.getLogger(AddStat.class.getName());
    public static final DateTimeFormatter ENTER_DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/YYYY");

    private AddStatInteractionListener mListener;

    private AutoCompleteTextView enterStation;
    private EditText enterDate;
    private EditText enterPrice;
    private EditText enterFuelAmount;
    private EditText enterKilometers;

    private StatService statService = new StatService();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddStat.
     */
    public static AddStat newInstance() {
        return new AddStat();
    }

    public AddStat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_stat, container, false);
        setViews(view);
        return view;
    }

    private void setViews(View view) {
        Button saveStatButton = (Button) view.findViewById(R.id.saveStat);
        this.enterDate = (EditText) view.findViewById(R.id.enterDate);
        this.enterPrice = (EditText) view.findViewById(R.id.enterPrice);
        this.enterStation = (AutoCompleteTextView) view.findViewById(R.id.enterStation);
        this.enterFuelAmount = (EditText) view.findViewById(R.id.enterFuelAmount);
        this.enterKilometers = (EditText) view.findViewById(R.id.enterKilometers);

        try {
            ArrayAdapter<String> stations = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, statService.getStations(getActivity()));
            enterStation.setAdapter(stations);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to get list of stations", e);
            Toast.makeText(getActivity(), "Unable to get list of stations", Toast.LENGTH_SHORT).show();
        }

        enterDate.setOnClickListener(new EnterDateListener(enterDate));
        enterDate.setText(DateTime.now().toString(ENTER_DATE_FORMATTER));

        saveStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    statService.saveStat(enterStation.getText().toString(), DateTime.parse(enterDate.getText().toString(),
                                    ENTER_DATE_FORMATTER), Float.parseFloat(enterFuelAmount.getText().toString()),
                            Float.parseFloat(enterPrice.getText().toString()), Float.parseFloat(enterKilometers.getText().toString()),
                            v.getContext());

                    enterStation.getText().clear();
                    enterDate.setText(DateTime.now().toString(ENTER_DATE_FORMATTER));
                    enterFuelAmount.getText().clear();
                    enterPrice.getText().clear();
                    enterKilometers.getText().clear();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Unable to save new stat entry", e);
                    Toast.makeText(getActivity(), "Unable to save new stat entry",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddStatInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddStatInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddStatInteractionListener {
        // TODO: Update argument type and name
        public void addStatInteraction(Uri uri);
    }
}