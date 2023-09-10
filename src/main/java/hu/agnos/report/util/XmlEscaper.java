/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.util;

import org.apache.commons.text.StringEscapeUtils;

/**
 * @author ruzsaz
 */
public class XmlEscaper {

    public static String escape(String t) {
        if (t == null) {
            return "";
        }
        return StringEscapeUtils.escapeXml11(t);
    }

    public static String unescape(String t) {
        return StringEscapeUtils.unescapeXml(t);
    }

}
