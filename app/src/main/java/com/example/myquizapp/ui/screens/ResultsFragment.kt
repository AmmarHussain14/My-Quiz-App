package com.example.myquizapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myquizapp.R
import com.example.myquizapp.viewmodel.QuizViewModel

class ResultsFragment : Fragment() {
    
    private val viewModel: QuizViewModel by activityViewModels()
    
    private lateinit var scoreProgressBar: ProgressBar
    private lateinit var scoreText: TextView
    private lateinit var percentageText: TextView
    private lateinit var skippedText: TextView
    private lateinit var streakText: TextView
    private lateinit var performanceMessage: TextView
    private lateinit var restartButton: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        scoreProgressBar = view.findViewById(R.id.scoreProgressBar)
        scoreText = view.findViewById(R.id.scoreText)
        percentageText = view.findViewById(R.id.percentageText)
        skippedText = view.findViewById(R.id.skippedText)
        streakText = view.findViewById(R.id.streakText)
        performanceMessage = view.findViewById(R.id.performanceMessage)
        restartButton = view.findViewById(R.id.restartButton)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewModel.quizState.observe(viewLifecycleOwner) { state ->
            // Update score text
            scoreText.text = "${state.correctAnswers} / ${state.questions.size}"
            
            // Update progress bar
            val progress = if (state.questions.isNotEmpty()) {
                (state.correctAnswers.toFloat() / state.questions.size * 100).toInt()
            } else 0
            scoreProgressBar.progress = progress
            
            // Update percentage text
            percentageText.text = "$progress% Correct"
            
            // Update skipped and streak text
            skippedText.text = "${state.skippedQuestions} skipped"
            streakText.text = "${state.longestStreak} streak"
            
            // Update performance message based on score
            val percentage = if (state.questions.isNotEmpty()) {
                (state.correctAnswers.toFloat() / state.questions.size * 100).toInt()
            } else 0
            
            val message = when {
                percentage >= 90 -> getString(R.string.performance_outstanding)
                percentage >= 70 -> getString(R.string.performance_great)
                percentage >= 50 -> getString(R.string.performance_good)
                else -> getString(R.string.performance_nice)
            }
            
            performanceMessage.text = message
        }
    }
    
    private fun setupClickListeners() {
        restartButton.setOnClickListener {
            viewModel.restartQuiz()
            findNavController().navigate(R.id.action_resultsFragment_to_questionFragment)
        }
    }
}
