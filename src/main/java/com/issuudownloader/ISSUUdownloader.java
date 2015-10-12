package com.issuudownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by thesealex on 12/10/15.
 */
public class ISSUUdownloader {
    public String url;

    public ISSUUdownloader(String url) {
        this.url = url;
    }

    public void download() throws IOException {
        if (checkIssuuValidity(this.url)) {
            System.out.println(this.url + " OK");
            Document doc = Jsoup.connect(this.url).get();
            Elements elements = doc.select("meta[property=og:video]");
            Element i = elements.first();
            System.out.println(i.attr("content"));
            Pattern pattern_id = Pattern.compile("documentId=(.+)");
            Matcher m = pattern_id.matcher(i.attr("content"));
            if (m.find()) {
                String docid = m.group();
            } else {
                System.err.println("Error : No publication found");
                System.exit(-1);
            }


        } else {
            System.err.println("Error : Invalid Issuu publication");
            System.exit(-1);
        }
    }

    private static boolean checkIssuuValidity(String url) {
        Pattern pattern_issuu = Pattern.compile("http://issuu\\.com/.+/docs/.+");
        Matcher matcher = pattern_issuu.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }


}
