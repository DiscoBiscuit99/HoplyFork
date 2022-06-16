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

    // The repository reference.
    private HoplyRepository repository;

    // The list of posts and reactions.
    // These will be updated from the PostViewModel.
    private List<Post> posts = new ArrayList<>();
    private List<Reaction> reactions = new ArrayList<>();

    private Context context;

    @NonNull
    @Override
    public PostHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        // Sets the repository reference.
        repository = HoplyRepository.getInstance( context.getApplicationContext() );

        // Get the item view from the post_item layout.
        View itemView =
                LayoutInflater
                        .from( parent.getContext() )
                        .inflate( R.layout.post_item, parent, false );

        return new PostHolder( itemView );

    }

    // For each view (in this case, post) in the view holder, set the relevant information.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder( @NonNull PostHolder holder, int position ) {

        // Get the current post.
        Post currentPost = posts.get( position );

        // Get the user of the post.
        Optional<User> userOfPost = repository.findUserById( currentPost.user_id );

        // If the user can be found in the database, get the
        // nickname ('name' in the relation) from the database.
        String nickname = "Unknown...";
        if ( userOfPost.isPresent() )
            nickname = userOfPost.get().name;

        // Calculate the like count.
        int likeCount = 0;
        for ( Reaction reaction : reactions )
            if ( reaction.type == 1 && reaction.post_id == currentPost.id )
                likeCount++;

        // Calculate the dislike count.
        int dislikeCount = 0;
        for ( Reaction reaction : reactions )
            if ( reaction.type == 2 && reaction.post_id == currentPost.id )
                dislikeCount++;

        // Set the required information.
        holder.textViewNickname.setText( nickname );
        holder.textViewContent.setText( currentPost.content );
        holder.likeBtn.setText( "Like " + likeCount );
        holder.dislikeBtn.setText( "Dislike " + dislikeCount );

    }

    // Returns the size of the posts list.
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Sets this list post to the given list of posts and notifies the listener.
    public void setPosts( List<Post> posts ) {

        this.posts = posts;
        notifyDataSetChanged();

    }

    // Sets this list reactions to the given list of reactions and notifies the listener.
    public void setReactions( List<Reaction> reactions ) {

        this.reactions = reactions;
        notifyDataSetChanged();

    }

    // Set the current context to the given context.
    public void context( Context context ) {
        this.context = context;
    }

    // A view holder class for the posts.
    class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewNickname;
        private TextView textViewContent;
        private Button likeBtn;
        private Button dislikeBtn;
        private Button locationBtn;

        // Returns a new post holder.
        public PostHolder( @NonNull View itemView ) {

            super( itemView );

            // Sets the relevant views.
            textViewNickname = itemView.findViewById( R.id.nickname_text_view );
            textViewContent = itemView.findViewById( R.id.content_text_view );
            likeBtn = itemView.findViewById( R.id.like_btn );
            dislikeBtn = itemView.findViewById( R.id.dislike_btn );
            locationBtn = itemView.findViewById( R.id.location_btn );

            // Insert like reaction when like button is pressed.
            likeBtn.setOnClickListener(v -> {
                Post currentPost = posts.get( getAdapterPosition() );
                repository.insertReactions( Reaction.newLike( currentPost.user_id, currentPost.id ) );
            });

            // Insert dislike reaction when dislike button is pressed.
            dislikeBtn.setOnClickListener(v -> {
                Post currentPost = posts.get( getAdapterPosition() );
                repository.insertReactions( Reaction.newDislike( currentPost.user_id, currentPost.id ) );
            });

            // Open the map view when location button is pressed.
            locationBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // Create a new intent and set relevant flags.
            Intent intent = new Intent(context, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Get the current post.
            Post currentPost = posts.get( getAdapterPosition());

            // Send the post id and location data with the new intent.
            intent.putExtra( "post-id", currentPost.id );
            intent.putExtra("lat", Math.random() * (40-10) + 10);
            intent.putExtra("long", Math.random() * (40-10) + 10);

            // Switch intent.
            context.startActivity(intent);
        }
    }

}
