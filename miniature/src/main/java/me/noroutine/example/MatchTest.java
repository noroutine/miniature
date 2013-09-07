package me.noroutine.example;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class MatchTest {

    public static class UrlRegexp {

        private static final Pattern PARAM_PATTERN = Pattern.compile(":\\w+");

        private HashMap<Integer, String> params;
        private String pattern;

        public UrlRegexp(String pattern) {
            params = new HashMap<Integer, String>();
            Matcher parameterMatcher = PARAM_PATTERN.matcher(pattern);

            StringBuffer sb = new StringBuffer();

            int i = 0;
            while (parameterMatcher.find()) {
                params.put(++i, parameterMatcher.group());

                parameterMatcher.appendReplacement(sb,"(.*)");
            }

            parameterMatcher.appendTail(sb);

            this.pattern = sb.toString();
        }

        public HashMap<Integer, String> getParams() {
            return params;
        }

        public void setParams(HashMap<Integer, String> params) {
            this.params = params;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public HashMap<String, String> match(String url) {
            Pattern p = Pattern.compile(this.pattern);
            Matcher  m = p.matcher(url);

            HashMap<String, String> params = new HashMap<String, String>();
            if (m.matches()) {

                for (int l = m.groupCount(), i = 0; i <= l; i++) {
                    params.put(this.params.get(i ), m.group(i));
                }
            }
            return params;
        }
    }

    public static void main(String... args) {
        String pattern = "/patients/:group/:id";

        UrlRegexp urlRegexp = new UrlRegexp(pattern);

        System.out.println(urlRegexp.getParams().size());
        System.out.println(urlRegexp.getPattern());

        HashMap<String, String> params = urlRegexp.match("/patients/3/1");

        for (Map.Entry<String, String> p: params.entrySet()) {
            System.out.println(p.getKey() + " = " + p.getValue());
        }


    }

}
