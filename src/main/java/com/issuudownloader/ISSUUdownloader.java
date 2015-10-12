package com.issuudownloader;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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

    public void download() throws IOException, DocumentException, InterruptedException {
        if (checkIssuuValidity(this.url)) {
            Document doc = Jsoup.connect(this.url).get();
            Elements elements = doc.select("meta[property=og:video]");
            Element i = elements.first();
            Pattern pattern_id = Pattern.compile("documentId=(.+)");
            Matcher m = pattern_id.matcher(i.attr("content"));
            if (m.find()) {
                String docid = m.group(1);
                String xml = "http://document.issuu.com/"+docid+"/document.xml";
                Document docxml = Jsoup.connect(xml).get();
                Elements pages = docxml.select("page");
                String pdffile = docxml.select("document").first().attr("orgDocName");
                String title = docxml.select("document").first().attr("title");
                System.out.println("Ebook found !\nDownloading "+title+" :\n");
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdffile));
                document.open();
                int total = pages.size();
                int n = 1;
                for(Element p : pages){
                    int percent = Math.round(100*n/total);
                    int percentdix = Math.round(percent/10);
                    String str = StringUtils.repeat("#", percentdix)+StringUtils.repeat(" ", 10-percentdix);
                    System.out.print("\r["+str+"] "+percent+"%");
                    String pagenumber = p.attr("number");
                    String jpg = "http://image.issuu.com/"+docid+"/jpg/page_"+pagenumber+".jpg";
                    Image image = Image.getInstance(new URL(jpg));
                    image.scaleToFit(PageSize.A4);
                    document.add(image);
                    document.newPage();
                    n++;
                }
                System.out.println("\r[##########] Done!\n");
                document.close();

                Thread.sleep(1000);
                System.out.println("Filename : " + pdffile);


                Thread.sleep(300);
                System.out.println("Thank you for downloading this ebook using ISSUUdownloader");

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
