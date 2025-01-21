package org.example;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.*;

import static java.util.Base64.*;

public class AuthorizationHandler implements SOAPHandler<SOAPMessageContext> {

        private static final String USERNAME = "admin";
        private static final String PASSWORD = "password";

        @Override
        public boolean handleMessage(SOAPMessageContext context) {
            Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (isOutbound) {
                try {
                    // Создание Basic Authorization заголовка
                    String credentials = USERNAME + ":" + PASSWORD;
                    String basicAuth = "Basic " + getEncoder().encodeToString(credentials.getBytes());

                    // Добавление заголовка в HTTP
                    Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
                    if (headers == null) {
                        headers = new java.util.HashMap<>();
                    }
                    headers.put("Authorization", Collections.singletonList(basicAuth));
                    context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            return true;
        }

        @Override
        public void close(MessageContext context) {}

        @Override
        public Set<QName> getHeaders() {
            return null;
        }
    }