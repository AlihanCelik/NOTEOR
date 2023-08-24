package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.example.notesapp.Adapter.CalendarNoteAdapter
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() ,CalendarAdapter.onItemClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView
    private lateinit var recyclerViewNote: RecyclerView

    var arrNotes = ArrayList<Notes>()
    var notesAdapter: CalendarNoteAdapter= CalendarNoteAdapter()

    val s = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
    var date_time=""

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDateMonth = view.findViewById(R.id.text_date_month)
        recyclerView = view.findViewById(R.id.recyclerView)
        ivCalendarNext = view.findViewById(R.id.iv_calendar_next)
        ivCalendarPrevious = view.findViewById(R.id.iv_calendar_previous)

        recyclerViewNote=view.findViewById(R.id.recycler_view)
        recyclerViewNote.setHasFixedSize(true)
        recyclerViewNote.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)

        setUpAdapter()
        setUpClickListener()
        setUpCalendar()
        view.findViewById<TextView>(R.id.no_tasks_text).visibility = View.GONE

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> {
                        recyclerView.parent?.requestDisallowInterceptTouchEvent(true)
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })


    }



    private fun setUpClickListener() {
        ivCalendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
        }
        ivCalendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar()
        }
    }



    private fun setUpAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapter.setData(calendarList2)
            adapter.setOnItemClickListener(this)
        }
        recyclerView.adapter = adapter
    }


    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        tvDateMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setOnItemClickListener(this)
        adapter.setData(calendarList)
    }

    override fun onItemClick(text: String, date: String, day: String) {

        view?.findViewById<TextView>(R.id.date)?.text=text

        GlobalScope.launch(Dispatchers.Main){
            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                var CArr = ArrayList<Notes>()
                for (arr in arrNotes){
                    date_time=arr.dateTime.toString().split(" ")[0]
                    if(text==date_time){

                        CArr.add(arr)
                    }

                }
                if (CArr.isEmpty()){
                    view?.findViewById<LinearLayout>(R.id.notasks_layout)?.visibility = View.VISIBLE
                    view?.findViewById<LinearLayout>(R.id.tasks_layout)?.visibility = View.GONE
                    view?.findViewById<TextView>(R.id.no_tasks_text)?.visibility = View.VISIBLE
                }else{
                    view?.findViewById<LinearLayout>(R.id.notasks_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.tasks_layout)?.visibility = View.VISIBLE
                }

                notesAdapter.setData(CArr)
                notesAdapter.notifyDataSetChanged()
                view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter =notesAdapter
            }

        }

    }

}


