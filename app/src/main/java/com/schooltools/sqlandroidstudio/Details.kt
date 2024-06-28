package com.schooltools.sqlandroidstudio

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.schooltools.sqlandroidstudio.databinding.ActivityDetailsBinding
import java.sql.Types.NULL

class Details : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val id = intent.getStringExtra("noteId")
        var info = intent.getStringExtra("info")

        binding.done.setOnClickListener {
            loadToDb(info,id)
            info = "old"
            listOf(binding.title,binding.notes).forEach {
                it.clearFocus()
            }

        }
        binding.back.setOnClickListener {
            finish()
        }
        listOf(binding.title,binding.notes).forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    binding.done.visibility = View.VISIBLE
                    binding.back.visibility = View.INVISIBLE
                }
                else {
                    binding.done.visibility = View.INVISIBLE
                    binding.back.visibility = View.VISIBLE
                }
            }
        }

        if(info=="old"){
            try {
                val db =this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
                val cursor = db.rawQuery("SELECT * FROM Notes WHERE id =?",arrayOf(id))
                val titleIx = cursor.getColumnIndex("title")
                val noteIx = cursor.getColumnIndex("detail")
                Toast.makeText(this,"Loaded $id", Toast.LENGTH_SHORT).show()
                while (cursor.moveToNext()) {
                    val title = cursor.getString(titleIx).toString()
                    val note = cursor.getString(noteIx).toString()
                    binding.title.setText(title)
                    binding.notes.setText(note)
                }
                cursor.close()
            }
            catch (e:Exception){
                Toast.makeText(this,"Database not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadToDb(info:String?, id:String?){
        try {
            val db =this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
            db.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, detail VARCHAR)")
            val titleText = binding.title.text.toString()
            val noteText = binding.notes.text.toString()

            if(titleText.isEmpty() && noteText.isEmpty()){ return }

            if(info=="old"){
                db.execSQL("UPDATE Notes SET title =?, detail =? WHERE id =?", arrayOf(titleText, noteText,id))
                Toast.makeText(this,"Updated $id", Toast.LENGTH_SHORT).show()
            }
            else {
                db.execSQL("INSERT INTO Notes (title, detail) VALUES (?,?)", arrayOf(titleText, noteText))
                Toast.makeText(this, "Added $id", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception){
            Toast.makeText(this,"Database not found", Toast.LENGTH_SHORT).show()
        }
    }
}