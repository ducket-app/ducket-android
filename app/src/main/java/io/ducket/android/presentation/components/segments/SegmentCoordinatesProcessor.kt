package io.ducket.android.presentation.components.segments

import kotlin.math.*

internal class SegmentCoordinatesProcessor {

    fun segmentCoordinates(position: Int, segmentsCount: Int, width: Float, spacing: Float): SegmentCoordinates {
        val segmentWidth = (width - spacing * (segmentsCount - 1)) / segmentsCount

        val topLeft = (segmentWidth + spacing) * position
        val bottomLeft = (segmentWidth + spacing) * position
        val topRight = segmentWidth * (position + 1) + spacing * position
        val bottomRight = segmentWidth * (position + 1) + spacing * position

        return SegmentCoordinates(topLeft, topRight, bottomLeft, bottomRight)
    }

    fun progressCoordinates(progress: Float, segmentsCount: Int, width: Float, spacing: Float): SegmentCoordinates {
        val segmentWidth = (width - spacing * (segmentsCount - 1)) / segmentsCount

        val topLeft = (segmentWidth + spacing) * ceil(progress - 1)
        val bottomLeft = (segmentWidth + spacing) * ceil(progress - 1)
        val topRight = segmentWidth * progress + spacing * ceil(progress - 1)
        val bottomRight = segmentWidth * progress + spacing * ceil(progress - 1)

        return SegmentCoordinates(topLeft, topRight, bottomLeft, bottomRight)
    }
}