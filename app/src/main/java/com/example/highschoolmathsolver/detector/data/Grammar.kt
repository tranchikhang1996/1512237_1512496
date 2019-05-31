package com.example.highschoolmathsolver.detector.data

import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.detector.config.ConfigurationProvider
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.data.rule.TerminalRule
import javax.inject.Inject

class Grammar @Inject constructor(private val configurationProvider: ConfigurationProvider) {
    val terminalRules: List<TerminalRule> by lazy { configurationProvider.getConfiguration().terminalRules }
    val binaryRules: Map<SpatialRelationship,List<BinaryRule>> = partRule(configurationProvider.getConfiguration().binaryRules)
    val starSymbol: List<String> by lazy { configurationProvider.getConfiguration().starSymbol }

    private fun partRule(rules : List<BinaryRule>) : Map<SpatialRelationship, List<BinaryRule>> {
        return rules.groupBy { it.sparel }
    }
}