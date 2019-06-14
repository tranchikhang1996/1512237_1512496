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
    override fun solution(): List<String> {
        if(MathUtils.haveC(latexExpression, 'm')) {
            return arrayListOf("${SolutionAdapter.INPUT_M}$latexExpression", "${SolutionAdapter.GRAPH}$latexExpression")
        }
        // giả sử input : y=(2m-1)x^{3}-2x^ { 2 } + mx + 1
        // Doi lay gia tri m tu View
        val listStrResult = arrayListOf<String>()
        var b0 = "<h3>- Theo đề ta có hàm số: "+ trimToKaTeX(latexExpression)+"<br>"
        // *** Phần này làm sao cho biểu thức hiển thị dưới dạng mathview
        // lúc này biểu thức latexNonM là : y=x^{3}-2x^{2}+x+1, hiển thị dưới dạng mathview
        val listMonoExp = nonMLatexToFormalExpression(latexExpression, 'x')
        // Hàm này fmlExpNonM là biểu diễn của hàm số dưới dạng Expression tự định nghĩa, có thể thao tác đạo hàm, từ đó giải
        // Chuyển sang dạng string
        val expNonM = MathUtils.listMonoExptoAddExp(listMonoExp).expToString()
        //listStrResult.add(b03test)
        var dongBien = true
        if (expNonM[0] == '-') {
            dongBien = false
        }
        b0+="- Tập xác định : \\(D=R\\) <br></h3>"
        listStrResult.add(b0)
        var b1=""

        // Xác định bậc của hàm số
        val listExpDerive= listMonoExpDerive(listMonoExp) // List chưa cac don thuc sau khi dao ham
        val yDerive=MathUtils.listMonoExptoAddExp(listExpDerive).expToString() // String ham so sau khi dao ham
        var b2="<h3>"
        if (MathUtils.getMaxn(latexExpression) == 1) {
            // Hàm bậc 1 nên đạo hàm là hằng số
            if (dongBien) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
            }
            listStrResult.add(b1)
            b2 += "Đạo hàm:<br>" + trimToKaTeX("y'="+yDerive)+"<br>"
        }
        else if (MathUtils.getMaxn(latexExpression) == 2) {
            // Hàm bậc 2
            if (dongBien) {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=+\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=+\\infty$$ </h3>"
            } else {
                b1+= "<h3>- Giới hạn của hàm số : $$\\lim_{x\\to+\\infty}y=-\\infty$$" +
                        "   $$\\lim_{x\\to-\\infty}y=-\\infty$$ </h3>"
            }
            listStrResult.add(b1)
            var x = 0.0
            b2 += "Đạo hàm:<br>" + trimToKaTeX("y'="+yDerive)+"<br>"
            b2 +="\\(y'=0\\)<br>\\(<=>\\)"+ trimToKaTeX(yDerive+"=0")+"<br>"
            var a=0.0
            var b=0.0
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
                if(dongBien){
                    b2 +="Điểm cực tiểu: \\((x="+String.format("%.3f", x)+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(x))+")\\)<br>"
                }else{
                    b2 +="Điểm cực đại: \\((x="+String.format("%.3f", x)+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(x))+")\\)<br>"
                }
            }
        }
        else if (MathUtils.getMaxn(latexExpression) == 3) {
            // Hàm bậc 3
            if (dongBien) {
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
            }else{
                valueOfx=solverLevel2Equation(a,b,c)
                if(valueOfx.size==1){
                    b2 +="\\(<=>\\)Phương trình có nghiệm kép : \\(x="+String.format("%.3f", valueOfx[0])+"\\)<br>"
                    b2 +="Đồ thị hàm số có cực đại và cực tiểu tại : (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")<br>"

                }
                else if(valueOfx.size==2){
                    b2 +="\\(<=>x="+String.format("%.3f", valueOfx[0])+"; x="+String.format("%.3f", valueOfx[1])+"\\)<br>"
                    if(dongBien){
                        b2 +="Điểm cực đại:\\( (x="+String.format("%.3f", valueOfx[0])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+") \\)<br>"
                        b2 +="Điểm cực tiểu:\\( (x="+String.format("%.3f", valueOfx[1])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+") \\)<br>"
                    }else{
                        b2 +="Điểm cực tiểu:\\( (x="+String.format("%.3f", valueOfx[0])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+") \\)<br>"
                        b2 +="Điểm cực đại:\\( (x="+String.format("%.3f", valueOfx[1])+
                                "; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+") \\)<br>"
                    }
                }
            }
        }
        else if (MathUtils.getMaxn(latexExpression) == 4) {
            if (dongBien) {
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
            a=listExpDerive[0].evalute(1.0)
            c=listExpDerive[1].evalute(1.0)
            var delta=-4*a*c;
            var valueOfx : List<Double> = arrayListOf()
            if(delta<0.0){
                b2 +="\\(<=>x=0\\)<br>"
                if(dongBien){
                    b2 +="Điểm cực tiểu:\\( (x=0,000; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                }else{
                    b2 +="Điểm cực đại:\\( (x=0,000; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                }
            }else{
                valueOfx=solverLevel2Equation(a,0.0,c);
                b2+="\\(<=>x=0; x="+String.format("%.3f", valueOfx[0])+"\\);<br>"+"\\( x="+String.format("%.3f", valueOfx[1])+"\\)<br>";
                if(dongBien){
                    b2 +="Điểm cực tiểu: \\( (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")\\) và \\((x="+
                            String.format("%.3f", valueOfx[1])+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+")\\)<br>"
                    b2 +="Điểm cực đại: \\( (x=0,000; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                }else{
                    b2 +="Điểm cực đại: \\( (x="+String.format("%.3f", valueOfx[0])+"; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[0]))+")\\) và \\((x="+
                            String.format("%.3f", valueOfx[1])+"; y="+String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(valueOfx[1]))+")\\)<br>"
                    b2 +="Điểm cực tiểu: \\( (x=0,000; y="+
                            String.format("%.3f", listMonoExptoAddExp(listMonoExp).evalute(0.0))+")\\)<br>"
                }
            }
        }
        listStrResult.add(b2+"</h3>")
        //var b3="DRAW_GRAPH"+latexExpression
        //listStrResult.add(b3)
        return listStrResult
    }
}