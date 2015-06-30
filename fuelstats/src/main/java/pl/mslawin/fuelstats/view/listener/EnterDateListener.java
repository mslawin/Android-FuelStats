package pl.mslawin.fuelstats.view.listener;

import android.view.View;
import android.widget.EditText;

/**
* Created by mslawin on 02.01.15.
*/
public class EnterDateListener implements View.OnClickListener {

    private final EditText enterDate;

    public EnterDateListener(EditText enterDate) {
        this.enterDate = enterDate;
    }

    @Override
    public void onClick(View v) {
        enterDate.getText().clear();
    }
}
