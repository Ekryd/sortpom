package sortpom.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wraps the java URL class so that it exposes methods to determine if entered values are urls or not.
 * 
 * @author bjorn
 * @since 2013-08-16
 */
class UrlWrapper {
    private final String spec;

    public UrlWrapper(String spec) {
        this.spec = spec;
    }

    public boolean isUrl() {
        try {
            new URL(spec);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public InputStream openStream() throws IOException {
        URL url = new URL(spec);
        return url.openStream();
    }
}
