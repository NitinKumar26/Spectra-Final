package com.vidya.spectra.activity;

import android.app.ProgressDialog;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.content.Intent;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vidya.spectra.adapter.MainAdapter;
import com.vidya.spectra.spectraApp.AppConfig;
import com.vidya.spectra.spectraApp.AppController;
import com.vidya.spectra.helper.SessionManager;
import com.vidya.spectra.helper.SQLiteHandler;
import com.vidya.spectra.R;
import com.vidya.spectra.model.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MnActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private SQLiteHandler db;
    private SessionManager session;
    private MainAdapter adapter;
    private List<Event> eventList;
    private Button membershipButton;
    private HashMap<String, String> user;
    private ProgressDialog pDialog;





    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                int begginingIndex = response.indexOf("orderId=") + "orderId=".length();
                int endIndex = response.indexOf(":txnId");
                String value = response.substring(begginingIndex, endIndex);

                storePaymentDetails(value, user.get("user_id"));

                membershipButton.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mn);
        // Call the function callInstamojo to start payment here
        //setting toolbar to WelcomeActivity
        android.support.v7.widget.Toolbar toolbar =  findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);                            //setting Activity Name
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();                    //getting ActionBar
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_button);     //locating Hamburger icon
        actionbar.setDisplayHomeAsUpEnabled(true);                      //setting Humburger Icon

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //following is the code related to navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        // sqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        membershipButton = findViewById(R.id.membership_button);
        if (!session.isLoggedIn()) {
            logoutUser();
        }


        // Fetching user details from sqlite
         user = db.getUserDetails();

        String name = user.get("name");
        String student_id = user.get("student_id");
        String payment_id = user.get("payment_id");

        if (!payment_id.isEmpty()){
            membershipButton.setVisibility(View.GONE);
        }else{
            membershipButton.setVisibility(View.VISIBLE);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        TextView nameView= headerLayout.findViewById(R.id.nameView);
        nameView.setText(name);
        TextView studentIdView= headerLayout.findViewById(R.id.student_id_nav_header);
        studentIdView.setText(student_id);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        eventList = new ArrayList<>();
        adapter = new MainAdapter(MnActivity.this,eventList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareAlbums();

        membershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = user.get("name");
                String email = user.get("email");
                String contact = user.get("contact");
                String feeAmount = "200";
                String feePurpose = "Membership Fee";
                callInstamojoPay(email, contact, feeAmount, feePurpose, name);

            }
        });




    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // set item as selected to persist highlight
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here
                switch (menuItem.getItemId()){

                    case R.id.nav_logout:
                        logoutUser();
                        finish();
                        break;
                    case R.id.nav_gallery:
                        Intent intentGallery= new Intent(MnActivity.this,GalleryActivity.class);
                        startActivity(intentGallery);
                        break;
                    case R.id.nav_about:
                        Intent intentAbout = new Intent(MnActivity.this,AboutActivity.class);
                        startActivity(intentAbout);
                        break;
                    case R.id.nav_events:
                        Intent intentEvents = new Intent (MnActivity.this,EventActivity.class);
                        startActivity(intentEvents);
                        break;
                }
                    return true;
                }
            });

    }



    private void logoutUser() {

        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MnActivity.this, WelActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        int[] covers = new int[]{
                R.drawable.declamation,
                R.drawable.its_a_boy_girl_thing,
                R.drawable.law_suit,
                R.drawable.radio_show};
        Event a = new Event("Declamation",  covers[0]);
        eventList.add(a);

        a = new Event("It's a Boy Girl Thing",  covers[1]);
        eventList.add(a);

        a = new Event("Lawsuit",  covers[2]);
        eventList.add(a);

        a = new Event("Radio Show",  covers[3]);
        eventList.add(a);

        adapter.notifyDataSetChanged();
    }



    private void storePaymentDetails(final String payment_id, final String user_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Updating membership details on our end");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MEMBERSHIP_DETAILS_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response);
                hideDialog();

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                membershipButton.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("payment_id", payment_id);
                params.put("user_id", user_id);
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
