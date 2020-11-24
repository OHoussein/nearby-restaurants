package dev.ohoussein.restos.ui.feature.venues.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.ohoussein.restos.databinding.ActivityRestaurantsBinding
import dev.ohoussein.restos.ui.core.model.UiVenue
import dev.ohoussein.restos.ui.core.util.ActivityUtils.openMapsOn
import dev.ohoussein.restos.ui.core.util.ViewUtils.showOrHideIfNull
import dev.ohoussein.restos.ui.feature.venues.viewmodel.ShareRestaurantsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantsActivity : AppCompatActivity() {


    companion object {
        const val BS_SHOW_HIDE_ANIMATION_DURATION_MS = 200L
    }

    private val bottomSheet: BottomSheetBehavior<ConstraintLayout> by lazy {
        BottomSheetBehavior.from(binding.bottomSheet.bsDetailsVenue)
    }
    private lateinit var binding: ActivityRestaurantsBinding

    private val sharedViewModel: ShareRestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomSheet.btnVenueDirection.setOnClickListener {
            sharedViewModel.selectedVenue.value?.let {
                openMapsOn(it.location)
            }
        }

        bottomSheet.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState >= BottomSheetBehavior.STATE_COLLAPSED) {
                    sharedViewModel.selectedVenue.value = null
                }
            }
        })

        sharedViewModel.selectedVenue.observe(this, { venue: UiVenue? ->
            if (venue != null) {
                showBottomSheet(venue)
            } else {
                bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            }
        })
    }


    private fun showBottomSheet(item: UiVenue) {
        lifecycleScope.launch {
            if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                delay(BS_SHOW_HIDE_ANIMATION_DURATION_MS)
            }
            with(binding.bottomSheet) {
                tvVenueName.text = item.name
                val category = (item.categories.firstOrNull { it.primary }
                        ?: item.categories.firstOrNull())
                tvVenueCategory.showOrHideIfNull(category?.name)
                tvVenueAddress.showOrHideIfNull(item.location.address)
            }
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

}
