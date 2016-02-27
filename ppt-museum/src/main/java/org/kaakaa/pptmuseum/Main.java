package org.kaakaa.pptmuseum;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.kaakaa.pptmuseum.db.MongoDBClient;
import org.kaakaa.pptmuseum.db.document.Document;
import org.kaakaa.pptmuseum.db.document.Slide;
import org.kaakaa.pptmuseum.http.RequestUtil;
import org.kaakaa.pptmuseum.jade.ListHelper;
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
        port(80);
        staticFileLocation("/public");

        // top page
        get("/", (rq, rs) -> {
            rs.redirect("/ppt-museum");
            return "redirect to /";
        });
        get("/ppt-museum", (rq, rs) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("slides", mongoDBClient.searchAll());
            map.put("helper", new ListHelper());
            return new ModelAndView(map, "ppt-museum");
        }, new JadeTemplateEngine());


        // upload page
        get("/ppt-museum/upload", (rq, rs) -> new ModelAndView(new HashMap(), "upload"), new JadeTemplateEngine());
        post("/ppt-museum/upload", (rq, rs) -> {
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
                        RequestUtil.makeDocumentModel(i, slide)
                                // upload document
                                .ifPresent(d -> mongoDBClient.upload(slide, d));
                    });
            rs.status(302);
            rs.header("Location", "/");
            return "redirect to /";
        });

        // delete slide
        delete("/ppt-museum/slide/delete/:id", (rq, rs) -> {
            mongoDBClient.delete(rq.params("id"));
            return 0;
        });

        // slide view page
        get("/ppt-museum/slide/:id", (rq, rs) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", rq.params("id"));
            return new ModelAndView(map, "slide");
        }, new JadeTemplateEngine());
        get("/ppt-museum/slide/comments/:id", (rq, rs) -> mongoDBClient.getComments(rq.params(":id")));
        post("/ppt-museum/slide/comments/:id", (rq, rs) -> {
            rs.status(302);
            rs.header("Location", "/");
            return mongoDBClient.addComments(rq.params(":id"), rq.queryParams("name"), rq.queryParams("comment"));
        });
        get("/ppt-museum/document/pdf/:id", (rq, rs) -> {
            Document document = mongoDBClient.getPDF(rq.params(":id"));
            rs.type(document.getContentType());
            return document.getFile();
        });
        get("/ppt-museum/thumbnail/:id", (rq, rs) -> {
            byte[] file = mongoDBClient.getThumbnail(rq.params("id"));
            rs.type("image/png");
            rs.header("Content-length", String.valueOf(file.length));
            rs.raw().getOutputStream().write(file);
            rs.raw().getOutputStream().flush();
            rs.raw().getOutputStream().close();
            return rs.raw();
        });
    }
}
