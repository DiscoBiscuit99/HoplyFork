package xyz.discobiscuit.hoplyfork.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xyz.discobiscuit.hoplyfork.MapsActivity;
import xyz.discobiscuit.hoplyfork.R;
import xyz.discobiscuit.hoplyfork.activities.PostsActivity;
import xyz.discobiscuit.hoplyfork.activities.StartActivity;
import xyz.discobiscuit.hoplyfork.database.HoplyDB;
import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.Post;
import xyz.discobiscuit.hoplyfork.database.PostDao;
import xyz.discobiscuit.hoplyfork.database.Reaction;
import xyz.discobiscuit.hoplyfork.database.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private HoplyRepository repository;

    private List<Post> posts = new ArrayList<>();
    private List<Reaction> reactions = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public PostHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        repository = HoplyRepository.getInstance( context.getApplicationContext() );

        View itemView =
                LayoutInflater
                        .from( parent.getContext() )
                        .inflate( R.layout.post_item, parent, false );

        return new PostHolder( itemView );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder( @NonNull PostHolder holder, int position ) {

        Post currentPost = posts.get( position );

        Optional<User> userOfPost = repository.findUserById( currentPost.user_id );

        String nickname = "Unknown...";
        if ( userOfPost.isPresent() )
            nickname = userOfPost.get().name;

        int likeCount = 0;
        for ( Reaction reaction : reactions )
            if ( reaction.type == 1 && reaction.post_id == currentPost.id )
                likeCount++;

        int dislikeCount = 0;
        for ( Reaction reaction : reactions )
            if ( reaction.type == 2 && reaction.post_id == currentPost.id )
                dislikeCount++;

        holder.textViewNickname.setText( nickname );
        holder.textViewContent.setText( currentPost.content );
        holder.likeBtn.setText( "Like " + likeCount );
        holder.dislikeBtn.setText( "Dislike " + dislikeCount );

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface RecyclerViewClickListener{
        void onClick( View view, int position);
    }

    public void setPosts( List<Post> posts ) {

        this.posts = posts;
        notifyDataSetChanged();

    }

    public void setReactions( List<Reaction> reactions ) {

        this.reactions = reactions;
        notifyDataSetChanged();

    }

    public void context( Context context ) {
        this.context = context;
    }

    class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewNickname;
        private TextView textViewContent;
        private Button likeBtn;
        private Button dislikeBtn;
        private Button locationBtn;

        public PostHolder( @NonNull View itemView ) {

            super( itemView );

            textViewNickname = itemView.findViewById( R.id.nickname_text_view );
            textViewContent = itemView.findViewById( R.id.content_text_view );
            likeBtn = itemView.findViewById( R.id.like_btn );
            dislikeBtn = itemView.findViewById( R.id.dislike_btn );
            locationBtn = itemView.findViewById( R.id.location_btn );

            likeBtn.setOnClickListener( new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Post currentPost = posts.get( getAdapterPosition() );
                    repository.insertReactions( Reaction.newLike( currentPost.user_id, currentPost.id ) );
                }

            } );

            dislikeBtn.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Post currentPost = posts.get( getAdapterPosition() );
                    repository.insertReactions( Reaction.newDislike( currentPost.user_id, currentPost.id ) );
                }

            } );

            locationBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Post currentPost = posts.get( getAdapterPosition());
            intent.putExtra( "post-id", currentPost.id );

            intent.putExtra("lat", Math.random() * (40-10) + 10);
            intent.putExtra("long", Math.random() * (40-10) + 10);

            context.startActivity(intent);
        }
    }

}
