package xyz.discobiscuit.hoplyfork.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

import xyz.discobiscuit.hoplyfork.R;
import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.User;

public class CreateUserActivity extends AppCompatActivity {

    private Button createUserBtn;
    private Button loginBtn;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_user );

        initBtns();

    }

    // Initialize the buttons on the page.
    private void initBtns() {

        createUserBtn = findViewById( R.id.create_user_btn_create_user );
        loginBtn = findViewById( R.id.login_btn_create_user_page );

        createUserBtn.setOnClickListener( new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick( View v ) {
                createUser();
            }

        } );

        loginBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                toLoginPage();
            }

        } );

    }

    // Create a user if not already present in the local database and go to the login page.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createUser() {

        // Procedure:
        // * create a new user.
        // * insert it (if it's not already in the database).

        String userId = ( (TextView) findViewById( R.id.id_edit_create_user ) )
                .getText()
                .toString();

        String name = ( (TextView) findViewById( R.id.name_edit_create_user ) )
                .getText()
                .toString();

        User newUser = new User( userId, name, "time" );

        Optional<User> existingUser =
                    HoplyRepository
                        .getInstance( getApplicationContext() )
                        .findUserById( newUser.id );

        // Insert the given user if not already present in the local database.
        if ( !existingUser.isPresent() )
            HoplyRepository
                .getInstance( getApplicationContext() )
                .insertUser( newUser );

        // Then go to login page.
        toLoginPage();

    }

    // Go to the login page.
    private void toLoginPage() {

        Intent loginIntent = new Intent( getApplicationContext(), LoginActivity.class );
        startActivity( loginIntent );

    }

}
