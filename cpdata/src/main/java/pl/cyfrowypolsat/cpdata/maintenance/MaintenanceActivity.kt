package pl.cyfrowypolsat.cpdata.maintenance

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.setPaddingTop
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpdata.R
import pl.cyfrowypolsat.cpdata.maintenance.model.MaintenanceResult
import java.text.SimpleDateFormat
import java.util.*

class MaintenanceActivity : AppCompatActivity() {

    companion object {
        const val MAINTENANCE_RESULT_KEY = "MAINTENANCE_RESULT_KEY"

        const val ONE_SECOND = 1000L

        var instance: MaintenanceActivity? = null
    }

    private var autoRefreshTimer: CountDownTimer? = null
    private var endDateTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
        setLayoutFullscreen()
        intent.getParcelableExtra<MaintenanceResult>(MAINTENANCE_RESULT_KEY)?.let { updateData(it) }
    }

    override fun onResume() {
        super.onResume()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onBackPressed() {
        return
    }

    fun updateData(maintenanceResult: MaintenanceResult) {
        Handler(Looper.getMainLooper()).post {
            val titleView = findViewById<TextView>(R.id.maintenance_title)
            val descriptionView = findViewById<TextView>(R.id.maintenance_description)
            val endDateView = findViewById<TextView>(R.id.maintenance_end_date)
            val autoRefreshView = findViewById<TextView>(R.id.maintenance_auto_refresh)

            titleView.text = maintenanceResult.title

            titleView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    AppCompatResources.getDrawable(this, getIconResourceId(maintenanceResult.icon)),
                    null,
                    null
            )

            descriptionView.text = maintenanceResult.description

            if (maintenanceResult.autoRefreshInterval > 0) {
                autoRefreshView.visible()

                autoRefreshTimer?.cancel()
                autoRefreshTimer = object : CountDownTimer(maintenanceResult.autoRefreshInterval * ONE_SECOND, ONE_SECOND) {
                    override fun onTick(millisUntilFinished: Long) {
                        val autoRefreshText = getString(
                                R.string.maintenance_auto_refresh,
                                SimpleDateFormat("s").format(Date(millisUntilFinished))
                        )
                        autoRefreshView.text = HtmlCompat.fromHtml(autoRefreshText, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }

                    override fun onFinish() {}
                }.start()
            } else {
                autoRefreshView.gone()
            }

            if (maintenanceResult.endData?.plannedEnd != null && maintenanceResult.endData.plannedEnd.after(Date())) {
                endDateView.visible()

                val endDataDescription = maintenanceResult.endData?.description ?: ""
                endDateTimer?.cancel()
                endDateTimer = object : CountDownTimer(calculateTimeToEndDate(maintenanceResult.endData.plannedEnd), ONE_SECOND) {
                    override fun onTick(millisUntilFinished: Long) {
                        val endDateText = endDataDescription + " " +
                                getDateFormat(maintenanceResult.endData?.timeFormat).format(Date(millisUntilFinished))
                        endDateView.text = endDateText
                    }

                    override fun onFinish() {}
                }.start()
            } else {
                endDateView.gone()
            }
        }
    }

    private fun getIconResourceId(icon: String?): Int {
        return if (icon == "info") {
            R.drawable.cpdata_ic_info_in_circle_big
        } else {
            R.drawable.cpdata_ic_exclamation_in_circle_big
        }
    }

    private fun calculateTimeToEndDate(endDate: Date): Long {
        return endDate.time - Date().time
    }

    fun maintenanceFinished() {
        if (!isFinishing && !isDestroyed) {
            finish()
        }
    }

    private fun setLayoutFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val containerView = findViewById<FrameLayout>(R.id.maintenance_main_container)
        ViewCompat.setOnApplyWindowInsetsListener(containerView) { _, insets ->
            containerView.setPaddingTop(insets.systemWindowInsetTop)
            insets
        }
    }

    private fun getDateFormat(timeFormat: String?): SimpleDateFormat {
        return try {
            SimpleDateFormat(timeFormat, Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
        } catch (e: Exception) {
            SimpleDateFormat("HH:mm", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
        }
    }

}