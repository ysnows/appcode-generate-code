package main.java.api;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import main.java.utils.CommonUtil;

public class ApiUtil {

    public static void main(String[] args) {
        String str = "curl -X GET \"https://mini.aotulive.com:81/api/member/tenant/findAllTenant?typeId=3&hasFocus=0&meter=0&latitude=35.04907&longitude=118.359436&limit=10&page=1\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMTM2MjYyNCwiaWF0IjoxNjMwMDY2NjI0LCJ1c2VySWQiOiIyNDE0In0.z18IUYrCgVdyfDcK6UsiBNhPY_Wy77bm2Chlmn5ipas\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\"";

        apiParse(str, "", "normal");

    }

    public static ArrayList<String> apiParse(String str, String model, String apiType) {
//        String str = "curl -X GET \"https://mini.aotulive.com:81/api/member/member/verificationPhone?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0OTU2MywiaWF0IjoxNjI5MjUzNTYzLCJ1c2VySWQiOiIyNDE0In0.-vt5itSwAF9PLt4DCqsR8ci1oOdU1A5NVmP9--3OlfQ\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\"";
//        str = "curl -X POST \"https://mini.aotulive.com:81/api/member/wx/login?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0ODU2NCwiaWF0IjoxNjI5MjUyNTY0LCJ1c2VySWQiOiIyNDE0In0.O5fBKlOBOMSQGBicAx-tvmHBCxUVUKeuJ79qHjq217Q\" -H \"content-type: application/json; charset=UTF-8\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\" -d \"{\\\"android0rIos\\\":1,\\\"city\\\":\\\"\\\",\\\"country\\\":\\\"\\\",\\\"deviceToken\\\":\\\"Au19UIa6JhkM0LIK8NQGKWjQRe1rRLWQB5k1gQxygekj\\\",\\\"headimgurl\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL5QXw1TpubcCFhvpXicAQvBLGny0DcIjRGksFibRW5hzK3ibbaVM08Xc09zUeKVTqqKPgJd1tjSUIqA/132\\\",\\\"language\\\":\\\"zh_CN\\\",\\\"nickname\\\":\\\"?????????\\\",\\\"openid\\\":\\\"oret550EU-IOK9WONq4-zsjornD4\\\",\\\"province\\\":\\\"\\\",\\\"sex\\\":1,\\\"unionid\\\":\\\"oyx1M5637x_1Dq9YHAZmvR-sxTLY\\\"}\"";

        var pattern = Pattern.compile("curl\\s+-X\\s+(?<method>(GET|POST))\\s\"(?<url>http|https://((?<host>\\w+\\.\\w+\\.\\w+)(:(?<port>\\d+))?)(?<path>(/\\w+)+)(\\?(?<query>(&?\\w+=(\\w|\\d|\\.|:|/|\\?|&|_|-)+)*))?)\"(?<header>(\\s-H\\s\"(\\w+|\\.|-|/|\\s|=|\\d|;)+:\\s(\\w+|\\.|-|/|\\s|=|\\d|;)+\")*)(\\s-d\\s\"(?<post>.*)\")?");
        var matcher = pattern.matcher(str);
        if (matcher.find()) {
            var method = matcher.group("method");
            var url = matcher.group("host");
            var port = matcher.group("port");
            var path = matcher.group("path");
            var query = matcher.group("query");
            var header = matcher.group("header");
            var post = matcher.group("post");
            System.out.println(method);
            System.out.println(url);
            System.out.println(port);
            System.out.println(path);
            System.out.println(query);
            System.out.println(header);
            System.out.println(post);


            //??????api
            var pathArr = path.split("/");
            var lastName = pathArr[pathArr.length - 1];
            var preLastName = pathArr[pathArr.length - 2];

            String apiName = preLastName + CommonUtil.toUpperCase4Index(lastName);

            var methodStrBuilder = new StringBuilder();
            var fieldStrBuilder = new StringBuilder();
            if (apiType.equals("normal")) {
                methodStrBuilder.append("\n- (void)").append(apiName).append(" {");
                methodStrBuilder.append("\n\tNSDictionary *params = @{");

                if (method.equals("GET")) {
                    if (query != null) {
                        var queryArr = query.split("&");
                        for (String querykv : queryArr) {
                            var kvArr = querykv.split("=");
                            var paramName = kvArr[0];
                            methodStrBuilder.append("\n\t\t@\"").append(paramName).append("\": self.").append(paramName).append(",");
                        }
                    }
                }

                if (method.equals("POST")) {
                    if (post != null) {
                        post = post.replace("\\", "");
                        JSONObject postJsonArr = (JSONObject) JSONObject.parse(post);
                        postJsonArr.entrySet().forEach(new Consumer<Map.Entry<String, Object>>() {
                            @Override
                            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                                methodStrBuilder.append("\n\t\t\t@\"").append(stringObjectEntry.getKey()).append("\": self.").append(stringObjectEntry.getKey()).append(",");
                            }
                        });
                    }
                }

                methodStrBuilder.append("\n\t};");
                methodStrBuilder.append("\n\t[[self ").append(method.toLowerCase()).append("Url:API_").append(apiName).append(" withParams:params modelClass:").append(TextUtils.isBlank(model) ? "nil" : "[" + model + " class]").append(" loading:YES] subscribeNext:^(Resp").append(TextUtils.isBlank(model) ? "" : "<" + model + " *>").append(" *resp) {");
                methodStrBuilder.append("\n\t\tif (resp.ok) {");
                if (!TextUtils.isBlank(model)) {
                    methodStrBuilder.append("\n\t\t\tself.").append(CommonUtil.toLowerCase4Index(model)).append("=[resp data];");
                }
                methodStrBuilder.append("\n\n\t\t}");
                methodStrBuilder.append("\n\t}];");
                methodStrBuilder.append("\n}");
            } else if (apiType.equals("list")) {
                //1. model
                methodStrBuilder.append("\n- (Class)modelClass {");
                methodStrBuilder.append("\n\treturn [").append(model).append(" class];");
                methodStrBuilder.append("\n}");

                methodStrBuilder.append("\n\n- (NSString *)apiUrl  {");
                methodStrBuilder.append("\n\treturn API_").append(apiName).append(";");
                methodStrBuilder.append("\n}");

                methodStrBuilder.append("\n\n- (NSMutableDictionary *)parameters {");
                methodStrBuilder.append("\n\tNSMutableDictionary *params = [super parameters];");

                if (method.equals("GET")) {
                    if (query != null) {
                        var queryArr = query.split("&");
                        for (String querykv : queryArr) {

                            var kvArr = querykv.split("=");
                            var paramName = kvArr[0];
                            if (!paramName.equals("page") && !paramName.equals("limit")) {
                                methodStrBuilder.append("\n\tif (self.").append(paramName).append(") {");
                                methodStrBuilder.append("\n\t\tparams[@\"").append(paramName).append("\"] = self.").append(paramName).append(";");
                                methodStrBuilder.append("\n\t}");

                                fieldStrBuilder.append("\n@property(nonatomic, strong) NSString *").append(paramName).append(";");
                            }
                        }
                    }
                }


                if (method.equals("POST")) {
                    if (post != null) {
                        post = post.replace("\\", "");
                        JSONObject postJsonArr = (JSONObject) JSONObject.parse(post);
                        postJsonArr.entrySet().forEach(new Consumer<Map.Entry<String, Object>>() {
                            @Override
                            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                                String paramName = stringObjectEntry.getKey();

                                if (!paramName.equals("page") && !paramName.equals("limit")) {
                                    methodStrBuilder.append("\n\tif (self.").append(paramName).append(") {");
                                    methodStrBuilder.append("\n\t\tparams[@\"").append(paramName).append("\"] = self.").append(paramName).append(";");
                                    methodStrBuilder.append("\n\t}");

                                    fieldStrBuilder.append("\n@property(nonatomic, strong) NSString *").append(paramName).append(";");
                                }
                            }
                        });
                    }
                }

                methodStrBuilder.append("\n\treturn params;");
                methodStrBuilder.append("\n}");
            }


            String apiStr = "\n#define API_" + apiName + "           NEW_API_APPEND_(@\"" + path.replaceFirst("/", "") + "\") \n";
            System.out.println(apiStr);

            System.out.println(methodStrBuilder.toString());

            var list = new ArrayList<String>();
            list.add(apiStr);
            list.add(methodStrBuilder.toString());
            list.add(apiName);
            list.add(fieldStrBuilder.toString());
            return list;

        }
        return null;
    }

    public static ArrayList<String> apiParsed(String str) {
//        String str = "curl -X GET \"https://mini.aotulive.com:81/api/member/member/verificationPhone?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0OTU2MywiaWF0IjoxNjI5MjUzNTYzLCJ1c2VySWQiOiIyNDE0In0.-vt5itSwAF9PLt4DCqsR8ci1oOdU1A5NVmP9--3OlfQ\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\"";
//        str = "curl -X POST \"https://mini.aotulive.com:81/api/member/wx/login?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0ODU2NCwiaWF0IjoxNjI5MjUyNTY0LCJ1c2VySWQiOiIyNDE0In0.O5fBKlOBOMSQGBicAx-tvmHBCxUVUKeuJ79qHjq217Q\" -H \"content-type: application/json; charset=UTF-8\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\" -d \"{\\\"android0rIos\\\":1,\\\"city\\\":\\\"\\\",\\\"country\\\":\\\"\\\",\\\"deviceToken\\\":\\\"Au19UIa6JhkM0LIK8NQGKWjQRe1rRLWQB5k1gQxygekj\\\",\\\"headimgurl\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL5QXw1TpubcCFhvpXicAQvBLGny0DcIjRGksFibRW5hzK3ibbaVM08Xc09zUeKVTqqKPgJd1tjSUIqA/132\\\",\\\"language\\\":\\\"zh_CN\\\",\\\"nickname\\\":\\\"?????????\\\",\\\"openid\\\":\\\"oret550EU-IOK9WONq4-zsjornD4\\\",\\\"province\\\":\\\"\\\",\\\"sex\\\":1,\\\"unionid\\\":\\\"oyx1M5637x_1Dq9YHAZmvR-sxTLY\\\"}\"";

        var pattern = Pattern.compile("curl\\s+-X\\s+(?<method>(GET|POST))\\s\"(?<url>http|https://((?<host>\\w+\\.\\w+\\.\\w+)(:(?<port>\\d+))?)(?<path>(/\\w+)+)(\\?(?<query>(&?\\w+=(\\w+|\\d+)+)*))?)\"(?<header>(\\s-H\\s\"(\\w+|\\.|-|/|\\s|=|\\d|;)+:\\s(\\w+|\\.|-|/|\\s|=|\\d|;)+\")*)(\\s-d\\s\"(?<post>.*)\")?");
        var matcher = pattern.matcher(str);
        if (matcher.find()) {
            var method = matcher.group("method");
            var url = matcher.group("host");
            var port = matcher.group("port");
            var path = matcher.group("path");
            var query = matcher.group("query");
            var header = matcher.group("header");
            var post = matcher.group("post");
            System.out.println(method);
            System.out.println(url);
            System.out.println(port);
            System.out.println(path);
            System.out.println(query);
            System.out.println(header);
            System.out.println(post);


            //??????api
            var pathArr = path.split("/");
            var lastName = pathArr[pathArr.length - 1];
            var preLastName = pathArr[pathArr.length - 2];

            String apiName = preLastName + CommonUtil.toUpperCase4Index(lastName);

            var methodStrBuilder = new StringBuilder();
            methodStrBuilder.append("\n- (RACSignal<id <RespProtocol>> *)").append(apiName).append(" {");
            methodStrBuilder.append("\n\tNSDictionary *params = @{");

            if (method.equals("GET")) {
                if (query != null) {
                    var queryArr = query.split("&");
                    for (String querykv : queryArr) {
                        var kvArr = querykv.split("=");
                        var paramName = kvArr[0];
                        methodStrBuilder.append("\n\t\t@\"").append(paramName).append("\": self.").append(paramName).append(",");
                    }
                }
            }

            if (method.equals("POST")) {
                if (post != null) {
                    post = post.replace("\\", "");
                    JSONObject postJsonArr = (JSONObject) JSONObject.parse(post);
                    postJsonArr.entrySet().forEach(new Consumer<Map.Entry<String, Object>>() {
                        @Override
                        public void accept(Map.Entry<String, Object> stringObjectEntry) {
                            methodStrBuilder.append("\n\t\t@\"").append(stringObjectEntry.getKey()).append("\": self.").append(stringObjectEntry.getKey()).append(",");
                        }
                    });
                }
            }

            methodStrBuilder.append("\n\t};");
            methodStrBuilder.append("\n\treturn [RACSignal createSignal:^RACDisposable *(id <RACSubscriber> subscriber) {");
            methodStrBuilder.append("\n\t\t[[self getUrl:API_").append(apiName).append(" withParams:params modelClass:nil loading:YES] subscribeNext:^(id <RespProtocol> x) {");
            methodStrBuilder.append("\n\t\t\tif (x.ok) {");
            methodStrBuilder.append("\n\t\t\t\t[subscriber sendNext:[x data]];");
            methodStrBuilder.append("\n\t\t\t\t[subscriber sendCompleted];");
            methodStrBuilder.append("\n\t\t\t}");
            methodStrBuilder.append("\n\t\t}];");
            methodStrBuilder.append("\n\t\treturn nil;");
            methodStrBuilder.append("\n\t}];");
            methodStrBuilder.append("\n}");


            String apiStr = "\n#define API_" + apiName + "           NEW_API_APPEND_(@\"" + path.replaceFirst("/", "") + "\") \n";
            System.out.println(apiStr);

            System.out.println(methodStrBuilder.toString());

            var list = new ArrayList<String>();
            list.add(apiStr);
            list.add(methodStrBuilder.toString());
            list.add(apiName);
            return list;

        }
        return null;
    }
}
