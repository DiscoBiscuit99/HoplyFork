package xyz.discobiscuit.hoplyfork.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import xyz.discobiscuit.hoplyfork.MapsActivity;
import xyz.discobiscuit.hoplyfork.R;
import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.MapLocationEntity;
import xyz.discobiscuit.hoplyfork.database.Post;
import xyz.discobiscuit.hoplyfork.database.Reaction;
import xyz.discobiscuit.hoplyfork.viewmodel.PostAdapter;
import xyz.discobiscuit.hoplyfork.viewmodel.PostViewModel;

public class PostsActivity extends AppCompatActivity {

    private PostViewModel postViewModel;

    private Button newPostBtn;

    private String currentUserId;
    private String currentUserNickname;

    private FusedLocationProviderClient locationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_posts );

        // Get the user id and nickname from the intent.
        currentUserId = getIntent().getStringExtra( "user-id" );
        currentUserNickname = getIntent().getStringExtra( "nickname" );

        // URLs for the remote database.
        String baseUrl = "https://caracal.imada.sdu.dk/app2022/";
        String reactionsUrl = baseUrl + "reactions";

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );

        // The request object for the reactions.
        JsonArrayRequest reactionsRequest = new JsonArrayRequest(
                Request.Method.GET,
                reactionsUrl,
                null,
                response -> {
                    Log.d("reactions-response", response.toString() );
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Type reactionCollectionType = new TypeToken<Collection<Reaction>>(){}.getType();
                    List<Reaction> reactions = gson.fromJson(response.toString(), reactionCollectionType );
                    for ( Reaction reaction : reactions )
                        HoplyRepository.getInstance(getApplicationContext()).insertReactions( reaction );
                },
                error -> Log.d("reactions-error", error.toString() )
        );

        requestQueue.add( reactionsRequest );

        initBtns();

        // Set up the recycler view.
        RecyclerView postsRecyclerView = findViewById( R.id.feed );
        postsRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        postsRecyclerView.setHasFixedSize( true );

        // Connect the adapter to the recycler view.
        PostAdapter postAdapter = new PostAdapter();
        postAdapter.context( getApplicationContext() );
        postsRecyclerView.setAdapter( postAdapter );

        // Set up the post view model provider.
        postViewModel = new ViewModelProvider( this ).get( PostViewModel.class );

        // Set observers for changes in the post view model.
        postViewModel.getAllPosts().observe( this, postAdapter::setPosts);
        postViewModel.getAllReactions().observe( this, postAdapter::setReactions);

        // Set location provider client.
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    // Initialize the buttons of the page.
    private void initBtns() {

        newPostBtn = findViewById( R.id.new_post_btn );

        // The new post button should insert both the post and a map location into the local database.
        newPostBtn.setOnClickListener(v -> {

            TextView postContentTextView = findViewById( R.id.post_content_text );

            String postContent =
                    postContentTextView
                            .getText()
                            .toString();

            postContentTextView.setText( "" );

            // Creates the post and gets user location.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                // If permission to use location data has been granted, get the position.
                if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    Post newPost = new Post(currentUserId, postContent, "time");

                    try {
                        int post_id = 0;
                        post_id = (int) HoplyRepository
                                .getInstance( getApplicationContext() )
                                .insertPostsReturnId(newPost);


                        int finalPost_id = post_id;

                        // Insert the location if possible.
                        locationProviderClient.getLastLocation()
                                .addOnSuccessListener(location -> {
                                    HoplyRepository.getInstance(getApplicationContext())
                                            .insertLocation(new MapLocationEntity(finalPost_id, location.getLatitude(),location.getLongitude()));
                                    Log.d("Location", location.getLatitude() + " " + location.getLongitude() );
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                // Otherwise, ask the user for permission to use location data.
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        });

    }
}
