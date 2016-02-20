package ru.doxhost.newhost.server.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.streams.ReadStream;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Eugene Kirin
 */
public class Nh2File {

    public static final Logger logger = LoggerFactory.getLogger(Nh2File.class);

    /**
     * Pack to zip async
     *
     * @param uploadedFile
     * @param asyncHandler
     * @return
     */
    public static Path zip(final Vertx vertx,
                           final String uploadedFile,
                           final String dist,
                           final String archiveName,
                           final String filename,
                           final Handler<AsyncResult<Buffer>> asyncHandler) {

        vertx.fileSystem().open(uploadedFile, new OpenOptions(), asyncResult -> {

            if (asyncResult.failed()) {
                logger.error(asyncResult.cause().getMessage(), asyncResult.cause());
                return;
            }

            Buffer read = Buffer.buffer();

            ReadStream result = asyncResult.result();

            result.handler(new Handler<Buffer>() {
                @Override
                public void handle(Buffer buffer) {
                    read.appendBuffer(buffer);
                }
            });

            result.endHandler(h -> {
                try {
                    asyncResult.result().close();

                    toZip(read.getBytes(), archiveName, filename, dist);

                    asyncHandler.handle(null);
                } catch (IOException e) {
                    logger.error("Failed to zip " + uploadedFile, e);
                }
            });
        });

        return null;
    }

    /**
     * Save to zip archive
     * @param bytes data
     * @param archiveName file archive name
     * @param filename file name inside archive
     * @param dist where to save
     * @throws IOException
     */
    public static void toZip(final byte[] bytes, String archiveName, String filename, String dist) throws IOException {

        try (InputStream in = new ByteArrayInputStream(bytes); ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dist + "/" + archiveName + ".zip"))) {

            out.putNextEntry(new ZipEntry(filename));

            byte[] b = new byte[1024];
            int count;

            while ((count = in.read(b)) > 0) {
                out.write(b, 0, count);
            }
        }
    }
}