package mak.rnd.contentws.ws.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;

import jakarta.activation.DataSource;

class ResourceDataSource implements DataSource {

    private final String path;
    private final String name;
    private final String contentType;

    public ResourceDataSource(String path, String contentType) {
        this.path = path;
        this.name = FilenameUtils.getName(path);
        this.contentType = contentType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Unimplemented method 'getOutputStream'");
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
