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
        for (String str : arrField) {
            var arrFieldInfo = str.split(":");
            var fieldName = arrFieldInfo[0];
            var fieldType = getFieldType(arrFieldInfo.length > 1 ? arrFieldInfo[1] : "str");

            propertyBuilder.append("\n@property(nonatomic, ");
            propertyBuilder.append(fieldType);
            propertyBuilder.append(fieldName);
            propertyBuilder.append(";");

            observerBuilder.append("\n\t[RACObserve(self.viewModel, ").append(fieldName).append(") subscribeNext:^(").append(fieldType).append(fieldName).append(") {");
            observerBuilder.append("\n\t\tif (").append(fieldName).append(" != nil) {");
            observerBuilder.append("\n\n\t\t}");
            observerBuilder.append("\n\t}];");

        }

        list.add(propertyBuilder.toString());
        list.add(observerBuilder.toString());

        return list;

    }

    private static String getFieldType(String text) {

        switch (text) {
            case "int":
                return "assign) NSInteger\t";
            case "bool":
                return "assign) Boolean\t";
            case "str":
                return "copy) NSString\t*";
            default:
                return "";
        }
    }
}
