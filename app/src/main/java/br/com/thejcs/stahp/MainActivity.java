package br.com.thejcs.stahp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.api.entity.PlayerEntity;
import br.com.thejcs.stahp.util.CroutonErrorListener;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends ActionBarActivity {

    private MatchListFragment listFragment;

    private boolean allMatches = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.action_list, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                switch (i) {
                    case 0:
                        showMatchList(false);
                        break;
                    case 1:
                        showMatchList(true);
                        break;
                }
                return false;
            }
        });

        // como gerar intents
        // https://github.com/ogrebgr/android_volley_examples/blob/master/src/com/github/volley_examples/Act_Main.java

        if (savedInstanceState == null) {
            retrievePlayerEntity();

            listFragment = new MatchListFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, listFragment)
                    .commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean newVisible = !allMatches;
//        menu.getItem(R.id.action_new).setVisible(newVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                createMatch();
                return true;
            case R.id.action_settings:
//                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Crouton.cancelAllCroutons();
    }

    private void retrievePlayerEntity() {
        setProgressBarIndeterminateVisibility(true);

        String playerId = StahpApplication.getInstance().getPlayerId();

        if(playerId == null) {
            promptRegisterPlayer();
        }
        else {
            validatePlayer(playerId);
        }
    }

    private void promptRegisterPlayer() {
        final String dialogTitle = getString(R.string.register_title);
        final String dialogText = getString(R.string.register_message);
        final String dialogConfirm = getString(R.string.confirm);
        final String defaultName = getString(R.string.guest_name);

        final EditText editName = new EditText(this);
        editName.setHint(defaultName);
        editName.selectAll();

        new AlertDialog.Builder(this)
                .setView(editName)
                .setTitle(dialogTitle)
                .setMessage(dialogText)
                .setPositiveButton(dialogConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playerName = editName.getText().toString().trim();
                        if(playerName.isEmpty()) {
                            playerName = defaultName;
                        }
                        registerPlayer(playerName);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void registerPlayer(String playerName) {
        Response.Listener<PlayerEntity> listener = createPlayerResponseListener();
        Response.ErrorListener errorListener = createPlayerErrorListener();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.createPlayer(playerName, listener, errorListener);
    }

    private void validatePlayer(String playerId) {
        Response.Listener<PlayerEntity> listener = createPlayerResponseListener();
        Response.ErrorListener errorListener = createPlayerErrorListener();

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.getPlayer(playerId, listener, errorListener);
    }

    private Response.Listener<PlayerEntity> createPlayerResponseListener() {
        return new Response.Listener<PlayerEntity>() {
            @Override
            public void onResponse(PlayerEntity playerEntity) {
                setProgressBarIndeterminateVisibility(false);

                StahpApplication.getInstance().setPlayerId(playerEntity.getId());
                welcomePlayer(playerEntity);

                showMatchList(false);
            }
        };
    }

    private Response.ErrorListener createPlayerErrorListener() {
        return new CroutonErrorListener(this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressBarIndeterminateVisibility(false);

                NetworkResponse response = error.networkResponse;
                if(response.statusCode == 404) {
                    invalidatePlayerId();
                }
                else {
                    super.onErrorResponse(error);
                }
            }
        };
    }

    private void invalidatePlayerId() {
        StahpApplication.getInstance().clearPlayerId();
        promptRegisterPlayer();
    }

    private void welcomePlayer(PlayerEntity player) {
        String text = String.format(getString(R.string.welcome_message), player.getName());
        Crouton.showText(this, text, Style.INFO);
    }

    private void showMatchList() {
        showMatchList(allMatches);
    }

    private void showMatchList(boolean allMatches) {
        this.allMatches = allMatches;

        invalidateOptionsMenu();

        if(allMatches) {

            listFragment.fetchAllMatches();
        }
        else {
            listFragment.fetchMatches();
        }
    }

    private void createMatch() {
        setProgressBarIndeterminateVisibility(true);

        Response.Listener<MatchEntity> listener = new Response.Listener<MatchEntity>() {
            @Override
            public void onResponse(MatchEntity matchEntity) {
                setProgressBarIndeterminateVisibility(false);
                showMatchList(false);
            }
        };

        Response.ErrorListener errorListener = new CroutonErrorListener(this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressBarIndeterminateVisibility(false);
                super.onErrorResponse(error);
            }
        };

        StahpAPI api = StahpApplication.getInstance().getAPI();
        api.createMatch(listener, errorListener);
    }
}
