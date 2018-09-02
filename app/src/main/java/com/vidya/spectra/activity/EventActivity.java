package com.vidya.spectra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vidya.spectra.R;

import com.bumptech.glide.Glide;
import com.vidya.spectra.adapter.EventAdapter;
import com.vidya.spectra.model.Event;
import com.vidya.spectra.utils.MyTouchListener;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_events);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_button_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.background_blue).into((ImageView) findViewById(R.id.backdrop_events));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new MyTouchListener(EventActivity.this, recyclerView, new MyTouchListener.OnTouchActionListener() {

            @Override
            public void onClick(View view, int position) {

                if (position == 0) {

                    Intent intentDeclamation= new Intent(EventActivity.this, DeclamationActivity.class);
                    startActivity(intentDeclamation);
                }else if (position==1){
                    Intent intentBoyGirl = new Intent(EventActivity.this, BoyGirlActivity.class);
                    startActivity(intentBoyGirl);
                }else if (position==2){
                    Intent intentLawSuit = new Intent(EventActivity.this,LawSuitActivity.class);
                    startActivity(intentLawSuit);
                }else{
                    Intent intentRadioShow = new Intent(EventActivity.this,RadioShowActivity.class);
                    startActivity(intentRadioShow);
                }

            }
        }));
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_events);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_events);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.events));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
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
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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
