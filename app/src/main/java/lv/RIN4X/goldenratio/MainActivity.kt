package lv.RIN4X.goldenratio

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lengthA = findViewById<EditText>(R.id.etLA)
        val lengthB = findViewById<EditText>(R.id.etLB)
        val result = findViewById<EditText>(R.id.etResult)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val showPreviousResults = findViewById<CheckBox>(R.id.cbShowPrevious)
        val clearButton = findViewById<Button>(R.id.btnClear)
        val container = findViewById<LinearLayout>(R.id.linearResults)
        val tvDisplayA = findViewById<TextView>(R.id.tvDisplayA)
        val tvDisplayB = findViewById<TextView>(R.id.tvDisplayB)
        val tvDisplaySUM = findViewById<TextView>(R.id.tvDisplaySUM)
        val scrollView = findViewById<ScrollView>(R.id.svPreviousResults)

        fun clearInputs() {
            lengthA.text.clear()
            lengthB.text.clear()
            result.text.clear()
        }

        fun clearALL() {
            lengthA.text.clear()
            lengthB.text.clear()
            result.text.clear()
            tvDisplayA.text = "a"
            tvDisplayB.text = "b"
            tvDisplaySUM.text = "a + b"
        }

        btnCalculate.setOnClickListener {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getTimeInstance()
            val formatedDate = formatter.format(date)

            if (lengthA.text.toString() == "" && lengthB.text.toString() == "" && result.text.toString() == "") {
                // error check: A,B,Result
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_nothing_entered),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (lengthA.text.toString() != "" && lengthB.text.toString() != "" && result.text.toString() != "") {
                // all entered/calculated, clearing
                clearInputs()
            } else if (lengthA.text.toString() != "" && result.text.toString() != "") {
                // error check: A, Result
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_a_and_result_can_t_have_values),
                    Toast.LENGTH_SHORT
                ).show()
                clearALL()
            } else if (lengthB.text.toString() != "" && result.text.toString() != "") {
                // error check: B, Result
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_b_and_result_can_t_have_values),
                    Toast.LENGTH_SHORT
                ).show()
                clearALL()
            } else if (lengthB.text.toString() != "" && lengthA.text.toString() != "" && result.text.toString() == "") {
                // error check: A,B entered
                Toast.makeText(
                    applicationContext, getString(R.string.error_a_and_b), Toast.LENGTH_SHORT
                ).show()
                clearALL()
            } else if (lengthA.text.toString() != "") {
                // calc B, Result
                val calculationPair =
                    GRCalculator.calculateResultAndBFromA(lengthA.text.toString().toDouble())

                val resultValue = "%.1f".format(calculationPair.first)
                val lengthBValue = "%.1f".format(calculationPair.second)

                // Animate result EditText
                result.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(resultValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }
                // Animate lengthB EditText
                lengthB.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(lengthBValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }

                // Animate "On line" TextViews with a slight delay for each
                tvDisplayA.apply {
                    alpha = 0f
                    translationY = -20f
                    text = "${lengthA.text}"
                    animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }

                // Add a small delay for cascade effect
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplayB.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${lengthB.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 50)

                // Add another small delay for the final element
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplaySUM.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${result.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 100)

                if (showPreviousResults.isChecked) {
                    // Create a new TextView and add it to the container
                    val textView = TextView(this).apply {
                        text = "[$formatedDate]\n" + context.getString(
                            R.string.results_B_phi_A_given, lengthA.text, lengthB.text, result.text
                        )
                        textSize = 16f
                        setPadding(16, 8, 16, 8)
                        // Initially set alpha to 0 (invisible)
                        alpha = 0f
                        // Set initial translation to appear from the right
                        translationX = 100f
                    }
                    container.addView(textView) // Add to ScrollView's LinearLayout
                    // Animate the TextView
                    textView.animate()
                        .alpha(1f)           // Fade in to fully visible
                        .translationX(0f)    // Move to original position
                        .setDuration(300)    // Animation duration in milliseconds
                        .setInterpolator(DecelerateInterpolator()) // Smooth deceleration
                        .start()
                    // Scroll to the bottom AFTER layout is updated
                    scrollView.post {
                        // Scroll to the last child (most recently added)
                        scrollView.smoothScrollTo(0, container.bottom)
                    }
                }
            } else if (lengthB.text.toString() != "") {
                // calc A, result
                val calculationPair =
                    GRCalculator.calculateResultAndAFromB(lengthB.text.toString().toDouble())

                val resultValue = "%.1f".format(calculationPair.first)
                val lengthAValue = "%.1f".format(calculationPair.second)

                // Animate result EditText
                result.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(resultValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }

                // Animate lengthA EditText
                lengthA.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(lengthAValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }

                // Animate "On line" TextViews with a slight delay for each
                tvDisplayA.apply {
                    alpha = 0f
                    translationY = -20f
                    text = "${lengthA.text}"
                    animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }

                // Add a small delay for cascade effect
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplayB.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${lengthB.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 50)

                // Add another small delay for the final element
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplaySUM.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${result.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 100)

                if (showPreviousResults.isChecked) {
                    val textView = TextView(this).apply {
                        text = "[$formatedDate]\n" + context.getString(
                            R.string.length_a_length_b_given_sum,
                            lengthA.text,
                            lengthB.text,
                            result.text
                        )
                        textSize = 16f
                        setPadding(16, 8, 16, 8)
                        // Initially set alpha to 0 (invisible)
                        alpha = 0f
                        // Set initial translation to appear from the right
                        translationX = 100f
                    }

                    container.addView(textView) // Add to LinearLayout inside ScrollView
                    // Animate the TextView
                    textView.animate()
                        .alpha(1f)           // Fade in to fully visible
                        .translationX(0f)    // Move to original position
                        .setDuration(300)    // Animation duration in milliseconds
                        .setInterpolator(DecelerateInterpolator()) // Smooth deceleration
                        .start()
                    // Scroll to the bottom AFTER layout is updated
                    scrollView.post {
                        // Scroll to the last child (most recently added)
                        scrollView.smoothScrollTo(0, container.bottom)
                    }
                }
            } else if (result.text.toString() != "") {
                // calc A, B based on Result
                val calculationPair =
                    GRCalculator.calculateABFromResult(result.text.toString().toDouble())

                val lengthAValue = "%.1f".format(calculationPair.first)
                val lengthBValue = "%.1f".format(calculationPair.second)

                // Animate lengthA EditText
                lengthA.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(lengthAValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }

                // Animate lengthB EditText
                lengthB.apply {
                    alpha = 0f
                    scaleX = 0.9f
                    scaleY = 0.9f
                    setText(lengthBValue)
                    animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(250)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .start()
                }

                // Animate "On line" TextViews with a slight delay for each
                tvDisplayA.apply {
                    alpha = 0f
                    translationY = -20f
                    text = "${lengthA.text}"
                    animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }

                // Add a small delay for cascade effect
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplayB.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${lengthB.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 50)

                // Add another small delay for the final element
                Handler(Looper.getMainLooper()).postDelayed({
                    tvDisplaySUM.apply {
                        alpha = 0f
                        translationY = -20f
                        text = "${result.text}"
                        animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }, 100)

                if (showPreviousResults.isChecked) {
                    val textView = TextView(this).apply {
                        text = "[$formatedDate]\n" + context.getString(
                            R.string.length_a_length_b_sum_given,
                            lengthA.text,
                            lengthB.text,
                            result.text
                        )
                        textSize = 16f
                        setPadding(16, 8, 16, 8)
                        // Initially set alpha to 0 (invisible)
                        alpha = 0f
                        // Set initial translation to appear from the right
                        translationX = 100f
                    }
                    container.addView(textView) // Add to LinearLayout inside ScrollView
                    // Animate the TextView
                    textView.animate()
                        .alpha(1f)           // Fade in to fully visible
                        .translationX(0f)    // Move to original position
                        .setDuration(300)    // Animation duration in milliseconds
                        .setInterpolator(DecelerateInterpolator()) // Smooth deceleration
                        .start()
                    // Scroll to the bottom AFTER layout is updated
                    scrollView.post {
                        // Scroll to the last child (most recently added)
                        scrollView.smoothScrollTo(0, container.bottom)
                    }
                }
            }
        }
        clearButton.setOnClickListener {
            container.removeAllViews() // clear all scrollview textviews
        }
    }
}

private object GRCalculator {
    private const val PHI: Double = 1.618033988749895
    private const val INV_PHI: Double = 0.618033988749895

    // Calculate A and B based on the total result
    fun calculateABFromResult(total: Double): Pair<Double, Double> {
        val a = total * INV_PHI
        val b = total - a
        return Pair(a, b)
    }

    // Calculate result and B based on given A
    fun calculateResultAndBFromA(a: Double): Pair<Double, Double> {
        val b = a / PHI
        val total = a + b
        return Pair(total, b)
    }

    // Calculate result and A based on given B
    fun calculateResultAndAFromB(b: Double): Pair<Double, Double> {
        val a = b * PHI
        val total = a + b
        return Pair(total, a)
    }
}