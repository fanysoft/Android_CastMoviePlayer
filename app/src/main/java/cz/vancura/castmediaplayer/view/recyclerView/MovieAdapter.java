package cz.vancura.castmediaplayer.view.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cz.vancura.castmediaplayer.model.MoviePOJO;
import cz.vancura.castmediaplayer.R;

/*
RecyclerView Adapter
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private static String TAG = "myTAG-MovieAdapter";

    private List<MoviePOJO> moviePOJOList;
    public static ListItemClickListener mOnClickListener;
    Context context;


    // 1 - contructor
    public MovieAdapter(List<MoviePOJO> dataClassList, ListItemClickListener onClickListener, Context context) {
        this.moviePOJOList = dataClassList;
        // TODO leak fix
        this.mOnClickListener = onClickListener;
        this.context = context;
    }


    // 2 - View Holder
    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView mTextViewName, mTextViewId, mTextViewViews;
        public ImageView mImageView;

        public MovieViewHolder(View view) {
            super(view);
            // add listeners
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            // GUI
            mTextViewName = view.findViewById(R.id.textViewName);
            mTextViewId = view.findViewById(R.id.textViewId);
            mTextViewViews = view.findViewById(R.id.textViewViews);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onCLick in MovieViewHolder");
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "onLongClick in MovieViewHolder");
            int position = getAdapterPosition();
            mOnClickListener.onLongClick(position);
            return false;
        }
    }


    // 3 - Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    // 4 - Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {

        MoviePOJO moviePOJO = moviePOJOList.get(position);

        String name = moviePOJO.getMovieName() + " " + moviePOJO.getMovieDuration();
        String id = "(" + String.valueOf(moviePOJO.getMovieId()) + ")";
        String views = "views " + String.valueOf(moviePOJO.getMovieViews());
        String iconUrl = moviePOJO.getMovieImageUrl();

        // GUI - textView
        holder.mTextViewName.setText(name);
        holder.mTextViewId.setText(id);
        holder.mTextViewViews.setText(views);

        // GUI - Image
        Glide.with(context)
                .load(iconUrl)
                .into(holder.mImageView);


    }


    // 5 - Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moviePOJOList.size();
    }



}
