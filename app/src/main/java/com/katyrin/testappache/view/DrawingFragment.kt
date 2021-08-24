package com.katyrin.testappache.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.katyrin.testappache.R
import com.katyrin.testappache.bus.AppEvent
import com.katyrin.testappache.bus.EventBus
import com.katyrin.testappache.databinding.FragmentDrawingBinding
import com.katyrin.testappache.model.entities.ArrayStroke
import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.utils.getColorByThemeAttr
import com.katyrin.testappache.utils.toast
import com.katyrin.testappache.viewmodel.DrawingState
import com.katyrin.testappache.viewmodel.DrawingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class DrawingFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val eventBus: EventBus by inject()
    private lateinit var model: DrawingViewModel
    private var binding: FragmentDrawingBinding? = null
    private var contentId: Int? = null
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentDrawingBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentId = DrawingFragmentArgs.fromBundle(requireArguments()).contentId
        scope.launch { eventBus.events.collectLatest { handleAppEvent(it) } }
        restoreState(savedInstanceState)
        iniViewModel()
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

    private fun iniViewModel() {
        val viewModel: DrawingViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) { renderData(it) }
        contentId?.let { model.getProjectById(it) }
    }

    private fun renderData(drawingState: DrawingState) {
        when (drawingState) {
            is DrawingState.SuccessSave -> requireActivity().onBackPressed()
            is DrawingState.Error -> toast(drawingState.message)
            is DrawingState.Success -> initPaintView(drawingState.contentData)
        }
    }

    private fun initPaintView(contentData: ContentData) {
        binding?.paintView?.init(contentData.bitmap)
        name = contentData.name
    }

    private fun initViews() {
        binding?.apply {
            cancelButton.setOnClickListener { paintView.undo() }
            repeatButton.setOnClickListener { paintView.redo() }
            brushButton.setOnClickListener { selectBrush() }
            eraserButton.setOnClickListener { selectEraser() }
            saveButton.setOnClickListener { saveImage() }
        }
    }

    private fun selectBrush(): Unit =
        getColorByThemeAttr(R.attr.alwaysBlackColor).let { color ->
            binding?.paintView?.setStrokeColor(color)
            binding?.brushSizeView?.setColor(color)
        }

    private fun selectEraser(): Unit =
        getColorByThemeAttr(R.attr.alwaysWhiteColor).let { color ->
            binding?.paintView?.setStrokeColor(color)
            binding?.brushSizeView?.setColor(color)
        }

    private fun saveImage(): Unit =
        model.saveImage(ContentData(binding?.paintView?.getBitmapImage(), name!!, contentId!!))

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
        contentId = null
        name = null
        super.onDestroy()
    }

    companion object {
        private const val ARRAY_STROKE = "ARRAY_STROKE"
        private const val BRUSH_COLOR_BUNDLE = "BRUSH_COLOR_BUNDLE"
        private const val BRUSH_SIZE = "BRUSH_COLOR"
    }
}