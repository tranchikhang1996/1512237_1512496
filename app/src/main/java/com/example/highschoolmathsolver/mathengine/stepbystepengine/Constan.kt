package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import com.example.highschoolmathsolver.util.MathUtils

import com.example.highschoolmathsolver.util.MathUtils.Companion.listMonoExpDerive
import com.example.highschoolmathsolver.util.MathUtils.Companion.listMonoExptoAddExp
import com.example.highschoolmathsolver.util.MathUtils.Companion.nonMLatexToFormalExpression
import com.example.highschoolmathsolver.util.MathUtils.Companion.solverLevel1Equation
import com.example.highschoolmathsolver.util.MathUtils.Companion.solverLevel2Equation
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToKaTeX

class Constan(_latexExpression: String) : MathType(_latexExpression) {
    override public fun solution(): List<String> {
        if(MathUtils.haveC(latexExpression, 'm')) {
            return arrayListOf("${SolutionAdapter.INPUT_M}$latexExpression", "${SolutionAdapter.GRAPH}$latexExpression")
        }
        // giả sử input : y=(2m-1)x^{3}-2x^ { 2 } + mx + 1
        // Doi lay gia tri m tu View
        var listStrResult = arrayListOf<String>()
        var b0 = "<h3>- Theo đề ta có hàm số: "+ trimToKaTeX(latexExpression)+"<br>"
        // *** Phần này làm sao cho biểu thức hiển thị dưới dạng mathview
        // lúc này biểu thức latexNonM là : y=x^{3}-2x^{2}+x+1, hiển thị dưới dạng mathview
        var listMonoExp = nonMLatexToFormalExpression(latexExpression, 'x')
        // Hàm này fmlExpNonM là biểu diễn của hàm số dưới dạng Expression tự định nghĩa, có thể thao tác đạo hàm, từ đó giải
        // Chuyển sang dạng string
        var ExpNonM = MathUtils.listMonoExptoAddExp(listMonoExp).expToString()
        //listStrResult.add(b03test)
        var positiveFx = true
        if (listMonoExp[0].evalute(1.0)<0) {
            positiveFx = false
        }
        b0+="- Tập xác định : \\(D=R\\) <br></h3>"
        listStrResult.add(b0)
        var b1=""

        // Xác định bậc của hàm số
        var listExpDerive=MathUtils.listMonoExpDerive(listMonoExp) // List chưa cac don thuc sau khi dao ham
        var yDerive=MathUtils.listMonoExptoAddExp(listExpDerive).expToString() // String ham so sau khi dao ham
        var b2="<h3>"
        var b3="<h3>- Sự biến thiên của hàm số :<br>"
        if (MathUtils.getMaxn(latexExpression) == 1) {
            // Hàm bậc 1 nên đạo hàm là hằng số
            if (positiveFx) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
                b3+="Hàm số tăng trong \\R"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
                b3+="Hàm số giảm trong \\R"
            }
            listStrResult.add(b1)
            b2 += "Đạo hàm:<br>" + trimToKaTeX("y'="+yDerive)+"<br>"
        }
        else if (MathUtils.getMaxn(latexExpression) == 2) {
            // Hàm bậc 2
            if (positiveFx) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
            }
            listStrResult.add(b1)
            var x=0.0
            b2 += "Đạo hàm:<br>" + trimToKaTeX("y'="+yDerive)+"<br>"
            b2 +="\\(y'=0\\)<br>\\(<=>\\)"+ trimToKaTeX(yDerive+"=0")+"<br>"
            var a=0.0;
            var b=0.0;
            if(listExpDerive.size==2){
                a=listExpDerive[0].evalute(1.0)
                b=listExpDerive[1].evalute(0.0)
            }else if(listExpDerive.size==1){
                a=listExpDerive[0].evalute(1.0)
            }
            if(a==0.0){
                b2 +="Phương trình vô nghiệm"
            }else{
                x=solverLevel1Equation(a,b);
                b2 +="\\(<=>x="+String.format("%.3f", x)+"\\)<br>"
                if(positiveFx){
                    b2 +="Điểm cực tiểu: \\((x="+String.format("%.3f", x)+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(x))+")\\)<br>"
                    b3+="Hàm số nghịch biến trong $$(-\\infty;"+x+")$$ Hàm số đồng biến trong $$("+x+";\\infty)$$"
                }else{
                    b2 +="Điểm cực đại: \\((x="+String.format("%.3f", x)+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(x))+")\\)<br>"
                    b3+="Hàm số đồng biến trong $$(-\\infty;"+x+")$$ Hàm số nghịch biến trong $$("+x+";\\infty)$$"
                }
            }
        }
        else if (MathUtils.getMaxn(latexExpression) == 3) {
            // Hàm bậc 3
            if (positiveFx) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
            }
            listStrResult.add(b1)
            var valueOfx : List<Double> = arrayListOf()
            b2 += "Đạo hàm:<br> " + trimToKaTeX("y'="+yDerive)+"<br>"
            b2 +="\\(y'=0\\)<br>\\(<=>\\)"+ trimToKaTeX(yDerive+"=0")+"<br>"
            var a=0.0
            var b=0.0
            var c=0.0
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
                b2 +="Phương trình vô nghiệm<br>"
                b2 +="Hàm số không có cực đại, cực tiểu<br>"
                if(positiveFx){
                    b3+="Hàm số đồng biến trong $$(-\\infty;+\\infty)"
                }else{
                    b3+="Hàm số nghịch biến trong $$(-\\infty;+\\infty)"
                }
            }else{
                valueOfx=solverLevel2Equation(a,b,c)
                if(valueOfx.size==1){
                    b2 +="\\(<=>\\)Phương trình có nghiệm kép : \\(x="+String.format("%.3f", valueOfx[0])+"\\)<br>"
                    b2 +="Đồ thị hàm số có cực đại và cực tiểu tại : (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")<br>"
                    if(positiveFx){
                        b3+="Hàm số đồng biến trong $$(-\\infty;+\\infty)"
                    }else{
                        b3+="Hàm số nghịch biến trong $$(-\\infty;+\\infty)"
                    }

                }
                else if(valueOfx.size==2){
                    b2 +="\\(<=>x="+String.format("%.3f", valueOfx[0])+"; x="+String.format("%.3f", valueOfx[1])+"\\)<br>"
                    if(positiveFx){
                        b2 +="Điểm cực đại:\\( (x="+String.format("%.3f", valueOfx[0])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+") \\)<br>"
                        b2 +="Điểm cực tiểu:\\( (x="+String.format("%.3f", valueOfx[1])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+") \\)<br>"
                        b3+="Hàm số đồng biến trong $$(-\\infty;"+valueOfx[0]+")$$ và $$("+valueOfx[1]+";+\\infty)$$ " +
                                "Hàm số nghịch biến trong $$("+valueOfx[0]+";"+valueOfx[1]+")$$"
                    }else{
                        b2 +="Điểm cực tiểu:\\( (x="+String.format("%.3f", valueOfx[0])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+") \\)<br>"
                        b2 +="Điểm cực đại:\\( (x="+String.format("%.3f", valueOfx[1])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+") \\)<br>"
                        b3+="Hàm số nghịch biến trong $$(-\\infty;"+valueOfx[0]+")$$ và $$("+valueOfx[1]+";+\\infty)$$ " +
                                "Hàm số đồng biến trong $$("+valueOfx[0]+";"+valueOfx[1]+")$$"
                    }
                }
            }
        }
        else if (MathUtils.getMaxn(latexExpression) == 4) {
            if (positiveFx) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
            }
            listStrResult.add(b1)
            b2 += "Đạo hàm:<br>" + trimToKaTeX("y'="+yDerive)+"<br>"
            b2 +="\\(y'=0\\)<br>\\(<=>\\)"+ trimToKaTeX(yDerive+"=0")+"<br>"
            var a=0.0
            var c=0.0
            if(listExpDerive.size==2){
                a=listExpDerive[0].evalute(1.0)
                c=listExpDerive[1].evalute(1.0)
            }
            else{
                a=listExpDerive[0].evalute(1.0)
                c=0.0
            }
            var delta=-4*a*c;
            var valueOfx : List<Double> = arrayListOf()
            if(delta<=0.0){
                b2 +="\\(<=>x=0\\)<br>"
                if(positiveFx){
                    b2 +="Điểm cực tiểu:\\( (x=0,000; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                    b3+="Hàm số nghịch biến trong $$(-\\infty;0,000)$$"+
                            "Hàm số đồng biến trong $$(0,000;+\\infty)$$"
                }else{
                    b2 +="Điểm cực đại:\\( (x=0,000; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                    b3+="Hàm số đồng biến trong $$(-\\infty;0,000)$$"+
                            "Hàm số nghịch biến trong $$(0,000;+\\infty)$$"
                }
            }else{
                valueOfx=solverLevel2Equation(a,0.0,c);
                b2+="\\(<=>x=0; x="+String.format("%.3f", valueOfx[0])+"\\);<br>"+"\\( x="+String.format("%.3f", valueOfx[1])+"\\)<br>";
                if(positiveFx){
                    b2 +="Điểm cực tiểu: \\( (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")\\) và \\((x="+
                            String.format("%.3f", valueOfx[1])+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+")\\)<br>"
                    b2 +="Điểm cực đại: \\( (x=0,000; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                    b3+="Hàm số nghịch biến trong $$(-\\infty;"+valueOfx[0]+")$$ và $$(0,000;"+valueOfx[1]+")$$" +
                            "Hàm số đồng biến trong $$("+valueOfx[0]+";0,000)$$ và $$("+valueOfx[1]+";+\\infty)$$ "
                }else{
                    b2 +="Điểm cực đại: \\( (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")\\) và \\((x="+
                            String.format("%.3f", valueOfx[1])+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+")\\)<br>"
                    b2 +="Điểm cực tiểu: \\( (x=0,000; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                    b3+="Hàm số đồng biến trong $$(-\\infty;"+valueOfx[0]+")$$ và $$(0,000;"+valueOfx[1]+")$$" +
                            "Hàm số nghịch biến trong $$("+valueOfx[0]+";0,000)$$ và $$("+valueOfx[1]+";+\\infty)$$ "
                }
            }
        }
        listStrResult.add(b2+"</h3>")
        listStrResult.add(b3+"</h3>")
        var b4="STATIC_GRAPH_FOR_CONSTAN"+latexExpression
        listStrResult.add(b4)
        return listStrResult
    }
}