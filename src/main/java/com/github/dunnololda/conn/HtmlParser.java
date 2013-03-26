package com.github.dunnololda.conn;

import org.ccil.cowan.tagsoup.Parser;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class HtmlParser {
    private HtmlHandler h;
    private Parser parser = new Parser();

    private static HtmlParser instance;

    public static HtmlParser instance() {
        if (instance == null) instance = new HtmlParser();
        return instance;
    }

    public HtmlParser() {
    }

    public static JSONObject parse() throws Exception {
        return instance().parse(Conn.instance().currentHtml);
    }

    public JSONObject parse(String html) throws IOException, SAXException, JSONException {
        h = new HtmlHandler();
        parser.setContentHandler(h);
        parser.parse(new InputSource(new StringReader(html)));
        return h.getParsed();
    }
}

class HtmlHandler extends DefaultHandler {
    private JSONObject parsedData = new JSONObject();

    private boolean isTitleObtain = false;
    private boolean isTitleObtained = false;    // to obtain title only once (some clever guys put it further inside too)
    private StringBuilder title = new StringBuilder();

    private boolean isLinkObtain = false;
    private String hrefValue = "";
    private StringBuilder linkText = new StringBuilder();

    public void startDocument() throws SAXException {

    }

    public void startElement(String uri,
                             String local_name,
                             String raw_name,
                             Attributes amap) throws SAXException {
        if ("meta".equalsIgnoreCase(raw_name)) {
            if ("refresh".equals(amap.getValue("http-equiv"))) {
                try {
                    String content = amap.getValue("content");
                    content = content.replace("; URL=", " ");
                    content = content.replace(";URL=", " ");
                    content = content.replace(";URL =", " ");
                    content = content.replace("; URL =", " ");
                    Scanner sc = new Scanner(content);
                    /*JSONObject data = new JSONObject();*/
                    String url = "";
                    if (sc.hasNext()) sc.next();
                    if (sc.hasNext()) url = sc.next();
                    /*parsedData.put("redirect", data);*/
                    if (!"".equals(url)) parsedData.put("redirect", url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if ("title".equalsIgnoreCase(raw_name) && !isTitleObtained) {
            isTitleObtain = true;
        }
        if ("a".equalsIgnoreCase(raw_name)) {
            hrefValue = amap.getValue("href");
            isLinkObtain = true;
        }
        if ("img".equalsIgnoreCase(raw_name)) {
            String img_src = amap.getValue("src");
            if (img_src != null) {
                try {
                    parsedData.accumulate("images", img_src);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if (isTitleObtain) {
            title.append(value);
        }
        if (isLinkObtain) {
            linkText.append(value);
        }
    }

    public void endElement(String uri,
                           String local_name,
                           String raw_name) throws SAXException {
        if ("title".equalsIgnoreCase(raw_name) && isTitleObtain) {
            try {
                parsedData.put("title", title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            title = new StringBuilder();
            isTitleObtain = false;
            isTitleObtained = true;
        }
        if ("a".equalsIgnoreCase(raw_name)) {
            JSONObject data = new JSONObject();
            try {
                data.put("url", hrefValue);
                data.put("text", linkText);
                parsedData.accumulate("links", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            linkText = new StringBuilder();
            hrefValue = "";
            isLinkObtain = false;
        }
    }

    public void endDocument() throws SAXException {

    }

    public JSONObject getParsed() {
        return parsedData;
    }
}
