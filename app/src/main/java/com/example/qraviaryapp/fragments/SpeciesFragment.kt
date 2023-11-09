package com.example.qraviaryapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpeciesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpeciesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_species, container, false)

        val genes = resources.getStringArray(R.array.Genes)
        val sortedGenes = genes.sortedArray()

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        //val adapter = GenesAdapter(getSortedGenesWithHeaders(sortedGenes))
        //recyclerView.adapter = adapter

        return rootView
    }

    private fun getSortedGenesWithHeaders(sortedGenes: Array<String>): List<Pair<String, String>> {
        val genesWithHeaders = mutableListOf<Pair<String, String>>()
        var currentHeader = ""
        for (gene in sortedGenes) {
            val header = gene[0].toUpperCase().toString()
            if (header != currentHeader) {
                genesWithHeaders.add(Pair(header, "")) // Add a header with an empty gene to show only the header
                currentHeader = header
            }
            genesWithHeaders.add(Pair("", gene)) // Add an empty header with the gene
        }
        return genesWithHeaders
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpeciesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SpeciesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}