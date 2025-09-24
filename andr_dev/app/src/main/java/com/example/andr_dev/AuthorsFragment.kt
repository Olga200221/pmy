package com.example.andr_dev

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment

class AuthorsFragment : Fragment(R.layout.fragment_authors) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView = view.findViewById<ListView>(R.id.listViewAuthors)

        val authors = listOf(
            Author("Ольга Шелегина", R.drawable.olga),
            Author("Эдуард Баженов", R.drawable.edic)
        )

        val adapter = AuthorAdapter(requireContext(), authors)
        listView.adapter = adapter
    }
}
