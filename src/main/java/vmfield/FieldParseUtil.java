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
            var fieldType = getFieldType(arrFieldInfo[1]);

            propertyBuilder.append("\n@property(nonatomic, copy) ");
            propertyBuilder.append(fieldType);
            propertyBuilder.append(fieldName);
            propertyBuilder.append(";");

            observerBuilder.append("\n\t[RACObserve(self.viewModel, " + fieldName + ") subscribeNext:^(id x) {");
            observerBuilder.append("\n\t\tif (" + fieldName + " != nil) {");
            observerBuilder.append("\n\t\tif (" + fieldName + " != nil) {");
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
                return "NSInteger\t";
            case "bool":
                return "Boolean\t";
            case "str":
            default:
                return "NSString\t\\*\t";
        }
    }
}
