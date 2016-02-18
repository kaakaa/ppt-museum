package org.kaakaa.pptmuseum;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.kaakaa.pptmuseum.db.MongoDBClient;
import org.kaakaa.pptmuseum.db.document.Document;
import org.kaakaa.pptmuseum.db.document.Slide;
import org.kaakaa.pptmuseum.http.RequestUtil;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static spark.Spark.*;

/**
 * Created by kaakaa on 16/02/09.
 */
public class Main {
    /**
     * MongoDB client
     */
    private static final MongoDBClient mongoDBClient = new MongoDBClient();

    public static void main(String[] args) throws TimeoutException, UnknownHostException {
        staticFileLocation("/public");

        // top page
        get("/", (rq, rs) -> {
            rs.redirect("/ppt-museum");
            return "redirect to /";
        });
        get("/ppt-museum", (rq, rs) -> {
            Map<String, List<Slide>> map = getDocuments(mongoDBClient);
            return new ModelAndView(map, "ppt-museum");
        }, new JadeTemplateEngine());


        // upload page
        get("/upload", (rq, rs) -> new ModelAndView(new HashMap(), "upload"), new JadeTemplateEngine());
        post("/upload", (rq, rs) -> {
            // parse request
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> fileItems = servletFileUpload.parseRequest(rq.raw());

            // make Slide model
            Slide slide = RequestUtil.makeSlideModel(fileItems);

            // make Document model
            fileItems.stream()
                    .filter(i -> !i.isFormField())
                    .findFirst()
                    .ifPresent(i -> {
                        RequestUtil.makeDocumentModel(i)
                                // upload document
                                .ifPresent(d -> mongoDBClient.upload(slide, d));
                    });

            rs.redirect("/");
            return "redirect to /";
        });

        // slide view page
        get("/slide/:id", (rq, rs) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", rq.params("id"));
            return new ModelAndView(map, "slide");
        }, new JadeTemplateEngine());
        get("/document/pdf/:id", (rq, rs) -> {
            Document document = mongoDBClient.getPDF(rq.params(":id"));
            rs.type(document.getContentType());
            return document.getFile();
        });
    }


    private static Map<String, List<Slide>> getDocuments(MongoDBClient mongoDBClient) {
        Map<String, List<Slide>> map = new HashMap<>();
        map.put("documents", mongoDBClient.searchAll());
        return map;
    }
}
