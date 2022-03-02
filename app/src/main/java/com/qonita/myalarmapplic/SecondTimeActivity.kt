package com.qonita.myalarmapplicpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.qonita.myalarmapplic.AlarmReceiver
import com.qonita.myalarmapplic.R
import com.qonita.myalarmapplic.databinding.ActivityMainBinding
import com.qonita.myalarmapplic.fragment.DatePickerFragment
import com.qonita.myalarmapplic.fragment.TimePickerFragment
import com.qonita.myalarmapplic.room.Alarm
import com.qonita.myalarmapplic.room.AlarmDB
import kotlinx.android.synthetic.main.activity_second_time.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SecondTimeAlarmActivity : AppCompatActivity(), View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener{

    private var binding: ActivityMainBinding? = null

    private lateinit var alarmReceiver: AlarmReceiver

    val db by lazy{AlarmDB(this)}

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_second_time)

        btn_set_time_second_time.setOnClickListener(this)
        btn_set_date_second_time.setOnClickListener(this)

        btn_add_set_second_time.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_set_date_second_time -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_set_time_second_time -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_add_set_second_time -> {
                val onceDate = tv_once_date.text.toString()
                val onceTime = tv_once_time.text.toString()
                val onceMessage = et_note_one_time.text.toString()

                alarmReceiver.setOneTimeAlarm(
                    this, AlarmReceiver.TYPE_SECOND_TIME,
                    onceDate,
                    onceTime,
                    onceMessage
                )

                CoroutineScope(Dispatchers.IO) .launch {
                    db.alarmDao().AddAlarm(
                        Alarm(0, onceTime, onceDate, onceMessage, AlarmReceiver.TYPE_SECOND_TIME)
                    )

                    finish()
                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormatOneTime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        tv_once_date.text = dateFormatOneTime.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormatOneTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_ONCE_TAG -> tv_once_time.text = timeFormatOneTime.format(calendar.time)
            else ->{

            }
        }
    }
    override fun onDestroy(){
        super.onDestroy()
        binding = null
    }

}



