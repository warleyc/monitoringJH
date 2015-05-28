package org.shm.monitoring.helper.stat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public final class NetworkRequestUtil {

    private NetworkRequestUtil() {
    }

    ;

    // http://code.google.com/intl/fr-FR/mobile/analytics/docs/web/
    public static String constructPageviewRequestPath(Event event,
                                                      String referrer) {
        String pagePath = "";
        if (event.getAction() != null) {
            pagePath = event.getAction();
        }
        if (!pagePath.startsWith("/")) {
            pagePath = (new StringBuilder()).append("/").append(pagePath)
                    .toString();
        }
        pagePath = encode(pagePath);
        Locale locale = Locale.getDefault();
        StringBuilder path = new StringBuilder();
        path.append("/__utm.gif");
        path.append("?utmwv=4.4ma");
        path.append("&utmn=").append(event.getRandomVal());
        path.append("&utmcs=UTF-8");
        path.append(String.format(
                "&utmsr=%dx%d",
                new Object[]{Integer.valueOf(event.getScreenWidth()),
                        Integer.valueOf(event.getScreenHeight())}));
        path.append(String.format("&utmul=%s-%s",
                new Object[]{locale.getLanguage(), locale.getCountry()}));
        path.append("&utmp=").append(pagePath);
        path.append("&utmac=").append(event.getAccountId());
        path.append("&utmcc=").append(getEscapedCookieString(event, referrer));
        return path.toString();
    }

    public static String constructEventRequestPath(Event event, String referrer) {
        Locale locale = Locale.getDefault();
        StringBuilder path = new StringBuilder();
        StringBuilder eventString = new StringBuilder();
        eventString.append(String.format(
                "5(%s*%s",
                new Object[]{encode(event.getCategory()),
                        encode(event.getAction())}));
        if (event.getLabel() != null) {
            eventString.append("*").append(encode(event.getLabel()));
        }
        eventString.append(")");
        if (event.getValue() > -1) {
            eventString.append(String.format("(%d)",
                    new Object[]{Integer.valueOf(event.getValue())}));
        }
        path.append("/__utm.gif");
        path.append("?utmwv=4.4ma");
        path.append("&utmn=").append(event.getRandomVal());
        path.append("&utmt=event");
        path.append("&utme=").append(eventString.toString());
        path.append("&utmcs=UTF-8");
        path.append(String.format(
                "&utmsr=%dx%d",
                new Object[]{Integer.valueOf(event.getScreenWidth()),
                        Integer.valueOf(event.getScreenHeight())}));
        path.append(String.format("&utmul=%s-%s",
                new Object[]{locale.getLanguage(), locale.getCountry()}));
        path.append("&utmac=").append(event.getAccountId());
        path.append("&utmcc=").append(getEscapedCookieString(event, referrer));
        return path.toString();
    }

    public static String getEscapedCookieString(Event event, String referrer) {
        StringBuilder cookieString = new StringBuilder();
        cookieString.append("__utma=");
        cookieString.append("999").append(".");
        cookieString.append(event.getUserId()).append(".");
        cookieString.append(event.getTimestampFirst()).append(".");
        cookieString.append(event.getTimestampPrevious()).append(".");
        cookieString.append(event.getTimestampCurrent()).append(".");
        cookieString.append(event.getVisits());
        if (referrer != null) {
            cookieString.append("+__utmz=");
            cookieString.append("999").append(".");
            cookieString.append(event.getTimestampFirst()).append(".");
            cookieString.append("1.1.");
            cookieString.append(referrer);
        }
        return encode(cookieString.toString());
    }

    private static String encode(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    /*
     * private static final String GOOGLE_ANALYTICS_GIF_PATH = "/__utm.gif";
	 * private static final String FAKE_DOMAIN_HASH = "999";
	 */
}
