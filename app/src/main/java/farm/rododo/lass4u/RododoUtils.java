package farm.rododo.lass4u;

import com.cht.iot.util.JsonUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by rickwang on 2016/11/7.
 */

public class RododoUtils {
    static final int TIMEOUT = 5000;
    static final String AUTH_URL = "http://iot.rododo.farm/lass/auth";

    public static DeviceAuth getDeviceAuth(String key) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setConnectionManagerTimeout(TIMEOUT);
        client.getParams().setSoTimeout(TIMEOUT);

        PostMethod m = new PostMethod(AUTH_URL);
        m.setParameter("data", key);

        int sc = client.executeMethod(m);
        if (sc != HttpStatus.SC_OK) {
            throw new IOException(String.format("[%d] %s", sc, m.getStatusText()));
        }

        InputStreamReader isr  = new InputStreamReader(m.getResponseBodyAsStream());
        AuthResult ar = JsonUtils.fromJson(m.getResponseBodyAsStream(), AuthResult.class);
        if (!AuthResult.SUCCESS.equalsIgnoreCase(ar.status)) {
            throw new IOException(String.format("%s - %s", ar.status, ar.payload));
        }

        return JsonUtils.fromJson(ar.payload, DeviceAuth.class);

    }

    public static class AuthResult {
        static final String SUCCESS = "success";

        public String status;
        public String payload;
    }

    public static class DeviceAuth {
        public String id; // device ID
        public String key; // device API Key
    }
}
