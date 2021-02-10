package cz.vancura.castmediaplayer.view.recyclerView;

import android.view.View;
/*
RecyclerView onLick interface used for listener
 */

public interface ListItemClickListener {
    void onListItemClick(int position);
    void onLongClick(int position);
}
