package com.vidya.spectra.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.vidya.spectra.R;
import com.vidya.spectra.app.AppConfig;
import com.vidya.spectra.app.AppController;
import com.vidya.spectra.helper.SQLiteHandler;
import com.vidya.spectra.helper.SessionManager;
import com.vidya.spectra.helper.SSLException;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputStudent_id;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        inputStudent_id = findViewById(R.id.student_id_login);
        inputPassword = findViewById(R.id.password_login);
        Button btnLogin = findViewById(R.id.button_login);
        final LinearLayout noInternet = findViewById(R.id.no_internet_login);
        final RelativeLayout loginLayout = findViewById(R.id.login_layout);
        noInternet.setVisibility(View.GONE);

        if (!isNetworkAvailable()){
            noInternet.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }else {

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Session manager
            session = new SessionManager(getApplicationContext());

            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Intent intent = new Intent(LogInActivity.this, MnActivity.class);
                startActivity(intent);
                finish();
            }

            // Login button Click Event
            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String student_id = inputStudent_id.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!student_id.isEmpty() && !password.isEmpty()) {

                        if (isNetworkAvailable()) {
                            // login user
                            SSLException.disableSSLCertificateChecking();
                            checkLogin(student_id, password);
                        } else {

                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });

        }
    }

    /**
     * Checks if there is Internet accessible.
     * Based on a stackoverflow snippet
     *
     * @return True if there is Internet. False if not.
     */
    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
         activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String student_id, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String student_id = user.getString("student_id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String course = user.getString("course");
                        String contact = user.getString("contact");

                        // Inserting row in users table
                        db.addUser(student_id, name, email,course,contact, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LogInActivity.this,
                                MnActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("student_id", student_id);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}