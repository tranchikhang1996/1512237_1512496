package com.example.highschoolmathsolver.detector.data

import com.example.highschoolmathsolver.detector.data.rule.Rule

class Candidate(
    val expression: String,
    val prob: Double,
    val hypothesis: Hypothesis,
    val rule : Rule
)