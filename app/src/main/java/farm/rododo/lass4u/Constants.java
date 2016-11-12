package farm.rododo.lass4u;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by rickwang on 2016/10/31.
 */

public class Constants {
    static final DateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static {
        UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
