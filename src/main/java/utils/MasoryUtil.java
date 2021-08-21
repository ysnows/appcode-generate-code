package main.java.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.plaf.TextUI;


public class MasoryUtil {


//    public static void main(String[] args) {
//        String text="viewTop";
//        System.out.println(text.length() >= 2);
//        System.out.println(isNumeric(text) != 0);
//    }

    public static void main(String[] args) {
        var str = "t.viewTop.b.-12;t.viewTop;t.viewTop.b;l.t.viewTop.t.12;h.w.115";
//        str = "t.viewTop;";
//        str = "t.viewTop.b;";
//        str = "l.t.viewTop.t.12;";
//        str = "l.t.viewTop.12;";
//        str = "l.r.viewTop;";
//        str = "t.b.viewTop;";
//        str = "h.w.12;";
//        str = "y.viewTop.12;";
//        str = "l.t.cont.12;r.cont.r.12;w.h.170";
        str = "l.t.viewTop.r.12;l.r.viewTop.12;r.";


        var needCompletion = isNeedCompletion(str);

        System.out.println(needCompletion);
    }

    public static void moveCaretToMasoryLine(Editor editor, Document document, String name) {
        var masoryIndex = document.getText().indexOf("" + name + " mas_makeConstraints:");
        var lineNumber = document.getLineNumber(masoryIndex);
        var lineEndOffset = document.getLineEndOffset(lineNumber);

        int offset = lineEndOffset + 1;
        editor.getCaretModel().getCurrentCaret().moveToOffset(offset);
    }


    @NotNull
    public static String parseMasory(String str) {
        if (TextUtils.isBlank(str)) {
            return "";
        }
        var groupList = str.split(";");
        var builder = new StringBuilder();
        for (int j = 0; j < groupList.length; j++) {
            String group = groupList[j];
            var strList = group.split("\\.");
            String preText = "";
            for (int i = 0; i < strList.length; i++) {
                var text = strList[i];
                boolean mas = isViewText(preText);

                //如果是第一个
                if (i == 0) {
                    if (j == 0) {
                        builder.append("\t\tmake");
                    } else {
                        builder.append("\n\t\tmake");
                    }
                }

                String processedText = processText(text, preText, mas);
                if (isViewText(preText)) {
                    //前一个文字是view
                    if (isNumeric(text) == 0) {
                        //当前文字不是数字
                        processedText = processedText + ")";
                        builder.append(".").append(processedText);
                    } else {
                        //当前文字是数字
                        processedText = ")." + processedText;
                        builder.append(processedText);
                    }
                } else {
                    builder.append(".").append(processedText);
                }

                if (i == strList.length - 1) {
                    if (isViewText(text)) {
                        //如果view是最后一个
                        builder.append((");"));
                    } else {
                        builder.append((";"));
                    }
                }

                preText = text;
            }
        }
        return builder.toString();
    }

    private static boolean isViewText(String text) {
        return text.length() >= 2 && isNumeric(text) == 0;
    }


    private static String processText(String text, String preText, boolean mas) {
        var numeric = isNumeric(text);

        if (isViewText(text)) {
            text = getSuperViewTest(text);
            return text.equals("self") ? "equalTo(self" : "equalTo(self." + text + "";

        } else if (numeric != 0) {
            if (mas) {
                if (numeric == -1) {
                    return "offset(-kNum(" + text.replace("-", "") + "))";
                } else if (numeric == 1) {
                    return "offset(kNum(" + text + "))";
                }
            }

            if ("h".equals(preText) || "w".equals(preText)) {
                return "mas_equalTo(kNum(" + text + "))";
            } else {
                if (numeric == -1) {
                    return "offset(-kNum(" + text.replace("-", "") + "))";
                } else if (numeric == 1) {
                    return "offset(kNum(" + text + "))";
                }
            }
        }

        switch (text) {
            case "l":
                return mas ? "mas_left" : "left";
            case "r":
                return mas ? "mas_right" : "right";
            case "t":
                return mas ? "mas_top" : "top";
            case "b":
                return mas ? "mas_bottom" : "bottom";
            case "h":
                return mas ? "mas_height" : "height";
            case "w":
                return mas ? "mas_width" : "width";
            case "y":
                return mas ? "mas_centerY" : "centerY";
            case "x":
                return mas ? "mas_centerX" : "centerX";
            case "c":
                return mas ? "mas_center" : "center";
            case "e":
                return mas ? "mas_edges" : "edges";
            default:
                return "";
        }
    }

    @NotNull
    public static String getSuperViewTest(String text) {
        switch (text) {
            case "self":
                text = "self";
                break;
            case "cont":
                text = "contentView";
                break;
            case "head":
                text = "headerView";
                break;
            case "main":
                text = "mainView";
                break;
            case "table":
                text = "tableView";
                break;
        }
        return text;
    }

    public static int isNumeric(String str) {
        var matches = Pattern.matches("-\\d+", str);
        if (matches) {
            return -1;
        }
        matches = Pattern.matches("\\d+", str);
        if (matches) {
            return 1;
        }
        return 0;
    }


    public static ArrayList<String> getPropertyList(String text) {
        var pattern = Pattern.compile("@property.*\\s\\*(\\w+);");
        var matcher = pattern.matcher(text);

        var list = new ArrayList<String>();
        while (true) {
            var b = matcher.find();
            if (!b) break;
            list.add(matcher.group(1));
        }

        return list;

    }

    public static boolean isNeedCompletion(String text) {
        return Pattern.matches("(.*;)?(\\w\\.)+", text);
//        return Pattern.matches("(\\w\\.)+", text);

    }

}
