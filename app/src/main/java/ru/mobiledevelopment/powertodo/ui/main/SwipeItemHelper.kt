package ru.mobiledevelopment.powertodo.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeItemHelper : ItemTouchHelper.SimpleCallback {

   var deleteListener: DeleteListener

    private var canSwipe = true

    interface DeleteListener {
        fun onItemDelete(pos: Int)
    }



    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        canSwipe = false
        Thread(object : Runnable {
            override fun run() {
                synchronized(this) {
                    try {
                        Thread.sleep(500) //wait for some and allow user to swipe again
                        canSwipe = true
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }).start()
        deleteListener.onItemDelete(viewHolder.adapterPosition)
    }

    //Allow dragging
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    //Allow user to swipe again when timer run out.
    override fun isItemViewSwipeEnabled(): Boolean {
        return canSwipe
    }
    
   //----------------------------------------

    //configure left swipe params
    var leftBG: Int = Color.LTGRAY
    var leftLabel: String = ""
    var leftIcon: Drawable? = null

    //configure right swipe params
    var rightBG: Int = Color.LTGRAY;
    var rightLabel: String = ""
    var rightIcon: Drawable? = null



    var context: Context;


    companion object{
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
    }


    constructor(context: Context ,  deleteListener: DeleteListener) : super(0, swipeFlags) {
        this.context = context
        this.deleteListener = deleteListener
    }



    private lateinit var background: Drawable

    var initiated: Boolean = false
    //Setting Swipe Text
    val paint = Paint()

    fun initSwipeView(): Unit {
        paint.setColor(Color.WHITE)
        paint.setTextSize(48f)
        paint.setTextAlign(Paint.Align.CENTER)
        background = ColorDrawable();
        initiated = true;
    }
    
    
    
   override fun onChildDraw(
       c: Canvas,
       recyclerView: RecyclerView,
       viewHolder: RecyclerView.ViewHolder,
       dX: Float,
       dY: Float,
       actionState: Int,
       isCurrentlyActive: Boolean
   ) {
       Log.d("onChildDraw", "dx: " + dX)

       val itemView = viewHolder.itemView
       if (!initiated) {
           initSwipeView()
       }


       if (dX != 0.0f) {

           if (dX > 0) {
               //right swipe
               val intrinsicHeight = (rightIcon?.getIntrinsicWidth() ?: 0)
               val xMarkTop = itemView.top + ((itemView.bottom - itemView.top) - intrinsicHeight) / 2
               val xMarkBottom = xMarkTop + intrinsicHeight

               colorCanavas(c, rightBG, itemView.left , itemView.top, itemView.right, itemView.bottom)
               drawTextOnCanvas(c, rightLabel, (itemView.left + 200).toFloat(), (xMarkTop + 10).toFloat())
               drawIconOnCanvas(
                   c, rightIcon, itemView.left + (rightIcon?.getIntrinsicWidth() ?: 0) + 50,
                   xMarkTop + 20,
                   itemView.left + 2 * (rightIcon?.getIntrinsicWidth() ?: 0) + 50,
                   xMarkBottom + 20
               )

           } else {
               //left swipe
               val intrinsicHeight = (leftIcon?.getIntrinsicWidth() ?: 0)
               val xMarkTop = itemView.top + ((itemView.bottom - itemView.top) - intrinsicHeight) / 2
               val xMarkBottom = xMarkTop + intrinsicHeight

               colorCanavas(
                   c,
                   leftBG,
                   itemView.right + dX.toInt(),
                   itemView.top,
                   itemView.right,
                   itemView.bottom
               )
               drawTextOnCanvas(c, leftLabel, (itemView.right - 200).toFloat(), (xMarkTop + 10).toFloat())
               drawIconOnCanvas(
                   c, leftIcon, itemView.right - 2 * (leftIcon?.getIntrinsicWidth() ?: 0) - 70,
                   xMarkTop + 20,
                   itemView.right - (leftIcon?.getIntrinsicWidth() ?: 0) - 70,
                   xMarkBottom + 20
               )
           }
       }

       super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

   }


    fun colorCanavas(canvas: Canvas, canvasColor: Int, left: Int, top: Int, right: Int, bottom: Int): Unit {
        (background as ColorDrawable).color = canvasColor
        background.setBounds(left, top, right, bottom)
        background.draw(canvas)
    }

    fun drawTextOnCanvas(canvas: Canvas, label: String, x: Float, y: Float) {
        canvas.drawText(label, x, y, paint)
    }

    fun drawIconOnCanvas(
        canvas: Canvas, icon: Drawable?, left: Int, top: Int, right: Int, bottom: Int
    ): Unit {
        icon?.setBounds(left, top, right, bottom)
        icon?.draw(canvas)

    }

    
}