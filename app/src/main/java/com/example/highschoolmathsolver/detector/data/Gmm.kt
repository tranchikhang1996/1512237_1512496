package com.example.highschoolmathsolver.detector.data

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Math.pow
import kotlin.math.PI
import kotlin.math.exp


class Gmm {
    private var C: Int = 0
    private var D: Int = 0
    private var G: Int = 0
    private var invcov: Array<Array<Double>> = arrayOf()
    var mean: Array<Array<Double>> = arrayOf()
    var weight: Array<Array<Double>> = arrayOf()
    var prior: Array<Double> = arrayOf()
    var det: Array<Double> = arrayOf()

    fun loadModel(spaFile: File) {
        val fileValues = arrayListOf<Double>()
        val b = BufferedReader(FileReader(spaFile))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(' ')
            values.forEach { fileValues.add(it.toDouble()) }
            line = b.readLine()
        }
        b.close()
        C = fileValues[0].toInt()
        D = fileValues[1].toInt()
        G = fileValues[2].toInt()
        invcov = Array(C * G, init = { Array(D) { 0.0 } })
        mean = Array(C * G, init = { Array(D) { 0.0 } })
        weight = Array(C, init = { Array(G) { 0.0 } })
        det = Array(C * G, init = { 0.0 })
        prior = fileValues.subList(3, 3 + C).toTypedArray()
        var pos = C + 3
        for (c in 0 until C) {

            for (i in 0 until G) {
                det[c * G + i] = 1.0

                for (j in 0 until D) {
                    invcov[c * G + i][j] = fileValues[pos++]

                    det[c * G + i] *= invcov[c * G + i][j]

                    if (invcov[c * G + i][j] == 0.0) {
                        invcov[c * G + i][j] = 1.0 / 1.0e-10
                    } else
                        invcov[c * G + i][j] = 1.0 / invcov[c * G + i][j]
                }
            }

            for (i in 0 until G) {
                for (j in 0 until D)
                    mean[c * G + i][j] = fileValues[pos++]
            }

            for (i in 0 until G) {
                weight[c][i] = fileValues[pos++]
            }
        }
    }

    private fun pdf(c: Int, v: Array<Double>): Double {
        var prob = 0.0
        for (i in 0 until G) {

            var exponent = 0.0
            for (j in 0 until D)
                exponent += (v[j] - mean[c * G + i][j]) * invcov[c * G + i][j] * (v[j] - mean[c * G + i][j])

            exponent *= -0.5

            prob += weight[c][i] * pow(2 * PI, -D / 2.0) * pow(det[c * G + i], -0.5) * exp(exponent)
        }

        return prior[c] * prob
    }

    fun posterior(features: Array<Double>): Array<Double> {
        val prob = Array(C, init = { 0.0 })
        var total = 0.0
        for (i in 0 until C) {
            prob[i] = pdf(i, features)
            total += prob[i]
        }

        for (i in 0 until C) {
            prob[i] /= total
        }
        return prob
    }
}