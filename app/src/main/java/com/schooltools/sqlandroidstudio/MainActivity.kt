package com.schooltools.sqlandroidstudio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.schooltools.sqlandroidstudio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private var dbNotes= ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val intent = Intent(this, Details::class.java)
            intent.putExtra("info","new")
            Toast.makeText(this,dbNotes.size.plus(1) .toString(),Toast.LENGTH_SHORT).show()
            intent.putExtra("noteId","${dbNotes.size+1}")
            startActivity(intent)
        }
        val staggeredGrid=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecycler.layoutManager= staggeredGrid
        staggeredGrid.gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        noteAdapter=NoteAdapter(dbNotes)
        noteAdapter.notifyItemInserted(dbNotes.size-1)
        binding.noteRecycler.adapter=noteAdapter


        binding.dateRecycler.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val dateAdapter=DateAdapter(Objects().datalist())
        binding.dateRecycler.adapter=dateAdapter


        binding.ctgRecycler.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val ctgAdapter=CtgAdapter(Objects().categorylist())
        binding.ctgRecycler.adapter=ctgAdapter

    }
    override fun onResume() {
        super.onResume()
        getDatabaseNotes()
        noteAdapter.notifyDataSetChanged()
    }
    private fun getDatabaseNotes() {
        try {
            dbNotes.clear()
            val bgColors=listOf(R.color.lightRed,R.color.lightGreen,R.color.lightBlue,R.color.lightYellow,R.color.lightPurple)
            val db =this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
            val cursor = db.rawQuery("SELECT * FROM Notes",null)
            val idIx = cursor.getColumnIndex("id")
            val titleIx = cursor.getColumnIndex("title")
            val noteIx = cursor.getColumnIndex("detail")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIx).toString()
                val title = cursor.getString(titleIx).toString()
                val note = cursor.getString(noteIx).toString()
                val bgColor=bgColors.random()
                dbNotes.add(Note(id,title,note,bgColor))
            }
            cursor.close()
        }
        catch (e:Exception){
            Toast.makeText(this,"Database tapılmadı",Toast.LENGTH_SHORT).show()
        }
    }
}