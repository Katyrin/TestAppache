package com.katyrin.testappache.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.katyrin.testappache.R
import com.katyrin.testappache.databinding.FragmentDrawingBinding
import com.katyrin.testappache.model.entities.ArrayStroke
import com.katyrin.testappache.utils.getColorByThemeAttr
import com.katyrin.testappache.viewmodel.DrawingViewModel


class DrawingFragment : Fragment() {

    private lateinit var viewModel: DrawingViewModel
    private var binding: FragmentDrawingBinding? = null
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentDrawingBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DrawingViewModel::class.java)
        mDetector = GestureDetectorCompat(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    binding?.innerBrushSizeImage?.layoutParams?.width = 78
                    Toast.makeText(requireContext(), "scroll", Toast.LENGTH_SHORT).show()
                    return true
                }

            }
        )
        binding?.brushSizeImageLayout?.setOnLongClickListener { view ->
            view.setOnTouchListener { _, event ->
                mDetector.onTouchEvent(event)
                true
            }
            true
        }
//        binding?.brushSizeImageLayout?.setOnTouchListener { _, event ->
//            mDetector.onTouchEvent(event)
//            true
//        }
        savedInstanceState?.apply {
            binding?.paintView?.setStrokeColor(getInt(BRUSH_COLOR))
            getParcelable<ArrayStroke>(ARRAY_STROKE)?.let { binding?.paintView?.setStrokes(it) }
        }
        initViews()
    }

    private fun initViews() {
        binding?.apply {
            cancelButton.setOnClickListener { paintView.undo() }
            repeatButton.setOnClickListener { paintView.redo() }
            brushButton.setOnClickListener {
                paintView.setStrokeColor(getColorByThemeAttr(R.attr.alwaysBlackColor))
            }
            eraserButton.setOnClickListener {
                paintView.setStrokeColor(getColorByThemeAttr(R.attr.alwaysWhiteColor))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARRAY_STROKE, binding?.paintView?.getStrokes())
        binding?.paintView?.getStrokeColor()?.let { outState.putInt(BRUSH_COLOR, it) }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        private const val ARRAY_STROKE = "ARRAY_STROKE"
        private const val BRUSH_COLOR = "BRUSH_COLOR"
    }
}