package com.syfuzzaman.swipeable_video_player_android.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        var newDx = (dx / 2).toInt() // 1.2 is an arbitrary value
        if (newDx == 0 && dx != 0) { // To have no 0 values (Optionally)
            newDx = 1
        }

        return super.scrollHorizontallyBy(newDx, recycler, state)
    }

}