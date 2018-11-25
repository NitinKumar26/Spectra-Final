package com.vidya.spectra.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vidya.spectra.R;
import com.vidya.spectra.spectraApp.AppConfig;
import com.vidya.spectra.spectraApp.AppController;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText studentID;
    private EditText password1;
    private EditText password2;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pDialog = new ProgressDialog(ResetPasswordActivity.this);

        studentID = findViewById(R.id.reset_student_id);
        password1 = findViewById(R.id.reset_password1);
        password2 = findViewById(R.id.reset_password2);
        Button resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String studentIDString = studentID.getText().toString().trim();
                final String password1String = password1.getText().toString().trim();
                final String password2String = password2.getText().toString().trim();
                if (!studentIDString.isEmpty() && !password1String.isEmpty() && !password2String.isEmpty()) {
                    if (password1String.equals(password2String)) {
                        resetPassword(studentIDString, password1String);
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ResetPasswordActivity.this, "Please enter the details.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void resetPassword(final String student_id, final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Password updated successfully!", Toast.LENGTH_LONG).show();
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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext( ),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
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
