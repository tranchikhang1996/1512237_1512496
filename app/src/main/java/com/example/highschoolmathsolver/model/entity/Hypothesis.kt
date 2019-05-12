package com.example.highschoolmathsolver.model.entity

import org.opencv.core.Rect

data class Hypothesis(var values: List<Pair<String, Double>>, var boundingBox: Rect) {
    fun getBestPrediction(): Pair<String, Double> = values.maxBy { it.second } ?: Pair("", 0.0)
}