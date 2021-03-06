package com.example.highschoolmathsolver.util

import com.example.highschoolmathsolver.mathengine.expression.AddExp
import com.example.highschoolmathsolver.mathengine.expression.FormalExpression
import com.example.highschoolmathsolver.mathengine.expression.MonomialExp
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function
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
        public fun indexOfX(s: String, charOfx: Char): ArrayList<Int> {
            var indxofX=ArrayList<Int>()
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
                    if(giaTriHeSo==-0.0){
                        giaTriHeSo=0.0
                    }
                    if (giaTriHeSo == 1.0 && start == 0) {
                        result = result.replace(heSoCoM, "")
                    } else if (giaTriHeSo == 1.0 && start != 0) {
                        result = result.replace(heSoCoM, "+")
                    } else if (giaTriHeSo == -1.0) {
                        result = result.replace(heSoCoM, "-")
                    } else if (giaTriHeSo >= 0.0) {
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
            // xét m ở phần hằng số
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

        public fun listMonoExptoAddExp(_listMonoExp: ArrayList<FormalExpression>): FormalExpression {
            var result = _listMonoExp[0]
            for (i in 1.._listMonoExp.size - 1) {
                result = AddExp(result, _listMonoExp[i])
            }
            return result
        }

        public fun listMonoExpDerive(_listMonoExp: ArrayList<FormalExpression>): ArrayList<FormalExpression> {
            var rs = ArrayList<FormalExpression>()
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
                var i = 0
                while (i < s.length) {
                    if ((s[i] == '^' && s[i + 1] == '{' && s[i - 1] != '}')) {
                        rs += s[i]
                        rs += s[i + 2]
                        i += 4;
                    } else {
                        rs += s[i]
                        i++
                    }
                }
            }
            rs = rs.replace("+-", "-");
            rs = rs.replace("=+", "=");
            rs = rs.replace("\\gt", ">");
            rs = rs.replace("\\lt", "<");
            return "\\(" + rs + "\\)"
        }

        public fun nonMLatexToFormalExpression(s: String, charOfx: Char): ArrayList<FormalExpression> {
            // chuyen tu bieu thuc k co tham so m dang latex thanh bieu thuc
            var listMonomialExp = ArrayList<FormalExpression>()
            // chua cac don thuc trong bieu thuc, thuc hien viet thanh bieu thuc

            // ham bac 2, bac 3, hoac trung phuong
            var indxofX = indexOfX(s, charOfx)
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

        public fun solverLevel2Equation(a: Double, b: Double, c: Double): ArrayList<Double> {
            var rs = arrayListOf<Double>()
            var delta = b * b - 4 * a * c;
            if (delta == 0.0) {
                var x = -b / (2 * a)
                if (x == -0.0) {
                    x = 0.0
                }
                val x_round = Math.round(x * 1000.0) / 1000.0
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
                val x1_round = Math.round(x1 * 1000.0) / 1000.0
                val x2_round = Math.round(x2 * 1000.0) / 1000.0
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

        // Ham tinh gia tri cua f(x) theo x
        // Truyen vao phuong trinh dang latex, va giá trị của x, sẽ có giá trị của y
        public fun calculateFx(x: Double, latexExpression: String): Double{
            val mxEquation= trimToMxParserFx(trimToKaTeX(latexExpression))
            var mxFunction=Function("f",mxEquation,"x");
            return mxFunction.calculate(x);
        }

        public fun trimToMxParserFx(katexExpression:String):String{
            var rs=katexExpression
            rs=rs.replace("\\(","")
            rs=rs.replace("\\)","")
            rs=rs.replace("x","*x")
            rs=rs.replace("-*","-")
            rs=rs.replace("+*","+")
            rs=rs.replace("\\sqrt","sqrt")
            rs=rs.replace("\\frac","")
            rs=rs.replace("}{","}/{")
            rs=rs.replace("{","(")
            rs=rs.replace("}",")")
            rs=rs.replace("(*","(")
            var finalRs=""
            var indexOfEqual=0;
            for(i in 0..rs.length-1){
                if(rs[i]=='='){
                    indexOfEqual=i
                    break;
                }
            }
            if(indexOfEqual==rs.length-2){
                if(rs[0]=='*'){
                    for(i in 1..rs.length-3){
                        finalRs+=rs[i]
                    }
                }else{
                    for(i in 0..rs.length-3){
                        finalRs+=rs[i]
                    }
                }
            }else{
                if(rs[0]=='*'){
                    for(i in 1..indexOfEqual-1){
                        finalRs+=rs[i]
                    }
                }else{
                    for(i in 0..indexOfEqual-1){
                        finalRs+=rs[i]
                    }
                }
                finalRs+="-("
                if(rs[indexOfEqual+1]=='*'){
                    for(i in indexOfEqual+2..rs.length-1){
                        finalRs+=rs[i]
                    }
                }else{
                    for(i in indexOfEqual+1..rs.length-1){
                        finalRs+=rs[i]
                    }
                }
                finalRs+=")"
            }
            return finalRs
        }

        public fun solveEquationMxParser(mxParserFx:String):ArrayList<Double>{
            var listRs=ArrayList<Double>()
            for(i in 0..200){
                val floorNum=(i-100)/10.toDouble()
                val ceilNum=floorNum+0.1
                val mxEquation= Expression("solve("+ mxParserFx+",x,"+floorNum+","+ceilNum+")");
                var rs=mxEquation.calculate()
                if(!rs.isNaN()){
                    rs=Math.round(rs*1000.0)/1000.0
                    if(listRs.size>0){
                        var isExist=false;
                        for(curItem in listRs)  {
                            if(rs==curItem){
                                isExist=true;
                                break;
                            }
                        }
                        if(isExist==false){
                            listRs.add(rs)
                        }
                    }else{
                        listRs.add(rs)
                    }
                }
            }
            return listRs
        }
    }
}