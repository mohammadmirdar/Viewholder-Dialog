package com.example.viewinstance

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MainActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var linearLayout: LinearLayout
    var newItem: Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainRootView = FrameLayout(this)
        val rootView = FrameLayout(this)

        mainRootView.addView(
            rootView,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT)
        )


        recycler = RecyclerView(this)

        rootView.addView(
            recycler,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT)
        )

        val adapter = Adapter { adapter, postion ->
            recycler.viewTreeObserver.addOnGlobalLayoutListener {

            }

            val viewType = adapter.getItemViewType(postion)
            val viewByPosition = recycler.layoutManager?.findViewByPosition(postion)
            newItem = (viewByPosition as Item)

            val array = IntArray(2)
            recycler.getChildViewHolder(newItem!!).itemView.getLocationInWindow(array)

            newItem?.let {
                recycler.removeView(newItem)
                val itemDialogView = ItemDialogView(
                    this,
                    rootView,
                    mainRootView,
                    newItem!!,
                    newItem!!,
                    array[0],
                    array[1]
                )
                itemDialogView.show()
            }

        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)


        setContentView(mainRootView)

    }


    inner class Adapter(val onItemClick: (Adapter, Int) -> Unit) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val item = Item(parent.context)
            val customHolder = CustomHolder(item)
            item.setOnLongClickListener {
                onItemClick(this, customHolder.adapterPosition)
                true
            }
            return customHolder
        }

        override fun getItemCount(): Int {
            return 40
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            ((holder as CustomHolder).itemView as Item).textView.text =
                "This is a Sample text $position"
        }


    }

    inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class Item(context: Context) : FrameLayout(context) {
        var textView: TextView


        init {
            setBackgroundColor(Color.BLUE)
            setPadding(10, 10, 10, 10)
            textView = TextView(context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)


            addView(textView, LayoutParams(LayoutParams.MATCH_PARENT, 100))
        }
    }
}