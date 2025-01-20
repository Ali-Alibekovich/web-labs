package org.example;

import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.*;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

public class UDDIServiceManager {

    private static final String CONFIG_FILE = "juddi-client.xml";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_CRED = "admin";

    public static void registerService() throws Exception {
        logInfo("Starting the UDDI client setup for service registration...");
        UDDIClient uddiClient = new UDDIClient(CONFIG_FILE);
        Transport transport = uddiClient.getTransport("default");

        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIPublicationPortType publish = transport.getUDDIPublishService();

        logInfo("Performing authentication with UDDI...");
        String authToken = authenticate(security);

        logInfo("Creating and registering a new business entity...");
        String businessKey = registerBusiness(publish, authToken);

        logInfo("Adding a new service under the business key: " + businessKey);
        registerService(publish, authToken, businessKey);
        logInfo("Service registration was successful.");
    }

    public static void searchAndInvokeService() throws Exception {
        logInfo("Setting up UDDI client for service discovery...");
        UDDIClient uddiClient = new UDDIClient(CONFIG_FILE);
        Transport transport = uddiClient.getTransport("default");

        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIInquiryPortType inquiry = transport.getUDDIInquiryService();

        logInfo("Authenticating to access UDDI services...");
        String authToken = authenticate(security);

        logInfo("Searching for the specified service: 'My Employee Service'...");
        String serviceKey = findService(inquiry, authToken, "My Employee Service");

        logInfo("Fetching the access point for the service with key: " + serviceKey);
        String accessPoint = getServiceAccessPoint(inquiry, authToken, serviceKey);

        logInfo("Access point for the service found: " + accessPoint);
    }

    private static String authenticate(UDDISecurityPortType security) throws Exception {
        GetAuthToken authRequest = new GetAuthToken();
        authRequest.setUserID(ADMIN_USER);
        authRequest.setCred(ADMIN_CRED);
        AuthToken authToken = security.getAuthToken(authRequest);
        logInfo("Authentication completed successfully. Token obtained.");
        return authToken.getAuthInfo();
    }

    private static String registerBusiness(UDDIPublicationPortType publish, String authToken) throws Exception {
        BusinessEntity business = new BusinessEntity();
        business.getName().add(new Name("My Business", null));

        SaveBusiness saveBusiness = new SaveBusiness();
        saveBusiness.setAuthInfo(authToken);
        saveBusiness.getBusinessEntity().add(business);

        BusinessDetail businessDetail = publish.saveBusiness(saveBusiness);
        String businessKey = businessDetail.getBusinessEntity().get(0).getBusinessKey();
        logInfo("Business entity successfully registered with key: " + businessKey);
        return businessKey;
    }

    private static void registerService(UDDIPublicationPortType publish, String authToken, String businessKey) throws Exception {
        BusinessService service = new BusinessService();
        service.setBusinessKey(businessKey);
        service.getName().add(new Name("My Employee Service", null));

        BindingTemplate bindingTemplate = new BindingTemplate();
        bindingTemplate.setAccessPoint(new AccessPoint("http://localhost:8080/web/ws/EmployeeService?wsdl", "wsdl"));
        BindingTemplates bindingTemplates = new BindingTemplates();
        bindingTemplates.getBindingTemplate().add(bindingTemplate);
        service.setBindingTemplates(bindingTemplates);

        SaveService saveService = new SaveService();
        saveService.setAuthInfo(authToken);
        saveService.getBusinessService().add(service);

        ServiceDetail serviceDetail = publish.saveService(saveService);
        logInfo("Service successfully registered with key: " + serviceDetail.getBusinessService().get(0).getServiceKey());
    }

    private static String findService(UDDIInquiryPortType inquiry, String authToken, String serviceName) throws Exception {
        FindService findService = new FindService();
        findService.setAuthInfo(authToken);
        findService.getName().add(new Name(serviceName, null));

        ServiceList serviceList = inquiry.findService(findService);
        if (serviceList.getServiceInfos() == null || serviceList.getServiceInfos().getServiceInfo().isEmpty()) {
            logError("No service found matching the name: " + serviceName);
            throw new Exception("Service not found!");
        }

        String serviceKey = serviceList.getServiceInfos().getServiceInfo().get(0).getServiceKey();
        logInfo("Service located. Key retrieved: " + serviceKey);
        return serviceKey;
    }

    private static String getServiceAccessPoint(UDDIInquiryPortType inquiry, String authToken, String serviceKey) throws Exception {
        GetServiceDetail getServiceDetail = new GetServiceDetail();
        getServiceDetail.setAuthInfo(authToken);
        getServiceDetail.getServiceKey().add(serviceKey);

        ServiceDetail serviceDetail = inquiry.getServiceDetail(getServiceDetail);

        if (serviceDetail.getBusinessService().isEmpty()) {
            logError("No binding information found for service key: " + serviceKey);
            throw new Exception("No bindings found for the specified service!");
        }

        BindingTemplates bindingTemplates = serviceDetail.getBusinessService().get(0).getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            logError("Service bindings are missing for key: " + serviceKey);
            throw new Exception("Service bindings are unavailable!");
        }

        return bindingTemplates.getBindingTemplate().get(0).getAccessPoint().getValue();
    }

    private static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    private static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}