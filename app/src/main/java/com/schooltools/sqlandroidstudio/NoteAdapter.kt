package com.schooltools.sqlandroidstudio

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.schooltools.sqlandroidstudio.databinding.NotesRecyclerBinding

class NoteAdapter(private val noteList:ArrayList<Note>):RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder(val binding:NotesRecyclerBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val binding=NotesRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resources = holder.itemView.context.resources
        holder.binding.title.text = noteList[position].title
        holder.binding.notes.text = noteList[position].notes
        holder.binding.notesBg.background.setTint(resources.getColor(noteList[position].color))
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,Details::class.java)
            intent.putExtra("noteId",noteList[position].id)
            intent.putExtra("info","old")
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener{
            alertDialog(holder)
            true
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
    private fun alertDialog(holder: ViewHolder){
        val drawable= ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_delete)
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButtonIcon(drawable)
            .setPositiveButton(null){_,_->
                deleteNote(holder)
            }
            .show()
    }
    private fun deleteNote(holder: ViewHolder){
        try {
            val db =holder.itemView.context.openOrCreateDatabase("Notes", AppCompatActivity.MODE_PRIVATE, null)
            db.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, detail VARCHAR)")
            db.execSQL("DELETE FROM Notes WHERE id = ?",arrayOf(noteList[holder.adapterPosition].id))
            db.close()
        }
        catch (e:Exception){
            Toast.makeText(holder.itemView.context,"Database not found", Toast.LENGTH_SHORT).show()
        }
        noteList.removeAt(holder.adapterPosition)
        notifyDataSetChanged()
    }

}