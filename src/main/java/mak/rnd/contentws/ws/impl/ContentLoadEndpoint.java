package mak.rnd.contentws.ws.impl;

import java.io.IOException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import jakarta.activation.DataHandler;
import mak.rnd.contentws.jaxb.LoadContentRequest;
import mak.rnd.contentws.jaxb.LoadContentResponse;
import mak.rnd.contentws.jaxb.ObjectFactory;

@Endpoint
public class ContentLoadEndpoint {

    /**
     * Фабрика для создания JAXB объектов.
     */
    private final ObjectFactory objectFactory;

    /**
     * Конструктор.
     * 
     * @param objectFactory фабрика для создания JAXB объектов.
     */
    public ContentLoadEndpoint(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

	@PayloadRoot(localPart = "LoadContentRequest", namespace = "http://mak.rnd.contentws/")
	@ResponsePayload
	public LoadContentResponse load(@RequestPayload LoadContentRequest request) throws IOException {
		final LoadContentResponse response = this.objectFactory.createLoadContentResponse();

        response.setAttachmentId(request.getAttachmentId());
        final ResourceDataSource ds = new ResourceDataSource("files/Тестовый файл.pdf", "application/pdf");
        response.setFilename(ds.getName());
        response.setContent(new DataHandler(ds));
        return response;
	}
}
