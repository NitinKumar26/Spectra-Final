package com.vidya.spectra.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.vidya.spectra.helper.SSLException;
import com.vidya.spectra.helper.SessionManager;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputStudentID;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        inputFullName =  findViewById(R.id.input_name_register);
        inputEmail =  findViewById(R.id.input_email_register);
        inputPassword =  findViewById(R.id.input_password_register);
        Button btnRegister = findViewById(R.id.btnRegister);
        inputStudentID = findViewById(R.id.input_student_id_register);
        final Spinner inputCourse =  findViewById(R.id.input_course);
        final EditText inputContact = findViewById(R.id.input_contact);
        LinearLayout noInternetRegister= findViewById(R.id.no_internet_register);
        RelativeLayout registerLayout= findViewById(R.id.register_layout);

        if (!isNetworkAvailable()){
            registerLayout.setVisibility(View.GONE);
            noInternetRegister.setVisibility(View.VISIBLE);
        }else{
            noInternetRegister.setVisibility(View.GONE);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
            SessionManager session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MnActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String studentID = inputStudentID.getText().toString().trim();
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String course = inputCourse.getSelectedItem().toString().trim();
                String contact = inputContact.getText().toString().trim();

                if (!studentID.isEmpty() && !name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    if (isNetworkAvailable()) {
                        SSLException.disableSSLCertificateChecking();
                        registerUser(studentID, name, email,course,contact, password);

                    }else{
                        Toast.makeText(RegisterActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
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
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String studentID, final String name, final String email,
                              final String password, final String course, final String contact) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String studentID = user.getString("student_id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String course = user.getString("course");
                        String contact = user.getString("contact");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(studentID, name, email, course,contact, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LogInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentID);
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("course",course);
                params.put("contact",contact);

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
