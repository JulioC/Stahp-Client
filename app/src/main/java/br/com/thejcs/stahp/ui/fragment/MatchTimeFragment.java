package br.com.thejcs.stahp.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.thejcs.stahp.R;

public class MatchTimeFragment extends Fragment {

    Integer timeLeft;
    Integer totalTime;

    TextView textTime;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_time, container, false);


        textTime = (TextView) rootView.findViewById(R.id.match_time);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
        progressBar.setMax(totalTime);
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;

        progressBar.setProgress(timeLeft);

        Resources res = getResources();
        textTime.setText(res.getQuantityString(R.plurals.time_left, timeLeft, timeLeft));
    }

}
