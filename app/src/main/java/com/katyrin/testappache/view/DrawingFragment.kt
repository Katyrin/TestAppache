package com.katyrin.testappache.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import com.katyrin.testappache.R
import com.katyrin.testappache.databinding.FragmentDrawingBinding
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
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}