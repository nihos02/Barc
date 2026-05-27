package com.barclaycardus.dynamicurl.service;

import com.barclaycardus.dynamicurl.config.WorkflowConfig;
import com.barclaycardus.dynamicurl.dto.DynamicUrlRequest;
import com.barclaycardus.dynamicurl.dto.DynamicUrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Frontier partner URL generation
 * Tests actual URL generation with placeholder substitution
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@DisplayName("Frontier Partner URL Generation Integration Tests")
public class FrontierPartnerUrlGenerationTest {

    @Autowired
    private DynamicUrlService dynamicUrlService;

    @Autowired
    private UrlConfigService urlConfigService;

    private DynamicUrlRequest ftfRequest;
    private DynamicUrlRequest ftdRequest;

    @BeforeEach
    void setUp() {
        // Setup FTF request
        ftfRequest = DynamicUrlRequest.builder()
            .partnerCode("FTF")
            .partnerId("test-partner-123")
            .servicingCode("SVC123456")
            .channel("EMAIL")
            .build();

        // Setup FTD request
        ftdRequest = DynamicUrlRequest.builder()
            .partnerCode("FTD")
            .partnerId("test-partner-456")
            .servicingCode("SVC789012")
            .channel("SMS")
            .build();
    }

    // ============== FTF Partner Tests ==============

    @Test
    @DisplayName("Should generate valid URL for FTF partner")
    void testGenerateUrlForFtfPartner() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftfRequest);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDynamicUrl(), "Dynamic URL should be generated");
        assertTrue(response.isSuccess(), "URL generation should be successful");
    }

    @Test
    @DisplayName("Should use correct template for FTF partner")
    void testFtfPartnerUsesCorrectTemplate() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftfRequest);

        String generatedUrl = response.getDynamicUrl();
        assertTrue(generatedUrl.contains("www.barclaysus.com/servicing/lookup"), 
            "URL should contain correct base template");
    }

    @Test
    @DisplayName("Should substitute %servicing% placeholder for FTF partner")
    void testFtfPartnerSubstitutesServicingPlaceholder() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftfRequest);

        String generatedUrl = response.getDynamicUrl();
        assertFalse(generatedUrl.contains("%servicing%"), 
            "Generated URL should not contain %servicing% placeholder");
        assertTrue(generatedUrl.contains(ftfRequest.getServicingCode()), 
            "Generated URL should contain servicing code");
    }

    @Test
    @DisplayName("Should generate URL with correct format for FTF")
    void testFtfPartnerUrlFormat() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftfRequest);

        String expectedUrlPattern = "www.barclaysus.com/servicing/lookup/SVC123456";
        assertEquals(expectedUrlPattern, response.getDynamicUrl(), 
            "URL should match expected format");
    }

    // ============== FTD Partner Tests ==============

    @Test
    @DisplayName("Should generate valid URL for FTD partner")
    void testGenerateUrlForFtdPartner() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftdRequest);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDynamicUrl(), "Dynamic URL should be generated");
        assertTrue(response.isSuccess(), "URL generation should be successful");
    }

    @Test
    @DisplayName("Should use correct template for FTD partner")
    void testFtdPartnerUsesCorrectTemplate() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftdRequest);

        String generatedUrl = response.getDynamicUrl();
        assertTrue(generatedUrl.contains("www.barclaysus.com/servicing/lookup"), 
            "URL should contain correct base template");
    }

    @Test
    @DisplayName("Should substitute %servicing% placeholder for FTD partner")
    void testFtdPartnerSubstitutesServicingPlaceholder() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftdRequest);

        String generatedUrl = response.getDynamicUrl();
        assertFalse(generatedUrl.contains("%servicing%"), 
            "Generated URL should not contain %servicing% placeholder");
        assertTrue(generatedUrl.contains(ftdRequest.getServicingCode()), 
            "Generated URL should contain servicing code");
    }

    @Test
    @DisplayName("Should generate URL with correct format for FTD")
    void testFtdPartnerUrlFormat() {
        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(ftdRequest);

        String expectedUrlPattern = "www.barclaysus.com/servicing/lookup/SVC789012";
        assertEquals(expectedUrlPattern, response.getDynamicUrl(), 
            "URL should match expected format");
    }

    // ============== Validity Tests ==============

    @Test
    @DisplayName("Should apply 14 days validity for EMAIL channel in FTF")
    void testFtfEmailValidity() {
        DynamicUrlRequest emailRequest = DynamicUrlRequest.builder()
            .partnerCode("FTF")
            .partnerId("test-partner-123")
            .servicingCode("SVC123456")
            .channel("EMAIL")
            .build();

        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(emailRequest);

        assertEquals("14D", response.getValidity(), 
            "EMAIL channel should have 14 days validity");
    }

    @Test
    @DisplayName("Should apply 15 minutes validity for SMS channel in FTF")
    void testFtfSmsValidity() {
        DynamicUrlRequest smsRequest = DynamicUrlRequest.builder()
            .partnerCode("FTF")
            .partnerId("test-partner-123")
            .servicingCode("SVC123456")
            .channel("SMS")
            .build();

        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(smsRequest);

        assertEquals("15M", response.getValidity(), 
            "SMS channel should have 15 minutes validity");
    }

    @Test
    @DisplayName("Should apply 14 days validity for EMAIL channel in FTD")
    void testFtdEmailValidity() {
        DynamicUrlRequest emailRequest = DynamicUrlRequest.builder()
            .partnerCode("FTD")
            .partnerId("test-partner-456")
            .servicingCode("SVC789012")
            .channel("EMAIL")
            .build();

        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(emailRequest);

        assertEquals("14D", response.getValidity(), 
            "EMAIL channel should have 14 days validity");
    }

    @Test
    @DisplayName("Should apply 15 minutes validity for SMS channel in FTD")
    void testFtdSmsValidity() {
        DynamicUrlRequest smsRequest = DynamicUrlRequest.builder()
            .partnerCode("FTD")
            .partnerId("test-partner-456")
            .servicingCode("SVC789012")
            .channel("SMS")
            .build();

        DynamicUrlResponse response = dynamicUrlService.generateDynamicUrl(smsRequest);

        assertEquals("15M", response.getValidity(), 
            "SMS channel should have 15 minutes validity");
    }

    // ============== Retry and Limit Tests ==============

    @Test
    @DisplayName("Should enforce maxRetryAllowed of 4 for FTF")
    void testFtfMaxRetryLimit() {
        WorkflowConfig ftfConfig = urlConfigService.getWorkflowConfig("frontier_ftf");
        assertEquals(4, ftfConfig.getMaxRetryAllowed(), 
            "Max retry should be 4");
    }

    @Test
    @DisplayName("Should enforce maxUrlGenAllowed of 25 for FTF")
    void testFtfMaxUrlGenLimit() {
        WorkflowConfig ftfConfig = urlConfigService.getWorkflowConfig("frontier_ftf");
        assertEquals(25, ftfConfig.getMaxUrlGenAllowed(), 
            "Max URL generation should be 25");
    }

    @Test
    @DisplayName("Should enforce maxRetryAllowed of 4 for FTD")
    void testFtdMaxRetryLimit() {
        WorkflowConfig ftdConfig = urlConfigService.getWorkflowConfig("frontier_ftd");
        assertEquals(4, ftdConfig.getMaxRetryAllowed(), 
            "Max retry should be 4");
    }

    @Test
    @DisplayName("Should enforce maxUrlGenAllowed of 25 for FTD")
    void testFtdMaxUrlGenLimit() {
        WorkflowConfig ftdConfig = urlConfigService.getWorkflowConfig("frontier_ftd");
        assertEquals(25, ftdConfig.getMaxUrlGenAllowed(), 
            "Max URL generation should be 25");
    }

    // ============== Error Handling Tests ==============

    @Test
    @DisplayName("Should fail gracefully for invalid servicing code in FTF")
    void testFtfInvalidServicingCode() {
        DynamicUrlRequest invalidRequest = DynamicUrlRequest.builder()
            .partnerCode("FTF")
            .partnerId("test-partner-123")
            .servicingCode(null)  // Invalid: null servicing code
            .channel("EMAIL")
            .build();

        assertThrows(IllegalArgumentException.class, 
            () -> dynamicUrlService.generateDynamicUrl(invalidRequest),
            "Should throw exception for null servicing code");
    }

    @Test
    @DisplayName("Should fail gracefully for invalid partner code")
    void testInvalidPartnerCode() {
        DynamicUrlRequest invalidRequest = DynamicUrlRequest.builder()
            .partnerCode("INVALID")  // Non-existent partner code
            .partnerId("test-partner-123")
            .servicingCode("SVC123456")
            .channel("EMAIL")
            .build();

        assertThrows(IllegalArgumentException.class, 
            () -> dynamicUrlService.generateDynamicUrl(invalidRequest),
            "Should throw exception for invalid partner code");
    }
}
