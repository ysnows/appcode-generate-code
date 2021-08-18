package main.java.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtil {

    public static void main(String[] args) {
        String str = "curl -X GET \"https://mini.aotulive.com:81/api/member/member/verificationPhone?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0OTU2MywiaWF0IjoxNjI5MjUzNTYzLCJ1c2VySWQiOiIyNDE0In0.-vt5itSwAF9PLt4DCqsR8ci1oOdU1A5NVmP9--3OlfQ\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\"";
        str = "curl -X POST \"https://mini.aotulive.com:81/api/member/wx/login?phone=17686941213&code=125457&invitationCode=123\" -H \"host: 7gw0sclkcp.xuduan.vip\" -H \"authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqaWFuZyIsImV4cCI6MTYzMDU0ODU2NCwiaWF0IjoxNjI5MjUyNTY0LCJ1c2VySWQiOiIyNDE0In0.O5fBKlOBOMSQGBicAx-tvmHBCxUVUKeuJ79qHjq217Q\" -H \"content-type: application/json; charset=UTF-8\" -H \"user-agent: okhttp/3.12.0\" -H \"Connection: close\" -d \"{\\\"android0rIos\\\":1,\\\"city\\\":\\\"\\\",\\\"country\\\":\\\"\\\",\\\"deviceToken\\\":\\\"Au19UIa6JhkM0LIK8NQGKWjQRe1rRLWQB5k1gQxygekj\\\",\\\"headimgurl\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL5QXw1TpubcCFhvpXicAQvBLGny0DcIjRGksFibRW5hzK3ibbaVM08Xc09zUeKVTqqKPgJd1tjSUIqA/132\\\",\\\"language\\\":\\\"zh_CN\\\",\\\"nickname\\\":\\\"小全速\\\",\\\"openid\\\":\\\"oret550EU-IOK9WONq4-zsjornD4\\\",\\\"province\\\":\\\"\\\",\\\"sex\\\":1,\\\"unionid\\\":\\\"oyx1M5637x_1Dq9YHAZmvR-sxTLY\\\"}\"";

//        var pattern = Pattern.compile("curl\\s+-X\\s+(?<method> GET|POST)\\s\"(?<url>http|https://((?<host>\\w+\\.\\w+\\.\\w+)(:(?<port>\\d+))?)(?<path>(/\\w+)+)(\\?(?<query>(&?\\w+=(\\w+|\\d+)+)*))?)\"(?<header>(\\s-H\\s\"(\\w+|\\.|-|/|\\s|=|\\d|;)+:\\s(.*)+\")*)");
        var pattern = Pattern.compile("curl\\s+-X\\s+(?<method> GET|POST)\\s\"(?<url>http|https://((?<host>\\w+\\.\\w+\\.\\w+)(:(?<port>\\d+))?)(?<path>(/\\w+)+)(\\?(?<query>(&?\\w+=(\\w+|\\d+)+)*))?)\"(?<header>(\\s-H\\s\"(\\w+|\\.|-|/|\\s|=|\\d|;)+:\\s(\\w+|\\.|-|/|\\s|=|\\d|;)+\")*)(\\s-d\"(?<post>.*)\")?");
        var matcher = pattern.matcher(str);
        if (matcher.find()) {
            var method = matcher.group("method");
            var url = matcher.group("host");
            var port = matcher.group("port");
            var path = matcher.group("path");
            var query = matcher.group("query");
            var post = matcher.group("post");
            var header = matcher.group("header");
            System.out.println(method);
            System.out.println(url);
            System.out.println(port);
            System.out.println(path);
            System.out.println(query);
            System.out.println(post);
            System.out.println(header);
        }
    }


//        var arr = str.split(" ");
//
//        var method = arr[2];
//        var url = arr[3];
//
//        int index = 0;
//        for (int i = 0; i < arr.length; i++) {
//            if ("-d".equals(arr[i])) {
//                index = i;
//                break;
//            }
//        }
//        var postStr = "";
//        if (method.equals("POST")) {
//            postStr = arr[index + 1];
//        }
//
//        try {
//            var uri = new URI(url);
//
//            var host = uri.getHost();
//            var port = uri.getPort();
//            var path = uri.getPath();
//            var uriQuery = uri.getQuery();
//
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(arr[2]);
//    }

}
