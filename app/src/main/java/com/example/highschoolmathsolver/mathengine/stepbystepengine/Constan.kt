package com.example.highschoolmathsolver.mathengine.stepbystepengine

import android.graphics.Color
import com.example.highschoolmathsolver.mathengine.expression.AddExp
import com.example.highschoolmathsolver.mathengine.expression.FormalExpression
import com.example.highschoolmathsolver.mathengine.expression.MonomialExp
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlin.math.sqrt

class Constan(_latexExpression: String,_m: Double) : MathType(_latexExpression) {
    // String input : y=-(2m-1)x^{3}+2x^{2}-x-m+1
    // tim cac vi tri chua bien x trong bieu thuc
    private var m:Double=_m
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

    // xem String co ki tu c hay khong
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

    // Thay the gia tri m vao, khu gia tri m
    public fun removeM(): String {
        // ham nay tra ve chuoi khong co tham so m voi gia tri m  duoc nguoi dung nhap tu truoc
        var m=this.m
        var s = this.latexExpression
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
                var heSoCoMExp = listMonoExptoAddExp(nonParaLatexToFormalExpression(temp, 'm'))
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
                var heSoCoMExp = listMonoExptoAddExp(nonParaLatexToFormalExpression(temp, 'm'))
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

    // Xu li sau khi khu tham so m ( cho m= 1 gia tri nao do)
    // ta duoc input : y=x^{3}-2x^ { 2 } + x + 1
    //                 01234567891011121314151617
    // output là mot bieu thuc dang FormalExpression, co the
    // tinh dao ham,giai dao ham, tinh gia tri bieu thuc
    // AddExp(AddExp(Monomial(1,3),Monomial(-2,2)),
    // test OK
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

    public fun trimToMathView(s: String): String {
        return ""
    }

    // bieu thuc dang latex da khu m sang dang cac bieu thuc dang FormalExpression
    // test OK
    public fun nonParaLatexToFormalExpression(s: String, charOfx: Char): MutableList<FormalExpression> {
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
            var n: Double = 0.0
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
            n = stringofn.toDouble()
            var monomialItem: FormalExpression = MonomialExp.creatInstance(a, n)
            listMonomialExp.add(monomialItem)
        }
        if (start < s.length - 1) {
            // co hang so trong bieu thuc
            var stringofa: String = ""
            var a: Double = 0.0
            var n: Double = 0.0
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

    public fun getMaxn(): Double {
        var s = this.latexExpression
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
        var rs = stringofn.toDouble()
        return rs
    }

    public fun drawGraph(graph: GraphView) {
        var m=this.m
        var series: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesUtil: PointsGraphSeries<DataPoint> = PointsGraphSeries()
        var seriesmaxX: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesmaxY: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesminX: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesminY: LineGraphSeries<DataPoint> = LineGraphSeries()
        graph.getViewport().setYAxisBoundsManual(true)
        graph.getViewport().setXAxisBoundsManual(true)
        graph.getViewport().setMinY(-7.0)
        graph.getViewport().setMaxY(7.0)
        graph.getViewport().setMinX(-7.0)
        graph.getViewport().setMaxX(7.0)

        // Set up các thông số cho graph
        graph.getViewport().setScrollable(true)
       // graph.getViewport().set(true)

        // ------------------------------

        // Vẽ đồ thị
        var x = -25.1;
        var y = 0.0;
        for (i in 1..500) {
            x += 0.1;
            // đây là biểu thức sau khi khử m và chuyển sang dạng FormalExpression
            y = listMonoExptoAddExp(nonParaLatexToFormalExpression(removeM(), 'x')).evalute(x)
            if (x == 0.0) {
                seriesUtil.appendData(DataPoint(x, y), true, 500)
            }
            series.appendData(DataPoint(x, y), true, 1000)
        }
        //-------------

        //seriesUtil.appendData(DataPoint(0.0,0.0),true,500)
        seriesUtil.setShape(PointsGraphSeries.Shape.POINT)
        seriesUtil.setSize(10.0f)
        seriesUtil.setColor(Color.RED)

        // đồ thị
        graph.addSeries(series)
        // các giao điểm
        graph.addSeries(seriesUtil)
        //----------

        // Thêm 4 series max X, min X, max Y, min Y để tạo khung, fix lỗi bị lệch về với các hàm hướng về 1 phía
        var xminy = -30.0;
        var yminy = 0.0;
        for (i in 1..60) {
            xminy += 1;
            yminy = -100.0;
            seriesminY.appendData(DataPoint(xminy, yminy), false, 1000)
        }
        graph.addSeries(seriesminY)

        var xmaxy = -30.0;
        var ymaxy = 0.0;
        for (i in 1..60) {
            xmaxy += 1;
            ymaxy = 100.0;
            seriesmaxY.appendData(DataPoint(xmaxy, ymaxy), false, 1000)
        }
        graph.addSeries(seriesmaxY)

        var xminx = 0.0;
        var yminx = -30.0;
        for (i in 1..60) {
            xminx = -100.0;
            yminx += 1;
            seriesminX.appendData(DataPoint(xminx, yminx), false, 1000)
        }
        graph.addSeries(seriesminX)

        var xmaxx = 0.0;
        var ymaxx = -30.0;
        for (i in 1..60) {
            xmaxx += 100.0;
            ymaxx += 1;
            seriesmaxX.appendData(DataPoint(xmaxx, ymaxx), false, 1000)
        }
        graph.addSeries(seriesmaxX)
        //-------------------------------------------
    }

    public fun solverLevel1Equation(a:Double,b:Double):Double{
        var rs=-b/a;
        if(rs==-0.0){
            rs=0.0
        }
        return rs
    }
    public fun solverLevel2Equation(a:Double,b:Double,c:Double):List<Double>{
        var rs= arrayListOf<Double>()
        var delta=b*b-4*a*c;
        if(delta==0.0){
            var x=-b/(2*a)
            if(x==-0.0){
                x=0.0
            }
            rs.add(x)
        }else{
            var x1=(-b- sqrt(delta))/(2*a);
            var x2=(-b+ sqrt(delta))/(2*a);
            if(x1==-0.0){
                x1=0.0
            }
            if(x2==-0.0){
                x2=0.0
            }
            rs.add(x1)
            rs.add(x2)
        }
        return rs;
    }
    override public fun solution(): List<String> {
        // giả sử input : y=(2m-1)x^{3}-2x^ { 2 } + mx + 1
        var m = this.m
        var listStrResult = arrayListOf<String>()
        var b01 = "-Theo đề ta có biểu thức: " + this.latexExpression
        // *** Phần này làm sao cho biểu thức hiển thị dưới dạng mathview
        var latexNonM = removeM()
        var b02 = "-Với m=" + m + " ta có hàm số sau " + latexNonM
        var b0=b01+ "\n" + b02
        listStrResult.add(b0)
        // lúc này biểu thức latexNonM là : y=x^{3}-2x^{2}+x+1, hiển thị dưới dạng mathview
        var listMonoExp = nonParaLatexToFormalExpression(latexNonM, 'x')
        // Hàm này fmlExpNonM là biểu diễn của hàm số dưới dạng Expression tự định nghĩa, có thể thao tác đạo hàm, từ đó giải
        // Chuyển sang dạng string
        var ExpNonM = listMonoExptoAddExp(listMonoExp).expToString()
        //var b03test="test hien thi exp: "+ExpNonM
        //listStrResult.add(b03test)
        var dongBien = true
        if (ExpNonM[0] == '-') {
            dongBien = false
        }
        var b04 = "Tập xác định : D=R"
        listStrResult.add(b04)
        // Xác định bậc của hàm số
        var listExpDerive=listMonoExpDerive(listMonoExp) // List chưa cac don thuc sau khi dao ham
        var yDerive=listMonoExptoAddExp(listExpDerive).expToString() // String ham so sau khi dao ham
        if (getMaxn() == 1.0) {
            // Hàm bậc 1 nên đạo hàm là hằng số
            var b11 = "Đạo hàm :\ny'=" + yDerive
            var b12 = ""
            if (dongBien) {
                b12 += "Giới hạn của hàm số : \nlim y(x->+vô cùng)=+vô cùng\nlim y(x->-vô cùng)=-vô cùng"
            } else {
                b12 += "Giới hạn của hàm số : \nlim y{x->+vô cùng}=-vô cùng\nlim y(x->-vô cùng)=+vô cùng"
            }
            var b13 = "Bảng biến thiên :"
            var b14 = "Đồ thị hàm số :"
            listStrResult.add(b11)
            listStrResult.add(b12)
            listStrResult.add(b13)
            listStrResult.add(b14)
        }
        else if (getMaxn() == 2.0) {
            // Hàm bậc 2
            var x=0.0
            var b21 = "-Đạo hàm :\n y'=" + yDerive
            listStrResult.add(b21)
            var b211="-y'=0\n<=>"+yDerive+"=0"
            var a=0.0;
            var b=0.0;
            var b212="-"
            if(listExpDerive.size==2){
                a=listExpDerive[0].evalute(1.0)
                b=listExpDerive[1].evalute(0.0)
            }else if(listExpDerive.size==1){
                a=listExpDerive[0].evalute(1.0)
            }
            if(a==0.0){
                b211+="\nPhương trình vô nghiệm"
                listStrResult.add(b211)
            }else{
                x=solverLevel1Equation(a,b);
                b211+="\n<=>x="+x
                listStrResult.add(b211)
                if(dongBien){
                    b212+="Giá trị hàm số đạt cực tiểu tại x="+x+
                            " \nKhi đó y="+listMonoExptoAddExp(listMonoExp).evalute(x)
                }else{
                    b212+="Giá trị hàm số đạt cực đại tại x="+solverLevel1Equation(a,b)+
                            " \nKhi đó y="+listMonoExptoAddExp(listMonoExp).evalute(x)
                }
                listStrResult.add(b212)
            }
            var b22 = "-"
            if (dongBien) {
                b22 += "Giới hạn của hàm số : \nlim y(x->+vô cùng)=+vô cùng\nlim y(x->-vô cùng)=-vô cùng"
            } else {
                b22 += "Giới hạn của hàm số : \nlim y{x->+vô cùng}=-vô cùng\nlim y(x->-vô cùng)=+vô cùng"
            }
            var b23 = "-Bảng biến thiên :"
            var b24 = "-Đồ thị hàm số :"


            listStrResult.add(b22)
            listStrResult.add(b23)
            listStrResult.add(b24)
        }
        else if (getMaxn() == 3.0) {
            // Hàm bậc 3
            var valueOfx : List<Double> = arrayListOf()
            var b31 = "-Đạo hàm :\n y'=" + yDerive
            listStrResult.add(b31)
            var b311="-y'=0\n<=>"+yDerive+"=0"
            listStrResult.add(b311)
            var b32 = "-"
            var a=0.0
            var b=0.0
            var c=0.0
            var b312="-"
            if(listExpDerive.size==3){
                a=listExpDerive[0].evalute(1.0)
                b=listExpDerive[1].evalute(1.0)
                c=listExpDerive[2].evalute(0.0)
            }
            else if(listExpDerive.size==2){
                if(listMonoExpDerive(listExpDerive).size==1){
                    a=listExpDerive[0].evalute(1.0)
                    b=0.0
                    c=listExpDerive[1].evalute(0.0)
                }
                else if(listMonoExpDerive(listExpDerive).size==2){
                    a=listExpDerive[0].evalute(1.0)
                    b=listExpDerive[1].evalute(1.0)
                    c=0.0
                }
            }
            else{
                a=listExpDerive[0].evalute(1.0)
                b=0.0
                c=0.0
            }
            var delta=b*b-4*a*c
            if(delta<0.0){
                b312+="Phương trình vô nghiệm"
                b312+="\nHàm số không có cực đại, cực tiểu"
                listStrResult.add(b312)
            }else{
                valueOfx=solverLevel2Equation(a,b,c)
                if(valueOfx.size==1){
                    b312+="<=>Phương trình có nghiệm kép x="+valueOfx[0]
                    b312+="/nĐồ thị hàm số có cực đại và cực tiểu tại : (x="+valueOfx[0]+", y="+
                            listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0])+")"

                }
                else if(valueOfx.size==2){
                    b312+="<=>x="+valueOfx[0]+", x="+valueOfx[1]
                    if(dongBien){
                        b312+="\nĐiểm cực đại : (x="+valueOfx[0]+
                                ", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0])+")"
                        b312+="\nĐiểm cực tiểu : (x="+valueOfx[1]+
                                ", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1])+")"
                    }else{
                        b312+="\nĐiểm cực tiểu : (x="+valueOfx[0]+
                                ", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0])+")"
                        b312+="\nĐiểm cực đại : x="+valueOfx[1]+
                                ", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1])+")"
                    }
                }
                listStrResult.add(b312)
            }
            if (dongBien) {
                b32 += "Giới hạn của hàm số : \nlim y(x->+vô cùng)=+vô cùng\nlim y(x->-vô cùng)=-vô cùng"
            } else {
                b32 += "Giới hạn của hàm số : \nlim y{x->+vô cùng}=-vô cùng\nlim y(x->-vô cùng)=+vô cùng"
            }
            var b33 = "-Bảng biến thiên :"
            var b34 = "-Đồ thị hàm số :"

            listStrResult.add(b32)
            listStrResult.add(b33)
            listStrResult.add(b34)
        }
        else if (getMaxn() == 4.0) {
            var b41 = "-Đạo hàm :\n y'=" + yDerive
            listStrResult.add(b41)
            var b411="-y'=0\n<=>"+yDerive+"=0"
            listStrResult.add(b411)
            var a=0.0
            var c=0.0
            var b412=""
            a=listExpDerive[0].evalute(1.0)
            c=listExpDerive[1].evalute(1.0)
            var delta=-4*a*c;
            var valueOfx : List<Double> = arrayListOf()
            if(delta<0.0){
                b412+="<=>x=0"
                if(dongBien){
                    b412+="\nHàm số đạt cực tiểu tại x=0, y="+listMonoExptoAddExp(listMonoExp).evalute(0.0)
                }else{
                    b412+="\nHàm số đạt cực đại tại x=0, y="+listMonoExptoAddExp(listMonoExp).evalute(0.0)
                }
            }else{
                valueOfx=solverLevel2Equation(a,0.0,c);
                b412+="<=>x=0, x="+valueOfx[0]+", x="+valueOfx[1];
                if(dongBien){
                    b412+="\nHàm số đạt cực tiểu tại (x="+valueOfx[0]+", y="+
                            listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0])+") và (x="+
                            valueOfx[1]+", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1])+")"
                    b412+="\nHàm số đạt cực đại tại (x=0.0, y="+
                            listMonoExptoAddExp(listMonoExp).evalute(0.0)+")"
                }else{
                    b412+="\nHàm số đạt cực đại tại (x="+valueOfx[0]+", y="+
                            listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0])+") và (x="+
                            valueOfx[1]+", y="+listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1])+")"
                    b412+="\nHàm số đạt cực tiểu tại (x=0.0, y="+
                            listMonoExptoAddExp(listMonoExp).evalute(0.0)+")"
                }
            }
            listStrResult.add(b412)
            var b42 = "-"
            if (dongBien) {
                b42 += "Giới hạn của hàm số : \nlim y(x->+vô cùng)=+vô cùng\nlim y(x->-vô cùng)=-vô cùng"
            } else {
                b42 += "Giới hạn của hàm số : \nlim y{x->+vô cùng}=-vô cùng\nlim y(x->-vô cùng)=+vô cùng"
            }
            var b43 = "-Bảng biến thiên :"
            var b44 = "-Đồ thị hàm số :"

            listStrResult.add(b42)
            listStrResult.add(b43)
            listStrResult.add(b44)
        }
        return listStrResult
    }
}