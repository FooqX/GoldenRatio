package lv.RIN4X.goldenratio

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Components
        val lengthA = findViewById<EditText>(R.id.etLA)
        val lengthB = findViewById<EditText>(R.id.etLB)
        val result = findViewById<EditText>(R.id.etResult)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val showPreviousResults = findViewById<CheckBox>(R.id.cbShowPrevious)
        val clearButton = findViewById<Button>(R.id.btnClear)
        val container = findViewById<LinearLayout>(R.id.linearResults)
        // end

        btnCalculate.setOnClickListener {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getTimeInstance() //or use getDateInstance()
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
                lengthA.text.clear()
                lengthB.text.clear()
                result.text.clear()
            } else if (lengthA.text.toString() != "" && result.text.toString() != "") {
                // error check: A, Result
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_a_and_result_can_t_have_values),
                    Toast.LENGTH_SHORT
                ).show()
                lengthA.text.clear()
                lengthB.text.clear()
                result.text.clear()
            } else if (lengthB.text.toString() != "" && result.text.toString() != "") {
                // error check: B, Result
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_b_and_result_can_t_have_values),
                    Toast.LENGTH_SHORT
                ).show()
                lengthA.text.clear()
                lengthB.text.clear()
                result.text.clear()
            } else if (lengthB.text.toString() != "" && lengthA.text.toString() != "" && result.text.toString() == "") {
                // error check: A,B entered
                Toast.makeText(
                    applicationContext, getString(R.string.error_a_and_b), Toast.LENGTH_SHORT
                ).show()
                lengthA.text.clear()
                lengthB.text.clear()
                result.text.clear()
            } else if (lengthA.text.toString() != "") {
                // calc B, Result
                val calculationPair =
                    GRCalculator.calculateResultAndBFromA(lengthA.text.toString().toDouble())
                result.setText("%.1f".format(calculationPair.first))
                lengthB.setText("%.1f".format(calculationPair.second))
                // end
                if (showPreviousResults.isChecked) {
                    // Create a new TextView and add it to the container
                    val textView = TextView(this).apply {
                        text = "[$formatedDate]\n" + context.getString(
                            R.string.results_B_phi_A_given, lengthA.text, lengthB.text, result.text
                        )
                        textSize = 16f
                        setPadding(16, 8, 16, 8)
                    }
                    container.addView(textView) // Add to ScrollView's LinearLayout
                }
            } else if (lengthB.text.toString() != "") {
                // calc A, result
                val calculationPair =
                    GRCalculator.calculateResultAndAFromB(lengthB.text.toString().toDouble())
                result.setText("%.1f".format(calculationPair.first))
                lengthA.setText("%.1f".format(calculationPair.second))

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
                    }

                    container.addView(textView) // Add to LinearLayout inside ScrollView
                }
            } else if (result.text.toString() != "") {
                // calc A, B based on Result
                val calculationPair =
                    GRCalculator.calculateABFromResult(result.text.toString().toDouble())
                lengthA.setText("%.1f".format(calculationPair.first))
                lengthB.setText("%.1f".format(calculationPair.second))

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
                    }
                    container.addView(textView) // Add to LinearLayout inside ScrollView
                }
            }
        }
        clearButton.setOnClickListener {
            container.removeAllViews()
        }
    }
}

// Calculation logic using Golden Ratio
private object GRCalculator {
    private const val PHI: Double = 1.618033988749895 // Golden ratio
    private const val INV_PHI: Double = 0.618033988749895 // 1 / phi

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

