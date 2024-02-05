package com.itheima.test;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

//把word转成pdf
public class JacobDemo {

    public static void main(String[] args) {
        String source = "d://张三_合同.docx";
        String target = "d://张三_合同.pdf";
        ActiveXComponent app = null;
        try {
//        调用window中的office程序
            app = new ActiveXComponent("Word.application");
//        调用word时不显示窗口
            app.setProperty("Visible",false);
//        获取文档
            Dispatch docs = app.getProperty("Documents").toDispatch();
//            打开指定的文档
            Dispatch doc = Dispatch.call(docs,"Open", source).toDispatch();
//        调用文件的另存为功能  宏值 17
            Dispatch.call(doc,"SaveAs",target,17);
//        调用关闭
            Dispatch.call(doc,"Close");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            app.invoke("Quit");
        }
    }
}
