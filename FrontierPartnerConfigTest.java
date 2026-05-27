package com.barclaycardus.dynamicurl.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Frontier partner configuration (FTF and FTD)
 * Tests configuration loading, URL generation, and validity settings
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@DisplayName("Frontier Partner Configuration Tests")
public class FrontierPartnerConfigTest {

    @Autowired
    private UrlConfigProperties urlConfigProperties;

    private WorkflowConfig frontierFtfConfig;
    private WorkflowConfig frontierFtdConfig;

    @BeforeEach
    void setUp() {
        frontierFtfConfig = urlConfigProperties.getWorkflows().get("frontier_ftf");
        frontierFtdConfig = urlConfigProperties.getWorkflows().get("frontier_ftd");
    }

    // ============== Configuration Loading Tests ==============

    @Test
    @DisplayName("Should load Frontier FTF workflow configuration")
    void testFrontierFtfConfigurationLoaded() {
        assertNotNull(frontierFtfConfig, "Frontier FTF configuration should be loaded");
    }

    @Test
    @DisplayName("Should load Frontier FTD workflow configuration")
    void testFrontierFtdConfigurationLoaded() {
        assertNotNull(frontierFtdConfig, "Frontier FTD configuration should be loaded");
    }

    // ============== Partner Code Tests ==============

    @Test
    @DisplayName("Should have correct partner code for FTF")
    void testFrontierFtfPartnerCode() {
        assertEquals("FTF", frontierFtfConfig.getPartnerCode(), 
            "Partner code should be FTF");
    }

    @Test
    @DisplayName("Should have correct partner code for FTD")
    void testFrontierFtdPartnerCode() {
        assertEquals("FTD", frontierFtdConfig.getPartnerCode(), 
            "Partner code should be FTD");
    }

    // ============== Partner UUID Tests ==============

    @Test
    @DisplayName("Should have correct partner UUID for FTF")
    void testFrontierFtfPartnerUuid() {
        String expectedUuid = "f8dd2535-46ab-30b5-a396-d3e9538c5551";
        assertEquals(expectedUuid, frontierFtfConfig.getPartnerUUID(), 
            "Partner UUID should match requirement");
    }

    @Test
    @DisplayName("Should have correct partner UUID for FTD")
    void testFrontierFtdPartnerUuid() {
        String expectedUuid = "f8dd2535-46ab-30b5-a396-d3e9538c5551";
        assertEquals(expectedUuid, frontierFtdConfig.getPartnerUUID(), 
            "Partner UUID should match requirement");
    }

    // ============== URL Template Tests ==============

    @Test
    @DisplayName("Should have correct URL template with %servicing% placeholder for FTF")
    void testFrontierFtfUrlTemplate() {
        String expectedTemplate = "www.barclaysus.com/servicing/lookup/%servicing%";
        assertEquals(expectedTemplate, frontierFtfConfig.getUrlTemplate(), 
            "URL template should use %servicing% placeholder");
    }

    @Test
    @DisplayName("Should have correct URL template with %servicing% placeholder for FTD")
    void testFrontierFtdUrlTemplate() {
        String expectedTemplate = "www.barclaysus.com/servicing/lookup/%servicing%";
        assertEquals(expectedTemplate, frontierFtdConfig.getUrlTemplate(), 
            "URL template should use %servicing% placeholder");
    }

    @Test
    @DisplayName("Should contain %servicing% placeholder not %s")
    void testUrlTemplateHasCorrectPlaceholder() {
        assertTrue(frontierFtfConfig.getUrlTemplate().contains("%servicing%"), 
            "Template should contain %servicing% placeholder");
        assertFalse(frontierFtfConfig.getUrlTemplate().contains("%s"), 
            "Template should not contain %s placeholder");
    }

    // ============== Validity Tests ==============

    @Test
    @DisplayName("Should have SMS validity of 15 minutes for FTF")
    void testFrontierFtfSmsValidity() {
        assertEquals("15M", frontierFtfConfig.getValidity().get("SMS"), 
            "SMS validity should be 15 minutes");
    }

    @Test
    @DisplayName("Should have SMS validity of 15 minutes for FTD")
    void testFrontierFtdSmsValidity() {
        assertEquals("15M", frontierFtdConfig.getValidity().get("SMS"), 
            "SMS validity should be 15 minutes");
    }

    @Test
    @DisplayName("Should have EMAIL validity of 14 days for FTF")
    void testFrontierFtfEmailValidity() {
        assertEquals("14D", frontierFtfConfig.getValidity().get("EMAIL"), 
            "EMAIL validity should be 14 days");
    }

    @Test
    @DisplayName("Should have EMAIL validity of 14 days for FTD")
    void testFrontierFtdEmailValidity() {
        assertEquals("14D", frontierFtdConfig.getValidity().get("EMAIL"), 
            "EMAIL validity should be 14 days");
    }

    // ============== Retry and Generation Limit Tests ==============

    @Test
    @DisplayName("Should have maxRetryAllowed of 4 for FTF")
    void testFrontierFtfMaxRetryAllowed() {
        assertEquals(4, frontierFtfConfig.getMaxRetryAllowed(), 
            "Max retry allowed should be 4");
    }

    @Test
    @DisplayName("Should have maxRetryAllowed of 4 for FTD")
    void testFrontierFtdMaxRetryAllowed() {
        assertEquals(4, frontierFtdConfig.getMaxRetryAllowed(), 
            "Max retry allowed should be 4");
    }

    @Test
    @DisplayName("Should have maxUrlGenAllowed of 25 for FTF")
    void testFrontierFtfMaxUrlGenAllowed() {
        assertEquals(25, frontierFtfConfig.getMaxUrlGenAllowed(), 
            "Max URL generation allowed should be 25");
    }

    @Test
    @DisplayName("Should have maxUrlGenAllowed of 25 for FTD")
    void testFrontierFtdMaxUrlGenAllowed() {
        assertEquals(25, frontierFtdConfig.getMaxUrlGenAllowed(), 
            "Max URL generation allowed should be 25");
    }

    // ============== Lookup Time Tests ==============

    @Test
    @DisplayName("Should have lookedUpTo of 2H for FTF")
    void testFrontierFtfLookedUpTo() {
        assertEquals("2H", frontierFtfConfig.getLookedUpTo(), 
            "Looked up to should be 2 hours");
    }

    @Test
    @DisplayName("Should have lookedUpTo of 2H for FTD")
    void testFrontierFtdLookedUpTo() {
        assertEquals("2H", frontierFtdConfig.getLookedUpTo(), 
            "Looked up to should be 2 hours");
    }
}
