package com.example.andr_dev

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.andr_dev.data.ScoreDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint // Enable Hilt injection for this Fragment
class RecordsFragment : Fragment(R.layout.fragment_records) {

    // Inject ScoreDao using Hilt
    @Inject
    lateinit var scoreDao: ScoreDao

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerScores)
        adapter = RecordsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            scoreDao.getTopScoresWithPlayer().collectLatest { scores ->
                adapter.submitList(scores)
            }
        }
    }
}