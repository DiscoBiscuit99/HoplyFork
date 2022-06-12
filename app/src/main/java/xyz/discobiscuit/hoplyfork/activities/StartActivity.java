package xyz.discobiscuit.hoplyfork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.discobiscuit.hoplyfork.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start );

        initBtns();

    }

    private void initBtns() {

        Button createUserBtn = findViewById( R.id.create_user_btn );
        Button loginBtn = findViewById( R.id.login_btn );

        createUserBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                toCreateUserPage();
            }

        } );

        loginBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                toLoginPage();
            }

        } );

    }

    private void toCreateUserPage() {

        Intent createUserIntent = new Intent( getApplicationContext(), CreateUserActivity.class );
        startActivity( createUserIntent );

    }

    private void toLoginPage() {

        Intent loginIntent = new Intent( getApplicationContext(), LoginActivity.class );
        startActivity( loginIntent );

    }

}