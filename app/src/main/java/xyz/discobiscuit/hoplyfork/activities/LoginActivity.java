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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        initBtns();

    }

    private void initBtns() {

        Button loginBtn = findViewById( R.id.login_btn_login );

        loginBtn.setOnClickListener( new View.OnClickListener() {

            @RequiresApi( api = Build.VERSION_CODES.N )
            @Override
            public void onClick( View v ) {
                String userId =
                    ( (TextView) findViewById( R.id.id_edit_login ) )
                        .getText()
                        .toString();

                login( userId );
            }

        } );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void login( String userId ) {

        Optional<User> currentUser = HoplyRepository
                .getInstance( getApplicationContext() )
                .findUserById( userId );

        if ( currentUser.isPresent() ) {

            String nickname = currentUser.get().name;

            Intent postsIntent = new Intent( getApplicationContext(), PostsActivity.class );
            postsIntent.putExtra( "user-id", userId );
            postsIntent.putExtra( "nickname", nickname );
            startActivity( postsIntent );

        } else {

            Intent createUserIntent = new Intent( getApplicationContext(), CreateUserActivity.class );
            startActivity( createUserIntent );

        }


    }

}
