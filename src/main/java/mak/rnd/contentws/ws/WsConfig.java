package mak.rnd.contentws.ws;

import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import mak.rnd.contentws.jaxb.ObjectFactory;
import mak.rnd.contentws.ws.impl.ContentLoadEndpoint;

@Configuration
@ComponentScan(basePackageClasses = ContentLoadEndpoint.class)
public class WsConfig {

    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }

	@Bean
	ServletRegistrationBean<MessageDispatcherServlet> webServicesRegistration(final ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}

	@Bean(name = "content")
	DefaultWsdl11Definition defaultWsdl11Definition(final XsdSchema countriesSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ContentLoadPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://mak.rnd.contentws/");
		wsdl11Definition.setSchema(countriesSchema);
		return wsdl11Definition;
	}

	@Bean
	XsdSchema countriesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("schema/contentws.xsd"));
	}

	@Bean
	Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("mak.rnd.contentws.jaxb");
		marshaller.setMtomEnabled(true); // здесь маршалеру разрешаем MTOM.
		return marshaller;
	}

	@Bean
	MarshallingPayloadMethodProcessor methodProcessor(final Jaxb2Marshaller marshaller) {
		return new MarshallingPayloadMethodProcessor(marshaller);
	}


	@Bean // Если удалить этот бин, контент будет возвращаеться в base64
	DefaultMethodEndpointAdapter endpointAdapter(final MarshallingPayloadMethodProcessor methodProcessor) {
		DefaultMethodEndpointAdapter adapter = new DefaultMethodEndpointAdapter();
		adapter.setMethodArgumentResolvers(List.of(methodProcessor));
		adapter.setMethodReturnValueHandlers(List.of(methodProcessor));
		return adapter;
	}
}
