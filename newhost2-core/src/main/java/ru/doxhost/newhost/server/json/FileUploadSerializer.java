package ru.doxhost.newhost.server.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.vertx.ext.web.FileUpload;

import java.io.IOException;

/**
 * @author Eugene Kirin
 */
public class FileUploadSerializer extends JsonSerializer<FileUpload> {
    @Override
    public void serialize(FileUpload fileUpload, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(fileUpload.uploadedFileName());
    }
}
