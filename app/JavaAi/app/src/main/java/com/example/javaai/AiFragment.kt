package com.example.javaai

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking

class AiFragment : Fragment() {
    private val conversationHistory = StringBuilder() // Stores the history of conversation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.activity_ai, container, false)

        val eTPrompt = view.findViewById<EditText>(R.id.eTPrompt)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        val messageLayout = view.findViewById<LinearLayout>(R.id.messageLayout)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollView)

        btnSubmit.setOnClickListener {
            val prompt = eTPrompt.text.toString()

            // Add the current user input to the history
            conversationHistory.append("User: $prompt\n")

            // Create a TextView for the user message bubble
            val userMessage = TextView(requireContext())
            userMessage.text = prompt
            userMessage.textSize = 18f
            userMessage.setBackgroundResource(R.drawable.user_message_background) // Define the background for user message
            userMessage.gravity = Gravity.START
            userMessage.setPadding(20, 10, 20, 10)

            // Add a margin to create a gap between messages
            val paramsUserMessage = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsUserMessage.setMargins(0, 0, 0, 20) // Set margin bottom for user message
            userMessage.layoutParams = paramsUserMessage

            // Add the user message to the layout
            messageLayout.addView(userMessage)

            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank()) {
                Toast.makeText(requireContext(), "Gemini API key is missing. Add GEMINI_API_KEY to local.properties.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey
            )

            runBlocking {
                // Send the conversation history as context to Gemini
                val response = generativeModel.generateContent(conversationHistory.toString())
                conversationHistory.append("AI: ${response.text}\n") // Add AI response to history

                // Create a TextView for the AI message bubble
                val aiMessage = TextView(requireContext())
                aiMessage.text = response.text
                aiMessage.textSize = 18f
                aiMessage.setBackgroundResource(R.drawable.ai_message_background) // Define the background for AI message
                aiMessage.gravity = Gravity.END
                aiMessage.setPadding(20, 10, 20, 10)

                // Add a margin to create a gap between messages
                val paramsAiMessage = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                paramsAiMessage.setMargins(0, 0, 0, 20) // Set margin bottom for AI message
                aiMessage.layoutParams = paramsAiMessage

                // Add the AI message to the layout
                requireActivity().runOnUiThread {
                    messageLayout.addView(aiMessage)

                    // Scroll to the bottom after adding new messages
                    scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }

            // Clear the EditText after processing
            eTPrompt.setText("")
        }

        return view
    }
}
