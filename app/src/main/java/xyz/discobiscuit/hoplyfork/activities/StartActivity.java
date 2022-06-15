package xyz.discobiscuit.hoplyfork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import xyz.discobiscuit.hoplyfork.MapsActivity;
import xyz.discobiscuit.hoplyfork.R;
import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.Post;
import xyz.discobiscuit.hoplyfork.database.Reaction;
import xyz.discobiscuit.hoplyfork.database.User;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start );

        HoplyRepository repository = HoplyRepository.getInstance( getApplicationContext() );

        String baseUrl = "https://caracal.imada.sdu.dk/app2022/";
        String usersUrl = baseUrl + "users";
        String postsUrl = baseUrl + "posts";
        String reactionsUrl = baseUrl + "reactions";

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );

        JsonArrayRequest usersRequest = new JsonArrayRequest(
                Request.Method.GET,
                usersUrl,
                null,
                response -> {
                    Log.d("users-response", response.toString() );
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Type userCollectionType = new TypeToken<Collection<User>>(){}.getType();
                    Collection<User> users = gson.fromJson(response.toString(), userCollectionType );
                    for ( User user : users )
                        repository.insertUser( user );
                },
                error -> Log.d( "users-error", error.toString() )
        );

        JsonArrayRequest postsRequest = new JsonArrayRequest(
                Request.Method.GET,
                postsUrl,
                null,
                response -> {
                    Log.d("posts-response", response.toString() );
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Type postCollectionType = new TypeToken<Collection<Post>>(){}.getType();
                    List<Post> posts = gson.fromJson(response.toString(), postCollectionType );
                    for ( Post post : posts )
                        repository.insertPosts( post );
                },
                error -> Log.d("posts-error", error.toString() )
        );

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
                        if ( reaction.userId != null )
                            repository.insertReactions( reaction );
                },
                error -> Log.d("reactions-error", error.toString() )
        );

        requestQueue.add( usersRequest );
        requestQueue.add( postsRequest );
        requestQueue.add( reactionsRequest );

        initBtns();

    }

    private void initBtns() {

        Button createUserBtn = findViewById( R.id.create_user_btn );
        Button loginBtn = findViewById( R.id.login_btn );

        createUserBtn.setOnClickListener(v -> toCreateUserPage());

        loginBtn.setOnClickListener(v -> toLoginPage());
    }

    private void toCreateUserPage() {

        Intent createUserIntent = new Intent( getApplicationContext(), CreateUserActivity.class );
        startActivity( createUserIntent );

    }

    private void toLoginPage() {

        Intent loginIntent = new Intent( getApplicationContext(), LoginActivity.class );
        startActivity( loginIntent );

    }

    public void toMap(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

}