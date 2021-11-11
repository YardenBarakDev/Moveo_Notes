package com.ybdev.moveonotes.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ybdev.moveonotes.R
import com.ybdev.moveonotes.databinding.NoteCellBinding
import com.ybdev.moveonotes.mvvm.model.Note
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    val differ = AsyncListDiffer(this, NoteListUpdate())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position], holder.itemView)


    override fun getItemCount(): Int { return differ.currentList.size }


    inner class ViewHolder(private val binding : NoteCellBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(note : Note, view: View){

            val format: Format = SimpleDateFormat("yyyy MM dd HH:mm:ss")
            binding.date.text = format.format(Date(note.timeStamp))
            binding.title.text = note.title
            binding.body.text = note.text

            view.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("note", note)
                }
                it.findNavController().navigate(R.id.action_global_noteFragment, bundle)
            }
        }
    }


    /**
     * updates the note list async and and removing duplicates
     */
    inner class NoteListUpdate : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.key == oldItem.key
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.key == oldItem.key
        }
    }

}