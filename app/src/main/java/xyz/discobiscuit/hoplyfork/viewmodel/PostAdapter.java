package xyz.discobiscuit.hoplyfork.viewmodel;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xyz.discobiscuit.hoplyfork.R;
import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.Post;
import xyz.discobiscuit.hoplyfork.database.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Post> posts = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public PostHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

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

        Optional<User> userOfPost =
                HoplyRepository
                        .getInstance( context )
                        .findUserById( currentPost.userId );

        String nickname = "Unknown...";
        if ( userOfPost.isPresent() )
            nickname = userOfPost.get().name;

        holder.textViewNickname.setText( nickname );
        holder.textViewContent.setText( currentPost.content );

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts( List<Post> posts ) {

        this.posts = posts;
        notifyDataSetChanged();

    }

    public void context( Context context ) {
        this.context = context;
    }

    class PostHolder extends RecyclerView.ViewHolder {

        private TextView textViewNickname;
        private TextView textViewContent;

        public PostHolder( @NonNull View itemView ) {

            super( itemView );

            textViewNickname = itemView.findViewById( R.id.nickname_text_view );
            textViewContent = itemView.findViewById( R.id.content_text_view );

        }

    }

}
