package br.com.thejcs.stahp.util;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import br.com.thejcs.stahp.R;
import br.com.thejcs.stahp.api.toolbox.VolleyErrorHelper;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CroutonErrorListener implements Response.ErrorListener {

    private final Activity activity;

    public CroutonErrorListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String message = String.format(
                activity.getString(R.string.error_message),
                VolleyErrorHelper.getMessage(error, activity));
        Crouton.showText(activity, message, Style.ALERT);

        logError(error);
    }

    protected void logError(VolleyError error) {
        // TODO: log or notify the error (?)
    }
}
