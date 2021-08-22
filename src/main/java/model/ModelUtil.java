package main.java.model;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class ModelUtil {

    public static void main(String[] args) {

        var json = "{\n" +
                "    \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU1MjQyNSwiaWF0IjoxNjI5MjU2NDI1LCJ1c2VySWQiOiIyNDE0In0.GJH96f5VyA-XT5T16R-WzLBYoZzRHxgoJFNn4zUsg7o\",\n" +
                "    \"tokenType\": null,\n" +
                "    \"refreshToken\": \"5fb3293fbfa546e4b377f9612e352ce8\",\n" +
                "    \"name\": \"小全速1\",\n" +
                "    \"account\": null,\n" +
                "    \"avatar\": \"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL5QXw1TpubcCFhvpXicAQvBLGny0DcIjRGksFibRW5hzK3ibbaVM08Xc09zUeKVTqqKPgJd1tjSUIqA/132\",\n" +
                "    \"workDescribe\": null,\n" +
                "    \"userId\": null,\n" +
                "    \"expire\": null,\n" +
                "    \"expiration\": null,\n" +
                "    \"mobile\": null,\n" +
                "    \"tenantCode\": null,\n" +
                "    \"hasNewUser\": false,\n" +
                "    \"openid\": \"oret550EU-IOK9WONq4-zsjornD4\",\n" +
                "    \"memberId\": 2414,\n" +
                "    \"isSetPayPassword\": 0\n" +
                "  }";

        System.out.println(json(json, ""));
    }

    public static String json(String str, String strContent) {

        var methodStrBuilder = new StringBuilder();

        JSONObject postJsonArr = (JSONObject) JSONObject.parse(str);
        postJsonArr.entrySet().forEach(new Consumer<Map.Entry<String, Object>>() {
            @Override
            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                var key = stringObjectEntry.getKey();
                var value = stringObjectEntry.getValue();
                if (strContent.contains(key)) {
                    return;
                }

                if (value instanceof Boolean) {
                    methodStrBuilder.append("\n@property(nonatomic, assign) Boolean ").append(key).append(";");

                } else if (value instanceof Integer) {
                    methodStrBuilder.append("\n@property(nonatomic, assign) NSInteger ").append(key).append(";");
                } else {
                    methodStrBuilder.append("\n@property(nonatomic, strong) NSString *").append(key).append(";");
                }
            }
        });

        return methodStrBuilder.toString();
    }
}
