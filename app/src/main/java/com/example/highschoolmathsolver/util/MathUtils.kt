package com.example.highschoolmathsolver.util

import android.graphics.Color
import com.example.highschoolmathsolver.mathengine.expression.AddExp
import com.example.highschoolmathsolver.mathengine.expression.FormalExpression
import com.example.highschoolmathsolver.mathengine.expression.MonomialExp
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlin.math.sqrt

class MathUtils {
    companion object {
        public fun haveC(s: String, c: Char): Boolean {
            var count = 0;
            for (i in 0..s.length - 1) {
                if (s[i] == c) {
                    count++
                }
            }
            if (count == 0) {
                return false
            } else {
                return true
            }
        }

        public fun indexOfX(s: String, charOfx: Char): MutableList<Int> {
            var indxofX: MutableList<Int> = ArrayList<Int>()
            indxofX.add(0)
            for (i in 0..s.length - 1) {
                if (s[i] == charOfx) {
                    indxofX.add(i)
                }
            }
            return indxofX
            // tra ve nhung vi tri co bien x trong chuoi latex
        }

        public fun removeM(m: Double, s: String): String {
            // ham nay tra ve chuoi khong co tham so m voi gia tri m  duoc nguoi dung nhap tu truoc
            var result = ""
            var indxofX = indexOfX(s, 'x')
            var start: Int = indxofX[0]
            // xét ở phần số mũ
            for (i in 0..indxofX.size - 2) {
                var heSoCoM: String = ""
                var lessThan0 = false
                var giaTriHeSo = 0.0
                var end: Int = indxofX[i + 1] - 1
                for (j in start..end) {
                    if (s[j] != 'y' && s[j] != '=') {
                        heSoCoM += s[j]
                    }
                    result += s[j]
                }
                if (haveC(heSoCoM, 'm') == true) {
                    var temp = heSoCoM
                    if (temp[0] == '-' && temp[1] == '(') {
                        lessThan0 = true
                        temp = temp.replace("-(", "")
                        temp = temp.replace(")", "")
                    }
                    if (temp[0] == '(') {
                        temp = temp.replace("(", "")
                        temp = temp.replace(")", "")
                    }
                    if (temp[0] == '+' && temp[1] == '(') {
                        lessThan0 = true
                        temp = temp.replace("+(", "")
                        temp = temp.replace(")", "")
                    }
                    var heSoCoMExp = listMonoExptoAddExp(nonMLatexToFormalExpression(temp, 'm'))
                    giaTriHeSo = heSoCoMExp.evalute(m)
                    if (lessThan0 == true) {
                        giaTriHeSo = -giaTriHeSo
                    }
                    if (giaTriHeSo == 1.0 && start == 0) {
                        result = result.replace(heSoCoM, "")
                    } else if (giaTriHeSo == 1.0 && start != 0) {
                        result = result.replace(heSoCoM, "+")
                    } else if (giaTriHeSo == -1.0) {
                        result = result.replace(heSoCoM, "-")
                    } else if (giaTriHeSo > 0.0) {
                        result = result.replace(heSoCoM, "+" + giaTriHeSo)
                    } else {
                        result = result.replace(heSoCoM, "" + giaTriHeSo)
                    }
                }
                if (end + 2 < s.length) {
                    if (s[end + 2] == '^') {
                        start = end + 6
                    } else {
                        start = end + 2
                    }
                } else {
                    start = end + 2
                }
                for (k in end + 1..start - 1) {
                    result += s[k]
                }
            }
            // xét m ở phần hệ số
            if (start < s.length - 1) {
                // neu co hang so
                var heSoCoM: String = ""
                var lessThan0 = false
                var giaTriHeSo: Double = 0.0
                for (k in start..s.length - 1) {
                    if (s[k] != 'y' && s[k] != '=') {
                        heSoCoM += s[k]
                    }
                    result += s[k]
                }
                if (haveC(heSoCoM, 'm') == true) {
                    var temp = heSoCoM
                    if (temp[0] == '-' && temp[1] == '(') {
                        lessThan0 = true
                        temp = temp.replace("-(", "")
                        temp = temp.replace(")", "")
                    }
                    if (temp[0] == '(') {
                        temp = temp.replace("(", "")
                        temp = temp.replace(")", "")
                    }
                    if (temp[0] == '+' && temp[1] == '(') {
                        lessThan0 = true
                        temp = temp.replace("+(", "")
                        temp = temp.replace(")", "")
                    }
                    var heSoCoMExp = listMonoExptoAddExp(nonMLatexToFormalExpression(temp, 'm'))
                    giaTriHeSo = heSoCoMExp.evalute(m)
                    if (lessThan0) {
                        giaTriHeSo = -giaTriHeSo
                    }
                    if (giaTriHeSo == 0.0) {
                        result = result.replace(heSoCoM, "")
                    } else if (giaTriHeSo > 0.0) {
                        result = result.replace(heSoCoM, "+" + giaTriHeSo)
                    } else {
                        result = result.replace(heSoCoM, "" + giaTriHeSo)
                    }
                }
            }
            return result
        }

        public fun listMonoExptoAddExp(_listMonoExp: MutableList<FormalExpression>): FormalExpression {
            var result = _listMonoExp[0]
            for (i in 1.._listMonoExp.size - 1) {
                result = AddExp(result, _listMonoExp[i])
            }
            return result
        }

        public fun listMonoExpDerive(_listMonoExp: MutableList<FormalExpression>): MutableList<FormalExpression> {
            var rs = arrayListOf<FormalExpression>()
            for (i in 0.._listMonoExp.size - 1) {
                if (_listMonoExp[i].derive().expToString() != "") {
                    rs.add(_listMonoExp[i].derive())
                }
            }
            return rs
        }

        public fun trimToKaTeX(s: String?): String {
            var rs = ""
            if (s != null) {
                for (i in 0..s.length - 1) {
                    if (s[i] != '{' && s[i] != '}') {
                        rs += s[i]
                    }
                }
            }
            rs = rs.replace("+-", "-");
            rs = rs.replace("=+", "=");
            return "\\(" + rs + "\\)"
        }

        public fun nonMLatexToFormalExpression(s: String, charOfx: Char): MutableList<FormalExpression> {
            // chuyen tu bieu thuc k co tham so m dang latex thanh bieu thuc
            var listMonomialExp: MutableList<FormalExpression> = ArrayList<FormalExpression>()
            // chua cac don thuc trong bieu thuc, thuc hien viet thanh bieu thuc

            // ham bac 2, bac 3, hoac trung phuong
            var indxofX = indexOfX(s, charOfx)
            // 0 2 9 15
            var numofindx = indxofX.size
            // 4
            var start: Int = indxofX[0]
            for (i in 0..numofindx - 2) {
                var stringofa: String = ""
                var stringofn: String = ""
                var a: Double = 0.0
                var n: Int = 0
                var end: Int = indxofX[i + 1] - 1
                for (j in start..end) {
                    if (s[j] != 'y' && s[j] != '=') {
                        stringofa += s[j]
                    }
                }
                if (end + 2 < s.length) {
                    if (s[end + 2] == '^') {
                        stringofn += s[end + 4];
                        // chi dung trong truong hop so mu la so nguyen
                        // trong pham vi bai tap ham so de dai hoc thi dung
                        start = end + 6
                    } else {
                        stringofn += 1
                        start = end + 2
                    }
                } else {
                    stringofn += 1
                    start = end + 2
                }
                if (stringofa == "" || stringofa == "+") {
                    a = 1.0
                } else if (stringofa == "-") {
                    a = -1.0
                } else {
                    a = stringofa.toDouble()
                }
                n = stringofn.toInt()
                var monomialItem: FormalExpression = MonomialExp.creatInstance(a, n)
                listMonomialExp.add(monomialItem)
            }
            if (start < s.length - 1) {
                // co hang so trong bieu thuc
                var stringofa: String = ""
                var a: Double = 0.0
                var n: Int = 0
                for (k in start..s.length - 1) {
                    if (s[k] != 'y' && s[k] != '=' && s[k] != charOfx) {
                        stringofa += s[k]
                    }
                }
                if (stringofa == "" || stringofa == "+") {
                    a = 1.0
                } else if (stringofa == "-") {
                    a = -1.0
                } else {
                    a = stringofa.toDouble()
                }
                var monomialItem: FormalExpression = MonomialExp.creatInstance(a, n)
                listMonomialExp.add(monomialItem)
            }
            return listMonomialExp
        }

        public fun getMaxn(s: String): Int {
            var indxofX = indexOfX(s, 'x')
            var start = indxofX[0];
            var end = indxofX[1] - 1
            var stringofn = ""
            if (end + 2 < s.length) {
                if (s[end + 2] == '^') {
                    stringofn += s[end + 4];
                    // chi dung trong truong hop so mu la so nguyen
                    // trong pham vi bai tap ham so de dai hoc thi dung
                    start = end + 6
                } else {
                    stringofn += 1
                    start = end + 2
                }
            } else {
                stringofn += 1
                start = end + 2
            }
            var rs = stringofn.toInt()
            return rs
        }

        public fun solverLevel1Equation(a: Double, b: Double): Double {
            var rs = -b / a;
            if (rs == -0.0) {
                rs = 0.0
            }
            return Math.round(rs * 1000.0) / 1000.0
        }

        public fun solverLevel2Equation(a: Double, b: Double, c: Double): List<Double> {
            var rs = arrayListOf<Double>()
            var delta = b * b - 4 * a * c;
            if (delta == 0.0) {
                var x = -b / (2 * a)
                if (x == -0.0) {
                    x = 0.0
                }
                val x_round=Math.round(x * 1000.0) / 1000.0
                rs.add(x_round)
            } else {
                var x1 = (-b - sqrt(delta)) / (2 * a);
                var x2 = (-b + sqrt(delta)) / (2 * a);
                if (x1 == -0.0) {
                    x1 = 0.0
                }
                if (x2 == -0.0) {
                    x2 = 0.0
                }
                val x1_round=Math.round(x1 * 1000.0) / 1000.0
                val x2_round=Math.round(x2 * 1000.0) / 1000.0
                rs.add(x1_round)
                rs.add(x2_round)
            }
            return rs;
        }

        public fun calculateY(x: Double, s: String): Double {
            var listFmlExp = nonMLatexToFormalExpression(s, 'x')
            var expValue = listMonoExptoAddExp(listFmlExp).evalute(x)
            return expValue
        }

        // Test ham tra ve cac diem cuc dai cuc tieu, loi lib
        public fun getListPoint(s: String): List<Double> {
            var rs = arrayListOf<Double>()
            var listMonoExp = nonMLatexToFormalExpression(s, 'x')
            var listExpDerive = listMonoExpDerive(listMonoExp) // List chưa cac don thuc sau khi dao ham
            var yDerive = listMonoExptoAddExp(listExpDerive).expToString() // String ham so sau khi dao ham
            if (getMaxn(s) == 2) {
                var a = 0.0;
                var b = 0.0;
                if (listExpDerive.size == 2) {
                    a = listExpDerive[0].evalute(1.0)
                    b = listExpDerive[1].evalute(0.0)
                } else if (listExpDerive.size == 1) {
                    a = listExpDerive[0].evalute(1.0)
                }
                if (a != 0.0) {
                    val x = solverLevel1Equation(a, b);
                    rs.add(x)
                }
            } else if (getMaxn(s) == 3) {
                var a = 0.0
                var b = 0.0
                var c = 0.0
                if (listExpDerive.size == 3) {
                    a = listExpDerive[0].evalute(1.0)
                    b = listExpDerive[1].evalute(1.0)
                    c = listExpDerive[2].evalute(0.0)
                } else if (listExpDerive.size == 2) {
                    if (listMonoExpDerive(listExpDerive).size == 1) {
                        a = listExpDerive[0].evalute(1.0)
                        b = 0.0
                        c = listExpDerive[1].evalute(0.0)
                    } else if (listMonoExpDerive(listExpDerive).size == 2) {
                        a = listExpDerive[0].evalute(1.0)
                        b = listExpDerive[1].evalute(1.0)
                        c = 0.0
                    }
                } else {
                    a = listExpDerive[0].evalute(1.0)
                    b = 0.0
                    c = 0.0
                }
                var delta = b * b - 4 * a * c
                if (delta >= 0) {
                    val cur_rs= solverLevel2Equation(a,b,c)
                    for(i in 0..cur_rs.size-1){
                        rs.add(cur_rs[i])
                    }
                }
            } else if (getMaxn(s) == 4) {
                var a = 0.0
                var c = 0.0
                a = listExpDerive[0].evalute(1.0)
                c = listExpDerive[1].evalute(1.0)
                var delta = -4 * a * c;
                rs.add(0.0)
                if(delta>=0){
                    val cur_rs= solverLevel2Equation(a,0.0,c)
                    for(i in 0..cur_rs.size-1){
                        rs.add(cur_rs[i])
                    }
                }
            }
            return rs
        }
    }
}