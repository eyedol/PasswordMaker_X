package io.github.eddieringle.android.apps.passwordmaker.util;

import com.google.common.reflect.Reflection;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Test to serialize an xml
 */
public class XmlSerilizerUtil {

    /** Default parser creator. */
    public static final XmlParserCreator PARSER_CREATOR = new XmlParserCreator() {
        @Override
        public XmlPullParser createParser() {

            try {
                final XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                return parser;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static GsonXml createGson() {
        return createGson(false);
    }

    public static GsonXml createGson(final boolean namespaces) {
        return new GsonXmlBuilder().setXmlParserCreator(PARSER_CREATOR).setSameNameLists(true).setTreatNamespaces(namespaces).create();
    }

}
