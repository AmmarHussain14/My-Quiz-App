package com.example.myquizapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myquizapp.R
import com.example.myquizapp.viewmodel.QuizViewModel

class SplashFragment : Fragment() {
    
    private val viewModel: QuizViewModel by activityViewModels()
    
    private lateinit var loadingContainer: LinearLayout
    private lateinit var errorContainer: LinearLayout
    private lateinit var retryButton: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        loadingContainer = view.findViewById(R.id.loadingContainer)
        errorContainer = view.findViewById(R.id.errorContainer)
        retryButton = view.findViewById(R.id.retryButton)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewModel.quizState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> {
                    loadingContainer.visibility = View.VISIBLE
                    errorContainer.visibility = View.GONE
                }
                state.error != null -> {
                    loadingContainer.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                }
                state.questions.isNotEmpty() -> {
                    // Questions loaded, navigate to question fragment
                    findNavController().navigate(R.id.action_splashFragment_to_questionFragment)
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        retryButton.setOnClickListener {
            viewModel.loadQuestions()
        }
    }
}
