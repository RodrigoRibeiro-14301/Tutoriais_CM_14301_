package com.notes.notesproxmlviews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    var addNoteBtn: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var menuBtn: ImageButton? = null
    var noteAdapter: NoteAdapter? = null
    var notesListenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        addNoteBtn = findViewById<FloatingActionButton?>(R.id.add_note_btn)
        recyclerView = findViewById<RecyclerView?>(R.id.recyler_view)
        menuBtn = findViewById<ImageButton?>(R.id.menu_btn)

        noteAdapter = NoteAdapter(mutableListOf())
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = noteAdapter

        addNoteBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity, NoteDetailsActivity::class.java
                )
            )
        })
        menuBtn!!.setOnClickListener(View.OnClickListener { v: View? -> showMenu() })
    }

    override fun onStart() {
        super.onStart()
        notesListenerRegistration = Utility.getCollectionReferenceForNotes()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e(TAG, "Failed to load notes.", error)
                    return@addSnapshotListener
                }
                if (snapshots == null) {
                    return@addSnapshotListener
                }
                noteAdapter?.updateNotes(snapshots.documents)
            }
    }

    override fun onStop() {
        super.onStop()
        notesListenerRegistration?.remove()
    }

    fun showMenu() {
        val popupMenu = android.widget.PopupMenu(this@MainActivity, menuBtn)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sign_out_menu -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
