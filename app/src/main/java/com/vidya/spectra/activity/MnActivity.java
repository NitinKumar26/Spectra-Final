package com.vidya.spectra.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.vidya.spectra.adapter.MainAdapter;
import com.vidya.spectra.helper.SessionManager;
import com.vidya.spectra.helper.SQLiteHandler;
import com.vidya.spectra.R;
import com.vidya.spectra.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MnActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private SQLiteHandler db;
    private SessionManager session;

    private MainAdapter adapter;
    private List<Event> eventList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mn);
        //setting toolbar to WelcomeActivity
        android.support.v7.widget.Toolbar toolbar =  findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);                            //setting Activity Name
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();                    //getting ActionBar
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_button);     //locating Hamburger icon
        actionbar.setDisplayHomeAsUpEnabled(true);                      //setting Humburger Icon


        //following is the code related to navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String student_id = user.get("student_id");


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

        Button membershipButton = findViewById(R.id.membership_button);
        membershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent membershipIntent = new Intent(MnActivity.this, MembershipActivity.class);
                startActivity(membershipIntent);
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



    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
