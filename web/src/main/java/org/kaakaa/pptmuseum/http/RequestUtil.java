package org.kaakaa.pptmuseum.http;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.kaakaa.pptmuseum.db.document.Document;
import org.kaakaa.pptmuseum.db.document.Slide;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by kaakaa on 16/02/18.
 */
public class RequestUtil {
    /**
     * <p>make Slide class instance of request item</p>
     *
     * @param fileItems FileItem in multipart request
     * @return Slide model
     */
    public static Slide makeSlideModel(List<FileItem> fileItems) {
        Slide slide = new Slide();
        // form data
        fileItems.stream()
                .filter(i -> i.isFormField())
                .forEach(i -> {
                    switch (i.getFieldName()) {
                        case "title":
                            slide.setTitle(i.getString());
                            break;
                        case "desc":
                            slide.setDescription(i.getString());
                            break;
                        case "tags":
                            slide.setTags(i.getString());
                            break;
                    }
                });
        return slide;
    }

    /**
     * <p>make Document class instance of request item</p>
     *
     * @param item FileItem in multipart request
     * @return Document model
     */
    public static Optional<Document> makeDocumentModel(FileItem item) {
        Document doc = new Document();
        doc.setContentType(item.getContentType());

        switch (doc.getContentType()) {
            case "application/pdf":
                doc.setFile(item.get());
                break;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                doc.setFile(convertByJodConverter(item.get(), doc.getContentType()));
                break;
            default:
                return Optional.ofNullable(null);
        }
        return Optional.of(doc);
    }

    /**
     * <p>convert ppt/pptx file to pdf format by JODConverter. </p>
     *
     * @param bytes       ppt/pptx file contents
     * @param contentType Content-type getting request header
     * @return pdf file contents
     */
    private static byte[] convertByJodConverter(byte[] bytes, String contentType) {
        byte[] results = new byte[1024];

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("http://jod:8080/converter/converted/document.pdf");

            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("outputFormat", "pdf")
                    .addBinaryBody("inputDocument", bytes, ContentType.create(contentType), "filename.ppt")
                    .build();
            post.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(post);
            try {
                HttpEntity resEntity = response.getEntity();
                results = EntityUtils.toByteArray(resEntity);
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}
