package mah.com.br.cash.Diversos;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Marcelo on 23/04/2015.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;

        // Add top margin only for the first or second item to avoid double space between items
        // Add top margin only for the first or second item to avoid double space between items
        //   if ((parent.getChildCount() > 0 && parent.getChildPosition(view) == 0)
        //           || (parent.getChildCount() > 1 && parent.getChildPosition(view) == 1))
        //       outRect.top = space;
    }
}
