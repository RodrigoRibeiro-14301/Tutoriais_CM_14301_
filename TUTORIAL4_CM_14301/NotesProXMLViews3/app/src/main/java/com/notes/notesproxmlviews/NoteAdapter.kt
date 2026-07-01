package com.notes.notesproxmlviews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(private val notes: MutableList<DocumentSnapshot>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.note_title_text_view)
        val contentTextView: TextView = view.findViewById(R.id.note_content_text_view)
        val dateTextView: TextView = view.findViewById(R.id.note_date_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val document = notes[position]
        val note = document.toObject(Note::class.java)

        holder.titleTextView.text = note?.title
        holder.contentTextView.text = note?.content
        holder.dateTextView.text = note?.timestamp?.let { Utility.timestampToString(it) } ?: ""

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NoteDetailsActivity::class.java)
            intent.putExtra("title", note?.title)
            intent.putExtra("content", note?.content)
            intent.putExtra("docId", document.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<DocumentSnapshot>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}

