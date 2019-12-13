package com.maruchin.medihelper.presentation.framework

import android.content.Context
import android.graphics.PointF
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


class ScrollZoomLayoutManager(context: Context, itemSpace: Int) : RecyclerView.LayoutManager() {
    private val context: Context
    // Size of each items
    private var mDecoratedChildWidth = 0
    private var mDecoratedChildHeight = 0
    //Property
    private var startLeft = 0
    private var startTop = 0
    private var offsetScroll // The offset distance for each items which will change according to the scroll offset
            : Int
    /**
     * Default is top in parent
     *
     * @return the content offset of y
     */
    /**
     * Default is top in parent
     *
     * @param contentOffsetY the content offset of y
     */
    //initial top position of content
    var contentOffsetY = -1
    private var itemSpace = 0 //the space between each items
    private var offsetDistance = 0
    /**
     *
     * @return the max scale rate.. default is 1.2f
     */
    /**
     *
     * @param maxScale the max scale rate.. default is 1.2f
     */
    var maxScale //max scale rate defalut is 1.2f
            : Float
    //Sparse array for recording the attachment and x position of each items
    private val itemAttached = SparseBooleanArray()
    private val itemsX = SparseArray<Int>()
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            offsetScroll = 0
            return
        }
        //calculate the size of child
        if (childCount == 0) {
            val scrap: View = recycler.getViewForPosition(0)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0)
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap)
            mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap)
            offsetDistance = (mDecoratedChildWidth * ((maxScale - 1f) / 2f + 1) + itemSpace).toInt()
            startLeft = (horizontalSpace - mDecoratedChildWidth) / 2
            startTop = if (contentOffsetY == -1) (verticalSpace - mDecoratedChildHeight) / 2 else contentOffsetY
            detachAndScrapView(scrap, recycler)
        }
        //record the state of each items
        var x = 0
        for (i in 0 until itemCount) {
            itemsX.put(i, x)
            itemAttached.put(i, false)
            x += offsetDistance
        }
        detachAndScrapAttachedViews(recycler)
        fixScrollOffset()
        layoutItems(recycler, state)
    }

    private fun layoutItems(
        recycler: Recycler,
        state: RecyclerView.State
    ) {
        if (state.isPreLayout) return
        //remove the views which out of range
        for (i in 0 until childCount) {
            val view: View? = getChildAt(i)
            val position = getPosition(view!!)
            if (itemsX[position] - offsetScroll + startLeft > horizontalSpace
                || itemsX[position] - offsetScroll + startLeft < -mDecoratedChildWidth - paddingLeft
            ) {
                itemAttached.put(position, false)
                removeAndRecycleView(view, recycler)
            }
        }
        //add the views which do not attached and in the range
        for (i in 0 until itemCount) {
            if (itemsX[i] - offsetScroll + startLeft <= horizontalSpace
                && itemsX[i] - offsetScroll + startLeft >= -mDecoratedChildWidth - paddingLeft
            ) {
                if (!itemAttached[i]) {
                    val scrap: View = recycler.getViewForPosition(i)
                    measureChildWithMargins(scrap, 0, 0)
                    addView(scrap)
                    val x = itemsX[i] - offsetScroll
                    val scale = calculateScale(startLeft + x)
                    scrap.setRotation(0f)
                    layoutDecorated(
                        scrap, startLeft + x, startTop,
                        startLeft + x + mDecoratedChildWidth, startTop + mDecoratedChildHeight
                    )
                    itemAttached.put(i, true)
                    scrap.setScaleX(scale)
                    scrap.setScaleY(scale)
                }
            }
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        var willScroll = dx
        val targetX = offsetScroll + dx
        //handle the boundary
        if (targetX < 0) {
            willScroll = -offsetScroll
        } else if (targetX > maxOffsetX) {
            willScroll = maxOffsetX - offsetScroll
        }
        offsetScroll += willScroll //increase the offset x so when re-layout it can recycle the right views
        //handle position and scale
        for (i in 0 until childCount) {
            val v: View? = getChildAt(i)
            val scale = calculateScale(v!!.getLeft())
            layoutDecorated(v, v.getLeft() - willScroll, v.getTop(), v.getRight() - willScroll, v.getBottom())
            v.setScaleX(scale)
            v.setScaleY(scale)
        }
        //handle recycle
        layoutItems(recycler, state)
        return willScroll
    }

    /**
     *
     * @param x start positon of the view you want scale
     * @return the scale rate of current scroll offset
     */
    private fun calculateScale(x: Int): Float {
        val deltaX = Math.abs(x - (horizontalSpace - mDecoratedChildWidth) / 2)
        var diff = 0f
        if (mDecoratedChildWidth - deltaX > 0) diff = mDecoratedChildWidth - deltaX.toFloat()
        return (maxScale - 1f) / mDecoratedChildWidth * diff + 1
    }

    private val horizontalSpace: Int
        private get() = width - paddingRight - paddingLeft

    private val verticalSpace: Int
        private get() = height - paddingBottom - paddingTop

    /**
     * fix the offset x in case item out of boundary
     */
    private fun fixScrollOffset() {
        if (offsetScroll < 0) {
            offsetScroll = 0
        }
        if (offsetScroll > maxOffsetX) {
            offsetScroll = maxOffsetX
        }
    }

    /**
     * @return the max offset distance
     */
    private val maxOffsetX: Int
        private get() = (itemCount - 1) * offsetDistance

    private fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0)!!)
        val direction = if (targetPosition < firstChildPos) -1 else 1
        return PointF(direction.toFloat(), 0f)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollToPosition(position: Int) {
        if (position < 0 || position > itemCount - 1) return
        val target = position * offsetDistance
        if (target == offsetScroll) return
        offsetScroll = target
        fixScrollOffset()
        requestLayout()
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@ScrollZoomLayoutManager.computeScrollVectorForPosition(targetPosition)
            }
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        removeAllViews()
        offsetScroll = 0
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * @return Get the current positon of views
     */
    val currentPosition: Int
        get() = Math.round(offsetScroll / offsetDistance.toFloat())

    /**
     * @return Get the dx should be scrolled to the center
     */
    val offsetCenterView: Int
        get() = currentPosition * offsetDistance - offsetScroll

    companion object {
        private const val SCALE_RATE = 1.2f
    }

    init {
        this.context = context
        offsetScroll = 0
        this.itemSpace = itemSpace
        maxScale = SCALE_RATE
    }
}