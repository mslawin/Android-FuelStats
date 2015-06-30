package pl.mslawin.fuelstats.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.roboguice.shaded.goole.common.collect.Lists;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.fuelstats.R;
import pl.mslawin.fuelstats.domain.Stat;
import pl.mslawin.fuelstats.dummy.DummyContent;
import pl.mslawin.fuelstats.service.StatService;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link pl.mslawin.fuelstats.view.ShowStatFragment.ShowStatInteractionListener}
 * interface.
 */
public class ShowStatFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final Logger logger = Logger.getLogger(ShowStatFragment.class.getName());
    private final StatService statService = new StatService();

    private ShowStatInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;

    private List<Object> stats;

    public static ShowStatFragment newInstance() {
        return new ShowStatFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowStatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStats();
        mAdapter = new ArrayAdapter<Object>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, stats){

            public Object getItem(int position) {
                return Html.fromHtml(super.getItem(position).toString());
            }
        };
    }

    private void initStats() {
        try {
            stats = new ArrayList<Object>();
            List<Stat> statsList = statService.getAllStats(getActivity().getApplicationContext());
            Collections.sort(statsList, new Comparator<Stat>() {
                @Override
                public int compare(Stat lhs, Stat rhs) {
                    return lhs.getDate().compareTo(rhs.getDate());
                }
            });
            stats.addAll(statsList);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to get list of stats", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mListView.setEmptyView(view.findViewById(R.id.empty));

        if (mAdapter.getCount() == 0) {
            setEmptyText();
        }

        registerForContextMenu(mListView);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ShowStatInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement ShowStatInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.showContextMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        if (view.getId() == R.id.list) {
            menu.add(R.string.showStats_deleteStat);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Stat statToRemove = getStat(((TextView) info.targetView).getText().toString());
        try {
            if (statService.removeStat(statToRemove, getActivity().getApplicationContext())) {
                stats.remove(statToRemove);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to remove stat " + statToRemove, e);
        }
        return false;
    }

    private Stat getStat(String statText) {
        for (Object stat : stats) {
            if (Html.fromHtml(stat.toString()).toString().equals(statText)) {
                return (Stat) stat;
            }
        }
        return null;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText() {
        View emptyView = mListView.getEmptyView();
        ((TextView) emptyView).setText(getResources().getString(R.string.showStats_emptyText));
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
    public interface ShowStatInteractionListener {
        public void showStatInteraction(String id);
    }
}