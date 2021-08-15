package main.java.utils;

import com.intellij.openapi.project.Project;

import org.apache.http.util.TextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CommonUtil {

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     *
     * @return
     */
    public static String getPackageName(Project project) {
        String package_name = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/app/src/main/AndroidManifest.xml");

            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                package_name = element.getAttribute("package");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return package_name;
    }

    /**
     * 服务端契约中的类型跟我们用的类型有差别，这里修正一下
     * bool -> boolean
     * string -> String
     * decimal -> double
     * list -> List
     */
    public static String modifyClassType(List<String> strings) {
        if (strings.size() > 1) {
            String type = strings.get(1);
            if ("boolean".contains(type)) {
                return "boolean";
            } else if ("decimal".equalsIgnoreCase(type)) {
                return "double";
            } else if (type.contains("string")) {
                return type.replace("string", "String");
            } else if ("list".equalsIgnoreCase(type)) {
                return "List";
            } else {
                return type;
            }
        }
        return "";
    }

    public static void appendAnnotation(List<String> strings, StringBuilder sb) {
        if (strings.size() == 3) {
            sb.append("/**\n\t*\t").append(strings.get(2)).append("\n*/\n");
        }
    }

    public static void appendField(List<String> strings, StringBuilder sb) {
        if (strings.size() == 0) {
            return;
        }
        sb.append(" ").append(strings.get(0)).append(";");
    }

    public static String appendFieldType(List<String> strings, StringBuilder sb) {
        return modifyClassType(strings);
    }

    public static void appendMemberType(String memberType, StringBuilder sb) {
        if (memberType == null) {
            memberType = "public static";
        }
        sb.append(memberType).append(" ");
    }

    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    public static String toUpperCase4Index(String string) {
        char[] methodName = string.toCharArray();
        methodName[0] = toUpperCase(methodName[0]);
        return String.valueOf(methodName);
    }

    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    public static char toUpperCase(char chars) {
        if (97 <= chars && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    public static String processColor(String color) {

        if (color.contains("#")) {
            color = String.format("[UIColor hex:@\"%s\"]", color);
        } else if ("title".equals(color)) {
            color = "COLOR_TITLE";
        } else if ("sub".equals(color)) {
            color = "COLOR_SUBTITLE";
        } else if ("pri".equals(color)) {
            color = "COLOR_PRIMARY";
        } else {
            color = String.format("UIColor.%sColor", color);
        }

        return color;
    }

    public static String processFont(String font) {
        if (font.indexOf("b") > 0) {
            font = String.format("BOLD_FONT(%s)", font.substring(1));
        } else {
            font = String.format("FONT(%s)", font);
        }
        return font;
    }

    public static String processImage(String image) {
        if (TextUtils.isBlank(image)) {
            image = "<#(NSString *)img#>";
        } else {
            image = String.format("@\"%s\"", image);
        }

        return image;
    }

    public static String processText(String string) {
        String text = String.format("@\"%s\"", string);
        return text;
    }


    public static int getIndexOfMethod(String input, String methodStrPattern) {
        Pattern pattern = Pattern.compile(methodStrPattern, Pattern.DOTALL);

        Matcher matcher = pattern.matcher(input);
        int end = 0;
        while (matcher.find()) {
            end = matcher.toMatchResult().end();
            System.out.println("no-end" + end);
            break;
        }
        pattern = Pattern.compile("-\\s+\\(", Pattern.DOTALL);
        String subinput = input.substring(end);

        matcher = pattern.matcher(subinput);

        int newEnd = 0;
        while (matcher.find()) {
            newEnd = matcher.toMatchResult().end();
            System.out.println("no-end" + newEnd);
            break;
        }
        int index;
        if (newEnd == 0) {
            index = input.lastIndexOf("}");
        } else {
            String substring = input.substring(0, end + newEnd);
            index = substring.lastIndexOf("}");
        }
        return index;
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
}
