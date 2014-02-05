package br.com.thejcs.stahp.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.thejcs.stahp.R;
import br.com.thejcs.stahp.api.entity.ChallengeEntity;

public class ChallengesFragment extends Fragment {

    private List<ChallengeEntity> challenges;

    private ArrayList<String> words;

    private Integer currentIndex;

    TextView textInitial;
    TextView textTopic;
    EditText editWord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge, container, false);

        textInitial = (TextView) rootView.findViewById(R.id.challenge_initial);
        textTopic = (TextView) rootView.findViewById(R.id.challenge_topic);

        editWord = (EditText) rootView.findViewById(R.id.challenge_word);
        editWord.setOnKeyListener(createEditKeyListener());

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(createButtonClickListener());

        return rootView;
    }

    public void setChallenges(List<ChallengeEntity> challenges) {
        this.challenges = challenges;
        words = new ArrayList<String>();
    }

    public void start() {
        showFirstChallenge();
    }

    public void stop() {

    }

    private View.OnClickListener createButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentWord();
            }
        };
    }

    private View.OnKeyListener createEditKeyListener() {
        return new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    saveCurrentWord();
                    return true;
                }
                return false;
            }
        };
    }

    private void saveCurrentWord() {
        String word = editWord.getText().toString().trim();
        words.add(currentIndex, word);
        showNextChallenge();
    }

    private void showNextChallenge() {
        showChallenge(currentIndex + 1);
    }

    private void showFirstChallenge() {
        showChallenge(0);
    }

    private void showChallenge(Integer i) {
        currentIndex = i;

        if(i >= challenges.size()) {
            // TODO: end game
            return;
        }

        ChallengeEntity challenge = challenges.get(i);

        textInitial.setText(String.format(
                getString(R.string.challenge_initial),
                challenge.getInitial()));
        textTopic.setText(challenge.getTopic());

        editWord.setText("");
        editWord.requestFocus();
    }

    public ArrayList<String> getWords() {
        return words;
    }
}
