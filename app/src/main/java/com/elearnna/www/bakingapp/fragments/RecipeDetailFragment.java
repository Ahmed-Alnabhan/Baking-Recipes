package com.elearnna.www.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.activities.RecipeDetailActivity;
import com.elearnna.www.bakingapp.activities.RecipeListActivity;
import com.elearnna.www.bakingapp.data.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mItem;
    private ArrayList<Step> mSteps;
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private TextView stepTxt;
    private ImageView nextBtn;
    private ImageView prevBtn;
    private int position;
    private ImageView noVideo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getParcelable(ARG_ITEM_ID);
            position = mItem.getId();
        }

        if (getActivity().getIntent().hasExtra("stepsList")) {
            mSteps = getActivity().getIntent().getExtras().getParcelableArrayList("stepsList");
        } else {
            mSteps = getArguments().getParcelableArrayList("stepsList");
        }
        // Initialize the playerView
        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        noVideo = (ImageView) rootView.findViewById(R.id.no_video);
        nextBtn = (ImageView) rootView.findViewById(R.id.next_btn);
        prevBtn = (ImageView) rootView.findViewById(R.id.previous_btn);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            stepTxt = (TextView) rootView.findViewById(R.id.recipe_detail);
            stepTxt.setText(mItem.getDescription());
            initializePlayer(Uri.parse(mItem.getVideoURL()));
        }

        if (RecipeDetailFragment.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resizeVideo();
            switchVisibility(View.GONE);
        } else {
            simpleExoPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimen_0_dp);
            simpleExoPlayerView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.dimen_0_dp);
            switchVisibility(View.VISIBLE);
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mSteps.size() - 1) {
                    position++;
                    mItem = mSteps.get(position);
                    stepTxt.setText(mItem.getDescription());
                    releaseExoPlayer();
                    initializePlayer(Uri.parse(mItem.getVideoURL()));
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    mItem = mSteps.get(position);
                    stepTxt.setText(mItem.getDescription());
                    releaseExoPlayer();
                    initializePlayer(Uri.parse(mItem.getVideoURL()));
                }
            }
        });

        return rootView;
    }



    private void initializePlayer(Uri videoURL) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Baking Recipes");
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            // Pause the video when the activity starts
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseExoPlayer();
    }

    private void releaseExoPlayer() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int visibility;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            visibility = View.GONE;
            resizeVideo();
            switchVisibility(visibility);
        } else {
            visibility = View.VISIBLE;
            simpleExoPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimen_0_dp);
            simpleExoPlayerView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.dimen_0_dp);
            switchVisibility(visibility);
        }

    }

    private void switchVisibility(int visibility) {
        stepTxt.setVisibility(visibility);
        nextBtn.setVisibility(visibility);
        prevBtn.setVisibility(visibility);
        getActivity().findViewById(R.id.detail_toolbar).setVisibility(visibility);
    }

    private void resizeVideo() {
        simpleExoPlayerView.setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

}
