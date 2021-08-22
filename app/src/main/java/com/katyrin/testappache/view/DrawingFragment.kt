package com.katyrin.testappache.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.katyrin.testappache.AppEvent
import com.katyrin.testappache.EventBus
import com.katyrin.testappache.R
import com.katyrin.testappache.databinding.FragmentDrawingBinding
import com.katyrin.testappache.model.entities.ArrayStroke
import com.katyrin.testappache.utils.getColorByThemeAttr
import com.katyrin.testappache.viewmodel.DrawingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class DrawingFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val eventBus: EventBus by inject()
    private lateinit var viewModel: DrawingViewModel
    private var binding: FragmentDrawingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentDrawingBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch { eventBus.events.collectLatest { handleAppEvent(it) } }
        viewModel = ViewModelProvider(this).get(DrawingViewModel::class.java)
        restoreState(savedInstanceState)
        initViews()
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            binding?.paintView?.apply {
                setStrokeColor(getInt(BRUSH_COLOR_BUNDLE))
                getParcelable<ArrayStroke>(ARRAY_STROKE)?.let { setStrokes(it) }
            }
            binding?.brushSizeView?.apply {
                setColor(getInt(BRUSH_COLOR_BUNDLE))
                setBrushSize(getFloat(BRUSH_SIZE))
            }
        }
    }

    private fun handleAppEvent(event: AppEvent) {
        when (event) {
            is AppEvent.UpdateBrushSize -> binding?.paintView?.setBrushSize(event.size)
            is AppEvent.UpdateColor -> binding?.paintView?.setStrokeColor(event.color)
        }
    }

    private fun initViews() {
        binding?.apply {
            cancelButton.setOnClickListener { paintView?.undo() }
            repeatButton.setOnClickListener { paintView?.redo() }
            brushButton.setOnClickListener {
                getColorByThemeAttr(R.attr.alwaysBlackColor).let { color ->
                    paintView?.setStrokeColor(color)
                    brushSizeView?.setColor(color)
                }
            }
            eraserButton.setOnClickListener {
                getColorByThemeAttr(R.attr.alwaysWhiteColor).let { color ->
                    paintView?.setStrokeColor(color)
                    brushSizeView?.setColor(color)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARRAY_STROKE, binding?.paintView?.getStrokes())
        binding?.brushSizeView?.apply {
            outState.putInt(BRUSH_COLOR_BUNDLE, getBrushColor())
            outState.putFloat(BRUSH_SIZE, getBrushSize())
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        private const val ARRAY_STROKE = "ARRAY_STROKE"
        private const val BRUSH_COLOR_BUNDLE = "BRUSH_COLOR_BUNDLE"
        private const val BRUSH_SIZE = "BRUSH_COLOR"
    }
}