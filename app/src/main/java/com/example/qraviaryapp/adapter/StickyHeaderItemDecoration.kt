package com.example.qraviaryapp.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.FlightListAdapter
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.NurseryListAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.ExpensesAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.PurchasesAdapter
import com.example.qraviaryapp.adapter.DetailedAdapter.SoldAdapter


class StickyHeaderItemDecoration(private val adapter: HomeGenesAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationcategories(private val adapter: CategoryFragmentAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecoration1(private val adapter: GenesAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationbirdlist(private val adapter: BirdListAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationpairprev(private val adapter: PreviousPairAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationsold(private val adapter: SoldAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationpurchase(private val adapter: PurchasesAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationexpenses(private val adapter: ExpensesAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationnursery(private val adapter: NurseryListAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationflight(private val adapter: FlightListAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#D9DADA"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
class StickyHeaderItemDecorationexpensesfilter(private val adapter: ExpensesFilterAdapter) : RecyclerView.ItemDecoration() {

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val headerHeight = 50 // Adjust the height as needed
    private val headerTextSize = 36f // Adjust the text size as needed

    init {
        headerPaint.setColor(Color.parseColor("#FFFFFFFF"));
        textPaint.setColor(Color.parseColor("#876EE1"));
        textPaint.textSize = headerTextSize
        textPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemCount = state.itemCount

        var previousHeader: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (position != RecyclerView.NO_POSITION) {
                val currentHeader = adapter.getHeaderForPosition(position)

                if (currentHeader != previousHeader) {
                    drawHeader(c, view, currentHeader)
                    previousHeader = currentHeader
                }
            }
        }
    }

    private fun drawHeader(c: Canvas, view: View, headerText: String) {
        val left = view.left
        val top = view.top - headerHeight
        val right = view.right
        val bottom = view.top
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), headerPaint)

        // Calculate the position for centering the text vertically
        val textY = top + (headerHeight / 2) + (textPaint.textSize / 2)

        // Calculate the position for aligning the text to the left
        val textX = left.toFloat() + 16 // Adjust the value as needed for left padding

        c.drawText(headerText, textX, textY, textPaint)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val isFirstItem = position == 0 || adapter.getHeaderForPosition(position) != adapter.getHeaderForPosition(position - 1)

        if (isFirstItem) {
            outRect.set(0, headerHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}