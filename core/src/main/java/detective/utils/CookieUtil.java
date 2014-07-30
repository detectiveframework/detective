package detective.utils;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class CookieUtil {
	public static String getCookieValue(String name, CookieStore store) {
		for (Cookie cookie : store.getCookies()) {
			if (cookie.getName().equalsIgnoreCase(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
