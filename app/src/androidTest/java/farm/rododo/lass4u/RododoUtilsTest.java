package farm.rododo.lass4u;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.cht.iot.util.JsonUtils;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RododoUtilsTest extends ApplicationTestCase {

    public RododoUtilsTest() {
        super(Application.class);
    }

    public void testGetDeviceAuth() throws Exception {
        String key = "otpauth://totp/FT2_RICK?secret=ERMCGSRTGBQW6R2RNBBHEIJENZAHK3KD&issuer=RODODO%20LASS4U";
        RododoUtils.DeviceAuth da = RododoUtils.getDeviceAuth(key);

        System.out.println(JsonUtils.toJson(da));
    }
}