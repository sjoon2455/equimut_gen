


import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class XmlFriendlyNameCoder {

    public final String dollarReplacement;
    public final String escapeCharReplacement;
    public transient Map escapeCache;
    public transient Map unescapeCache;






    public XmlFriendlyNameCoder(String dollarReplacement, String escapeCharReplacement) {
        this.dollarReplacement = dollarReplacement;
        this.escapeCharReplacement = escapeCharReplacement;
    }


    public String encodeName(String name) {
        final WeakReference ref = (WeakReference)escapeCache.get(name);
        String s = (String)(ref == null ? null : ref.get());

        if (s == null) {
            final int length = name.length();
            int i = 0;
            for (; i < length; i++ ) {
                char c = name.charAt(i);
                if (c == '$' || c == '_') {
                    break;
                }
            }
            if (i == length) {
                return name;
            }
            final StringBuffer result = new StringBuffer(length + 8);
            if (i > 0) {
                result.append(name.substring(0, i));
            }
            for (; i < length; i++ ) {
                char c = name.charAt(i);
                if (c == '$') {
                    result.append(dollarReplacement);
                } else if (c == '_') {
                    result.append(escapeCharReplacement);
                } else {
                    result.append(c);
                }
            }
            s = result.toString();
            escapeCache.put(name, new WeakReference(s));
        }
        return s;
    }



    public String decodeName(String name) {
        final WeakReference ref = (WeakReference)unescapeCache.get(name);
        String s = (String)(ref == null ? null : ref.get());
        if (s == null) {
            final char dollarReplacementFirstChar = dollarReplacement.charAt(0);
            final char escapeReplacementFirstChar = escapeCharReplacement.charAt(0);
            final int length = name.length();
            int i = 0;
            for (; i < length; i++ ) {
                char c = name.charAt(i);
                if (c == dollarReplacementFirstChar || c == escapeReplacementFirstChar) {
                    break;
                }
            }
            if (i == length) {
                return name;
            }
            final StringBuffer result = new StringBuffer(length + 8);
            if (i > 0) {
                result.append(name.substring(0, i));
            }
            for (; i < length; i++ ) {
                char c = name.charAt(i);
                if (c == dollarReplacementFirstChar && name.startsWith(dollarReplacement, i)) {
                    i += dollarReplacement.length() - 1;
                    result.append('$');
                } else if (c == escapeReplacementFirstChar
                    && name.startsWith(escapeCharReplacement, i)) {
                    i += escapeCharReplacement.length() - 1;
                    result.append('_');
                } else {
                    result.append(c);
                }
            }
            s = result.toString();
            unescapeCache.put(name, new WeakReference(s));
        }
        return s;
    }

}
