package com.example.highschoolmathsolver.detector

import android.graphics.Bitmap
import android.util.SparseArray
import android.widget.ImageView
import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.config.ConfigurationProvider
import com.example.highschoolmathsolver.detector.data.CYKCell
import com.example.highschoolmathsolver.detector.data.Grammar
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.model.SpatialRelationshipModel
import com.example.highschoolmathsolver.detector.model.SymbolRecognitionModel
import com.example.highschoolmathsolver.util.ImageUtils
import com.example.highschoolmathsolver.util.ImageUtils.Companion.mergeRegion
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import timber.log.Timber
import javax.inject.Inject
import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.detector.data.Candidate
import com.example.highschoolmathsolver.detector.data.CombineRegion
import com.example.highschoolmathsolver.extentions.*
import com.example.highschoolmathsolver.util.AndroidUtils
import com.example.highschoolmathsolver.util.CykUtils.Companion.getH
import com.example.highschoolmathsolver.util.CykUtils.Companion.getI
import com.example.highschoolmathsolver.util.CykUtils.Companion.getM
import com.example.highschoolmathsolver.util.CykUtils.Companion.getRX
import com.example.highschoolmathsolver.util.CykUtils.Companion.getRY
import com.example.highschoolmathsolver.util.CykUtils.Companion.getS
import com.example.highschoolmathsolver.util.CykUtils.Companion.getV
import com.example.highschoolmathsolver.util.ImageUtils.Companion.setSymbolRegion
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.opencv.android.Utils
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import org.opencv.core.*


class SCFGDetector @Inject constructor(
    private val symRecModel: SymbolRecognitionModel,
    private val spaRelModel: SpatialRelationshipModel,
    private val grammar: Grammar,
    private val configurationProvider: ConfigurationProvider
) : Detector<String?>() {

    private val locker = Semaphore(1)
    private var frameSize = Rect()
    private val subscriptions = CompositeDisposable()
    private val symbolTypes by lazy { configurationProvider.getConfiguration().symbolTypes }
    private val lMax = 2
    private var rx = 0.0
    private var ry = 0.0
    private val subject = PublishSubject.create<Frame>()
    private var expression: String? = null
    private val timeSpan = 1000L
    var imageView: ImageView? = null
    private val maxN = 20
    private val minN = 0
    private val minBoxW = 4
    private val minBoxH = 4

    init {
        initSubject()
    }

    override fun release() {
        subscriptions.clear()
        locker.release()
        imageView = null
        super.release()
    }

    fun onFrameSizeChange(rect: Rect) {
        locker.acquire()
        frameSize = rect
        Timber.d("SCFGDetector change frame size = ${frameSize.x} ${frameSize.y} ${frameSize.width} ${frameSize.height}")
        locker.release()
    }

    fun detectFromByteArray(byte: ByteArray): String {
        Timber.d("SCFGDetector DetectWithByteArray start")
        CYKCell.currentId = 0
        val src = ImageUtils.toMat(byte, frameSize)
        val thresh = ImageUtils.preProcessing(src)

        val drawImage = Mat()
        thresh.copyTo(drawImage)
        ImageUtils.drawToImageView(drawImage, imageView)

        val result = cyk(thresh)
        Timber.d("SCFGDetector DetectWithByteArray result = $result")
        return result ?: ""
    }

    private fun removeNoise(components: List<Rect>): List<Rect> {
        return components.filter { it.width > minBoxW || it.height > minBoxH }
    }

    private fun initCYK(image: Mat, components: List<Rect>): SparseArray<MutableList<CYKCell>> {
        val table: SparseArray<MutableList<CYKCell>> = SparseArray()
        for (i in 1..components.size) {
            table.put(i, arrayListOf())
        }
        val omega = getRegions(lMax, components)
        var maxID = 0L
        for (i in 1..lMax) {
            for (bi in omega[i]) {
                if (maxID < bi.id) {
                    maxID = bi.id
                }

                val region = mergeRegion(bi.childRegions)
                val predictions = if (region.width >= (2 * rx).toInt() && region.height < (0.2 * rx).toInt())
                    arrayListOf(Pair("-", 1.0))
                else symRecModel.getPrediction(image, region)
                val candidates = HashMap<String, Candidate>()
                val centroid = bi.childRegions.sumBy { (it.y + it.t) / 2 }.toDouble() / i

                for (termRule in grammar.terminalRules) {
                    val symProb = predictions.find { it.first == termRule.s }?.second ?: 0.0
                    if (symProb <= 0.5) {
                        continue
                    }
                    val prob = termRule.prob * symProb
                    val hypothesis = setSymbolRegion(region, centroid, symbolTypes[termRule.s] ?: SymbolType.MIDDLE)
                    val candidate = Candidate(termRule.template, prob, hypothesis, termRule)
                    candidates[termRule.A] = candidate
                }

                val cell = CYKCell(candidates, null, null, -1, -1, region).apply {
                    this.id = bi.id
                    this.childIds.addAll(bi.childIds)
                }
                table[i].add(cell)
            }
            table[i].sortBy { it.region.x }
        }
        CYKCell.currentId = maxID
        return table
    }

    private fun getRegions(l: Int, components: List<Rect>): SparseArray<MutableList<CombineRegion>> {
        val omega = SparseArray<MutableList<CombineRegion>>()
        val step1s = arrayListOf<CombineRegion>()
        var id = 0L
        components.forEach {
            step1s.add(CombineRegion(id, it, null, arrayListOf(id++), arrayListOf(it)))
        }
        omega.put(1, step1s)

        if (l < 2) {
            return omega
        }

        omega.put(2, arrayListOf())
        for (i in 0 until omega[1].size - 1) {
            val combineRegion = omega[1][i]
            val searchRegion = combineRegion.region.getSearchRegion(1, (ry / 2).toInt())
            for (j in i + 1 until omega[1].size) {
                val b = omega[1][j]
                if (!searchRegion.isOverlapOf(b.region)) {
                    continue
                }
                omega[2].add(
                    CombineRegion(
                        id++,
                        combineRegion.region.merged(b.region),
                        Pair(combineRegion, b),
                        arrayListOf(b.id).apply { addAll(combineRegion.childIds) },
                        arrayListOf(b.region).apply { addAll(combineRegion.childRegions) })
                )
            }
        }
        return omega
    }

    private fun cyk(image: Mat): String? {
        val components = removeNoise(ImageUtils.segment(image))
        val n = components.size
        Timber.d("SCFGDetector number of components = $n")
        if (n > maxN || n < minN) {
            return ""
        }
        rx = getRX(components)
        ry = getRY(components)
        val table = initCYK(image, components)
        for (la in 2..n) {

            val iterator = Observable.create<Pair<List<CYKCell>, List<CYKCell>>> {

                for (lb in 1 until la) {
                    val lc = la - lb
                    it.onNext(Pair(table[lb], table[lc]))
                }
                it.onComplete()

            }.flatMap({ pair ->

                Observable.create<List<CYKCell>> {
                    val results = combine(pair.first, pair.second)
                    it.onNext(results)
                    it.onComplete()

                }.subscribeOn(Schedulers.io())

            }, 20).blockingIterable()

            iterator.forEach { participants ->
                for (p in participants) {
                    val cell =
                        table[la].findLast { it.childIds.size == p.childIds.size && it.childIds.containsAll(p.childIds) }
                    if (cell == null) {
                        p.id = CYKCell.currentId
                        table[la].add(p)
                        continue
                    }
                    for ((key, value) in p.candidates) {
                        val cellCandidate = cell.candidates[key]
                        if (cellCandidate == null || cellCandidate.prob < value.prob) {
                            cell.candidates[key] = value
                        }
                    }
                }
            }

            table[la].sortBy { it.region.x }
        }
        return trace(table, n)
    }


    private fun combine(listB: List<CYKCell>, listC: List<CYKCell>): List<CYKCell> {
        val results = arrayListOf<CYKCell>()
        for (bB in listB) {
            val setH = getH(bB, listC, rx.toInt(), ry.toInt())
            val setV = getV(bB, listC, rx.toInt(), ry.toInt())
            val setI = getI(bB, listC, rx.toInt(), ry.toInt())
            val setM = getM(bB, listC, rx.toInt(), ry.toInt())
            val setS = getS(bB, listC, rx.toInt(), ry.toInt())

            for (bC in setH) {
                add(SpatialRelationship.HORIZONTAL, results, bB, bC)
                add(SpatialRelationship.SUPERSCRIPT, results, bB, bC)
                add(SpatialRelationship.SUBSCRIPT, results, bB, bC)
            }

            for (bC in setV) {
                add(SpatialRelationship.VERTICAL, results, bB, bC)
                add(SpatialRelationship.VE, results, bB, bC)
            }

            for (bC in setI) {
                add(SpatialRelationship.INSIDE, results, bB, bC)
            }

            for (bC in setM) {
                add(SpatialRelationship.ROOT, results, bB, bC)
            }

            for (bC in setS) {
                add(SpatialRelationship.SSE, results, bB, bC)
            }
        }
        return results
    }

    private fun add(
        rls: SpatialRelationship,
        table: MutableList<CYKCell>,
        b: CYKCell,
        c: CYKCell
    ) {
        val rules =
            grammar.binaryRules[rls]?.filter { b.candidates.containsKey(it.B) && c.candidates.containsKey(it.C) }
        val candidates = HashMap<String, Candidate>()
        val region = b.region.merged(c.region)

        rules?.forEach {
            val candidateB = b.candidates[it.B]
            val candidateC = c.candidates[it.C]
            if (candidateB != null && candidateC != null) {
                val spaProb = spaRelModel.getProb(candidateB.hypothesis, candidateC.hypothesis, it.sparel, rx, ry)
                val candidateA = fusion(region, candidateB, candidateC, it, spaProb)
                candidateA?.let { c -> candidates[it.A] = c }
            }
        }

        if (candidates.isEmpty()) {
            return
        }

        val child = (b.childIds + c.childIds).distinct()
        table.add(CYKCell(candidates, b, c, b.id, c.id, region).apply {
            this.childIds.addAll(child)
        })
    }

    private fun fusion(region: Rect, cB: Candidate, cC: Candidate, rule: BinaryRule, spaProb: Double): Candidate? {
        if (spaProb < 0.3) {
            return null
        }

        val prob = cB.prob * cC.prob * rule.prob * spaProb

        val hypothesis = rule.mergeRegions(region, cB.hypothesis, cC.hypothesis)

        val expression = rule.template.replace("$1", cB.expression).replace("$2", cC.expression)

        return Candidate(expression, prob, hypothesis, rule)
    }

    private fun trace(table: SparseArray<MutableList<CYKCell>>, n: Int): String? {
        var expression: String? = null
        var maxProb = 0.0
        for (cell in table[n]) {
            for ((a, candidate) in cell.candidates) {
                if (!grammar.starSymbol.contains(a)) {
                    continue
                }
                if (candidate.prob < maxProb) {
                    continue
                }
                maxProb = candidate.prob
                expression = candidate.expression
            }
        }
        return expression
    }

    override fun detect(frame: Frame?): SparseArray<String?> {
        val result = SparseArray<String?>()
        result.put(0, expression)
        return result
    }

    private fun process(frame: Frame): String? {
        locker.acquire()
        val src = ImageUtils.toMat(frame, frameSize)
        locker.release()
        val thresh = ImageUtils.preProcessing(src)
        val bitmap = Bitmap.createBitmap(thresh.cols(), thresh.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(thresh, bitmap)
        AndroidUtils.runOnUIThreadWithRxjava { imageView?.setImageBitmap(bitmap) }
        return cyk(thresh)
    }

    private fun updateValue(result: String?) {
        Timber.d("testObservable = $result")
        expression = result
    }

    private fun initSubject() {
        val disposable = subject.toFlowable(BackpressureStrategy.LATEST)
            .delay(2000L, TimeUnit.MILLISECONDS)
            .throttleFirst(timeSpan, TimeUnit.MILLISECONDS)
            .map { process(it) }
            .onErrorReturnItem("")
            .subscribe(
                { updateValue(it) },
                { throwable ->
                    Timber.d(throwable, "[MEParser] onError!")
                    locker.release()
                })
        subscriptions.add(disposable)
    }
}


/**
private fun traceLog(key : String ,cell : CYKCell?) : String {

if(cell == null) {
return ""
}

val candidate = cell.candidates[ key ] ?: return ""
if(candidate.rule is BinaryRule) {
val binaryRule = candidate.rule
val leftKey = binaryRule.B
val rightKey = binaryRule.C
val left = traceLog(leftKey, cell.B)
val right = traceLog(rightKey, cell.C)
Timber.d("testObservable $left and $right with ${binaryRule.sparel}")
}
return "${candidate.expression} [${candidate.hypothesis.region.x} ${candidate.hypothesis.region.y} ${candidate.hypothesis.region.width} ${candidate.hypothesis.region.height} "
}
 **/