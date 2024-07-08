# Передача бинарного контента через SOAP-сервис с MTOM

При создании SOAP-сервиса передача бинарного контента может производится следующими способами:
1. В теле свойства элемента в base64.
    В этом случае пакет ответа состоит всего из одной части, где в поля, которые должны содержать бинарный контент, он помещается в base64.
    Пример:
    ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:mak="http://mak.rnd.contentws/">
    <soapenv:Header/>
    <soapenv:Body>
        <mak:LoadContentRequest>
            <mak:AttachmentId>123</mak:AttachmentId>
        </mak:LoadContentRequest>
    </soapenv:Body>
    </soapenv:Envelope>
    ```
    Ответ:
    ```xml
    HTTP/1.1 200 
    Accept: text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2
    SOAPAction: ""
    Content-Type: text/xml;charset=utf-8
    Content-Length: 44541
    Date: Mon, 08 Jul 2024 11:28:21 GMT
    Keep-Alive: timeout=60
    Connection: keep-alive

    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:LoadContentResponse xmlns:ns2="http://mak.rnd.contentws/"><ns2:AttachmentId>123</ns2:AttachmentId><ns2:Filename>Тестовый файл.pdf</ns2:Filename><ns2:Content>JVBERi0xLjUKJeLjz9MKMSAwIG9iago8PCAKICAgL1R5cGUgL0NhdGFsb2cKICAgL1BhZ2V...
    ```
1. В теле ответа в MTOM.
    В этом случае пакет ответа состоит из нескольких частей (тип контента: Multipart/Related): сам SOAP пакет, где в полях, которые должны содержать бинарный контент, помещаются ссылки на контент, который идёт следующими частями пакета.
    Пример:
    ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:mak="http://mak.rnd.contentws/">
    <soapenv:Header/>
    <soapenv:Body>
        <mak:LoadContentRequest>
            <mak:AttachmentId>123</mak:AttachmentId>
        </mak:LoadContentRequest>
    </soapenv:Body>
    </soapenv:Envelope>
    ```
    Ответ:
    ```xml
    HTTP/1.1 200 
    Accept: text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2
    SOAPAction: ""
    Content-Type: Multipart/Related; boundary="----=_Part_0_287038979.1720436840228"; type="application/xop+xml"; start-info="text/xml"
    Transfer-Encoding: chunked
    Date: Mon, 08 Jul 2024 11:07:20 GMT
    Keep-Alive: timeout=60
    Connection: keep-alive

    ------=_Part_0_287038979.1720436840228
    Content-Type: application/xop+xml; charset=utf-8; type="text/xml"

    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:LoadContentResponse xmlns:ns2="http://mak.rnd.contentws/"><ns2:AttachmentId>123</ns2:AttachmentId><ns2:Filename>Тестовый файл.pdf</ns2:Filename><ns2:Content><xop:Include xmlns:xop="http://www.w3.org/2004/08/xop/include" href="cid:3fc6feca-da06-4280-b782-acb8f38a2bfe%40mak.rnd.contentws"/></ns2:Content></ns2:LoadContentResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
    ------=_Part_0_287038979.1720436840228
    Content-Type: application/pdf
    Content-ID: <3fc6feca-da06-4280-b782-acb8f38a2bfe@mak.rnd.contentws>
    Content-Transfer-Encoding: binary

    %PDF-1.5
    ...
    ```
1. Путём передачи ссылки для скачивания на общий сервис обмена бинарным контентом.
В этом случае вместо самого бинарного контента в сообщении передаётся ссылка на общий сервис, к которому имеют доступ как сервис, так и все его клиенты. Получатель скачивает контент по полученной ссылке.

Это приложение является примером сервиса, передающего контент, используя MTOM. 
Если удалить бин "endpointAdapter" в классе WsConfig, то контент будет возвращаться в base64.