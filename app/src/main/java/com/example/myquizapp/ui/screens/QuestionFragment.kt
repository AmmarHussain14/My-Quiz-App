package com.example.myquizapp.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.R
import com.example.myquizapp.ui.adapters.OptionItem
import com.example.myquizapp.ui.adapters.OptionsAdapter
import com.example.myquizapp.viewmodel.QuizViewModel
import kotlin.math.abs

class QuestionFragment : Fragment() {
    
    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var optionsAdapter: OptionsAdapter
    
    // View references
    private lateinit var progressBar: ProgressBar
    private lateinit var questionCounterText: TextView
    private lateinit var streakContainer: LinearLayout
    private lateinit var questionText: TextView
    private lateinit var optionsRecyclerView: RecyclerView
    private lateinit var skipButton: Button
    private lateinit var swipeHintText: TextView
    private lateinit var streakBadgeCard: CardView
    
    // Streak dots
    private lateinit var streakDot1: View
    private lateinit var streakDot2: View
    private lateinit var streakDot3: View
    private lateinit var streakText: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        progressBar = view.findViewById(R.id.progressBar)
        questionCounterText = view.findViewById(R.id.questionCounterText)
        streakContainer = view.findViewById(R.id.streakContainer)
        questionText = view.findViewById(R.id.questionText)
        optionsRecyclerView = view.findViewById(R.id.optionsRecyclerView)
        skipButton = view.findViewById(R.id.skipButton)
        swipeHintText = view.findViewById(R.id.swipeHintText)
        streakBadgeCard = view.findViewById(R.id.streakBadgeCard)
        
        // Initialize streak dots
        val streakLinearLayout = streakContainer.getChildAt(0) as LinearLayout
        streakDot1 = streakLinearLayout.getChildAt(0)
        streakDot2 = streakLinearLayout.getChildAt(1)
        streakDot3 = streakLinearLayout.getChildAt(2)
        streakText = streakContainer.getChildAt(1) as TextView
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        setupSwipeGesture(view)
    }
    
    private fun setupRecyclerView() {
        optionsAdapter = OptionsAdapter { index ->
            viewModel.selectAnswer(index)
        }
        
        optionsRecyclerView.apply {
            adapter = optionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    
    private fun setupObservers() {
        viewModel.quizState.observe(viewLifecycleOwner) { state ->
            // Update progress bar
            progressBar.progress = (viewModel.getProgress() * 100).toInt()
            
            // Update question counter
            val currentQuestionIndex = state.currentQuestionIndex + 1
            val totalQuestions = state.questions.size
            questionCounterText.text = "Question $currentQuestionIndex of $totalQuestions"
            
            // Update question text
            val currentQuestion = viewModel.getCurrentQuestion()
            currentQuestion?.let { question ->
                questionText.text = question.question
                
                val optionItems = question.options.mapIndexed { index, option ->
                    OptionItem(
                        text = option,
                        letter = "${('A' + index)}",
                        isSelected = state.selectedAnswerIndex == index,
                        isCorrect = state.isAnswerRevealed && index == question.correctOptionIndex,
                        isWrong = state.isAnswerRevealed && state.selectedAnswerIndex == index && index != question.correctOptionIndex,
                        index = index
                    )
                }
                optionsAdapter.submitList(optionItems)
            }
            
            // Update streak visibility
            streakContainer.visibility = if (state.currentStreak > 0) View.VISIBLE else View.GONE
            
            // Update streak dots
            updateStreakDots(state.currentStreak)
            
            // Update swipe hint visibility
            swipeHintText.visibility = if (!state.isAnswerRevealed) View.VISIBLE else View.GONE
            
            // Update streak badge visibility
            streakBadgeCard.visibility = if (state.showStreakBadge) View.VISIBLE else View.GONE
            
            // Navigate to results when quiz is complete
            if (state.showResult) {
                findNavController().navigate(R.id.action_questionFragment_to_resultsFragment)
            }
            
            // Auto-advance after 2 seconds when answer is revealed
            if (state.isAnswerRevealed) {
                Handler(Looper.getMainLooper()).postDelayed({
                    // Navigation will be handled by ViewModel's nextQuestion logic
                }, 2000)
            }
            
            // Dismiss streak badge after 2 seconds
            if (state.showStreakBadge) {
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.dismissStreakBadge()
                }, 2000)
            }
        }
    }
    
    private fun setupClickListeners() {
        skipButton.setOnClickListener {
            viewModel.skipQuestion()
        }
        
        streakBadgeCard.setOnClickListener {
            viewModel.dismissStreakBadge()
        }
    }

    private fun setupSwipeGesture(root: View) {
        val swipeThresholdPx = 100       // min horizontal distance
        val velocityThresholdPx = 400    // min horizontal velocity

        val detector = GestureDetectorCompat(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean = true

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val diffX = e2.x - e1!!.x
                    val diffY = e2.y - e1!!.y

                    // Prefer horizontal swipes, ignore mostly-vertical gestures
                    if (abs(diffX) > abs(diffY)
                        && abs(diffX) > swipeThresholdPx
                        && abs(velocityX) > velocityThresholdPx
                    ) {
                        // Left or right — both skip
                        viewModel.skipQuestion()
                        return true
                    }
                    return false
                }
            }
        )

        // Attach to the whole root view
        root.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
        }

        // Also forward touch events from the RecyclerView so swiping over options works.
        // This won't block vertical scrolling because we check direction/threshold above.
        optionsRecyclerView.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
        }
    }
    
    private fun updateStreakDots(currentStreak: Int) {
        // Reset all dots to inactive
        streakDot1.setBackgroundResource(R.drawable.streak_dot_inactive)
        streakDot2.setBackgroundResource(R.drawable.streak_dot_inactive)
        streakDot3.setBackgroundResource(R.drawable.streak_dot_inactive)
        
        // Update dots based on current streak (max 3, then reset)
        when (currentStreak) {
            1 -> {
                streakDot1.setBackgroundResource(R.drawable.streak_dot_active)
            }
            2 -> {
                streakDot1.setBackgroundResource(R.drawable.streak_dot_active)
                streakDot2.setBackgroundResource(R.drawable.streak_dot_active)
            }
            3 -> {
                streakDot1.setBackgroundResource(R.drawable.streak_dot_active)
                streakDot2.setBackgroundResource(R.drawable.streak_dot_active)
                streakDot3.setBackgroundResource(R.drawable.streak_dot_active)
            }
            // For streak > 3, show as 3 (max streak achieved)
            else -> {
                streakDot1.setBackgroundResource(R.drawable.streak_dot_active)
                streakDot2.setBackgroundResource(R.drawable.streak_dot_active)
                streakDot3.setBackgroundResource(R.drawable.streak_dot_active)
            }
        }
        
        // Update streak text
        streakText.text = "$currentStreak streak!"
    }
}
