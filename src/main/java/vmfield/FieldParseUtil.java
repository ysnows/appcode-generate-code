package main.java.vmfield;

import java.util.ArrayList;

public class FieldParseUtil {
    public static void main(String[] args) {
        var parse = parse("name:str;age:int;sex:bool;");
        System.out.println("hello");
    }

    public static ArrayList<String> parse(String text) {
        //name:str;age:int;sex:bool;
        var list = new ArrayList<String>();
        var arrField = text.split(";");

        var propertyBuilder = new StringBuilder();
        var observerBuilder = new StringBuilder();
        var bindBuilder = new StringBuilder();
        var importBuilder = new StringBuilder();
        for (String str : arrField) {
            var arrFieldInfo = str.split(":");
            var fieldName = arrFieldInfo[0];
            var fieldType = getFieldType(arrFieldInfo.length > 1 ? arrFieldInfo[1] : "str");
            var pureFieldType = getPureFieldType(arrFieldInfo.length > 1 ? arrFieldInfo[1] : "str");
            var propertyType = getPropertyType(arrFieldInfo.length > 1 ? arrFieldInfo[1] : "str");
            var bindView = arrFieldInfo.length > 2 ? arrFieldInfo[2] : "<#(NSString *)view#>";

            if (!isNormalField(arrFieldInfo.length > 1 ? arrFieldInfo[1] : "str")) {
                importBuilder.append("\n#import \"").append(pureFieldType).append(".h\"");
            }

            propertyBuilder.append("\n@property(nonatomic, ").append(propertyType).append(") ");
            propertyBuilder.append(fieldType);
            propertyBuilder.append(fieldName);
            propertyBuilder.append(";");

            observerBuilder.append("\n\t[RACObserve(self.vm, ").append(fieldName).append(") subscribeNext:^(").append(fieldType).append(fieldName).append(") {");
            observerBuilder.append("\n\t\tif (").append(fieldName).append(" != nil) {");
            observerBuilder.append("\n\n\t\t}");
            observerBuilder.append("\n\t}];");

            bindBuilder.append("\n\tRAC(self.vm, ").append(fieldName).append(") = self.").append(bindView).append(".rac_textSignal;");

        }

        list.add(propertyBuilder.toString());
        list.add(observerBuilder.toString());
        list.add(bindBuilder.toString());
        list.add(importBuilder.toString());

        return list;

    }

    private static String getFieldType(String text) {


        switch (text) {
            case "int":
                return "NSInteger\t";
            case "bool":
                return "Boolean\t";
            case "str":
                return "NSString\t*";
            default:
                return "" + text + " *";
        }
    }

    private static boolean isNormalField(String text) {
        return "int".equals(text) || "bool".equals(text) || "str".equals(text);
    }

    private static String getPureFieldType(String text) {

        switch (text) {
            case "int":
                return "NSInteger";
            case "bool":
                return "Boolean";
            case "str":
                return "NSString";
            default:
                return "" + text + "";
        }
    }

    private static String getPropertyType(String text) {

        switch (text) {
            case "int":
            case "bool":
                return "assign";
            case "str":
            default:
                return "strong";
        }
    }
}
