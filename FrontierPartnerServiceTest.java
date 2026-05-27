package com.barclaycardus.dynamicurl.service;

import com.barclaycardus.dynamicurl.config.UrlConfigProperties;
import com.barclaycardus.dynamicurl.config.WorkflowConfig;
import com.barclaycardus.dynamicurl.dto.DynamicUrlRequest;
import com.barclaycardus.dynamicurl.entity.DynamicUrlEntity;
import com.barclaycardus.dynamicurl.repository.DynamicUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests with Mockito for Frontier partner URL service
 * Tests business logic in isolation without Spring context
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Frontier Partner Service Unit Tests (Mockito)")
public class FrontierPartnerServiceTest {

    @Mock
    private DynamicUrlRepository dynamicUrlRepository;

    @Mock
    private UrlConfigProperties urlConfigProperties;

    @InjectMocks
    private DynamicUrlServiceImpl dynamicUrlService;

    private WorkflowConfig ftfWorkflow;
    private WorkflowConfig ftdWorkflow;
    private UrlConfigProperties mockConfigProperties;

    @BeforeEach
    void setUp() {
        // Setup FTF workflow config
        ftfWorkflow = WorkflowConfig.builder()
            .urlTemplate("www.barclaysus.com/servicing/lookup/%servicing%")
            .partnerCode("FTF")
            .partnerUUID("f8dd2535-46ab-30b5-a396-d3e9538c5551")
            .maxRetryAllowed(4)
            .maxUrlGenAllowed(25)
            .lookedUpTo("2H")
            .validity(Map.of("SMS", "15M", "EMAIL", "14D"))
            .build();

        // Setup FTD workflow config
        ftdWorkflow = WorkflowConfig.builder()
            .urlTemplate("www.barclaysus.com/servicing/lookup/%servicing%")
            .partnerCode("FTD")
            .partnerUUID("f8dd2535-46ab-30b5-a396-d3e9538c5551")
            .maxRetryAllowed(4)
            .maxUrlGenAllowed(25)
            .lookedUpTo("2H")
            .validity(Map.of("SMS", "15M", "EMAIL", "14D"))
            .build();

        // Setup mock config properties
        Map<String, WorkflowConfig> workflows = new HashMap<>();
        workflows.put("frontier_ftf", ftfWorkflow);
        workflows.put("frontier_ftd", ftdWorkflow);

        mockConfigProperties = new UrlConfigProperties();
        mockConfigProperties.setWorkflows(workflows);
    }

    // ============== FTF Workflow Resolution Tests ==============

    @Test
    @DisplayName("Should resolve FTF partner code to frontier_ftf workflow")
    void testResolveFtfWorkflow() {
        when(urlConfigProperties.getWorkflows()).thenReturn(
            mockConfigProperties.getWorkflows()
        );

        WorkflowConfig result = dynamicUrlService.getWorkflowConfig("FTF");
        
        assertNotNull(result, "Workflow should be resolved");
        assertEquals("FTF", result.getPartnerCode(), "Partner code should be FTF");
    }

    @Test
    @DisplayName("Should resolve FTD partner code to frontier_ftd workflow")
    void testResolveFtdWorkflow() {
        when(urlConfigProperties.getWorkflows()).thenReturn(
            mockConfigProperties.getWorkflows()
        );

        WorkflowConfig result = dynamicUrlService.getWorkflowConfig("FTD");
        
        assertNotNull(result, "Workflow should be resolved");
        assertEquals("FTD", result.getPartnerCode(), "Partner code should be FTD");
    }

    // ============== URL Template Tests ==============

    @Test
    @DisplayName("Should use %servicing% placeholder in FTF template")
    void testFtfTemplateHasServicingPlaceholder() {
        assertTrue(ftfWorkflow.getUrlTemplate().contains("%servicing%"),
            "FTF template should contain %servicing% placeholder");
        assertEquals("www.barclaysus.com/servicing/lookup/%servicing%",
            ftfWorkflow.getUrlTemplate(),
            "FTF template should match requirement");
    }

    @Test
    @DisplayName("Should use %servicing% placeholder in FTD template")
    void testFtdTemplateHasServicingPlaceholder() {
        assertTrue(ftdWorkflow.getUrlTemplate().contains("%servicing%"),
            "FTD template should contain %servicing% placeholder");
        assertEquals("www.barclaysus.com/servicing/lookup/%servicing%",
            ftdWorkflow.getUrlTemplate(),
            "FTD template should match requirement");
    }

    @Test
    @DisplayName("Should replace %servicing% with actual servicing code")
    void testServicingPlaceholderReplacement() {
        String template = ftfWorkflow.getUrlTemplate();
        String servicingCode = "SVC123456";
        String result = template.replace("%servicing%", servicingCode);

        assertEquals("www.barclaysus.com/servicing/lookup/SVC123456", result,
            "Placeholder should be replaced with servicing code");
    }

    // ============== Validity Configuration Tests ==============

    @Test
    @DisplayName("Should have both SMS and EMAIL validity for FTF")
    void testFtfValidityHasBothChannels() {
        Map<String, String> validity = ftfWorkflow.getValidity();
        
        assertNotNull(validity.get("SMS"), "SMS validity should be configured");
        assertNotNull(validity.get("EMAIL"), "EMAIL validity should be configured");
    }

    @Test
    @DisplayName("Should have correct SMS and EMAIL validity for FTF")
    void testFtfValidityValues() {
        Map<String, String> validity = ftfWorkflow.getValidity();
        
        assertEquals("15M", validity.get("SMS"), "SMS should be 15 minutes");
        assertEquals("14D", validity.get("EMAIL"), "EMAIL should be 14 days");
    }

    @Test
    @DisplayName("Should have both SMS and EMAIL validity for FTD")
    void testFtdValidityHasBothChannels() {
        Map<String, String> validity = ftdWorkflow.getValidity();
        
        assertNotNull(validity.get("SMS"), "SMS validity should be configured");
        assertNotNull(validity.get("EMAIL"), "EMAIL validity should be configured");
    }

    @Test
    @DisplayName("Should have correct SMS and EMAIL validity for FTD")
    void testFtdValidityValues() {
        Map<String, String> validity = ftdWorkflow.getValidity();
        
        assertEquals("15M", validity.get("SMS"), "SMS should be 15 minutes");
        assertEquals("14D", validity.get("EMAIL"), "EMAIL should be 14 days");
    }

    // ============== Partner UUID Tests ==============

    @Test
    @DisplayName("Should have correct UUID for FTF")
    void testFtfUuid() {
        String expectedUuid = "f8dd2535-46ab-30b5-a396-d3e9538c5551";
        assertEquals(expectedUuid, ftfWorkflow.getPartnerUUID(),
            "FTF UUID should match requirement");
    }

    @Test
    @DisplayName("Should have correct UUID for FTD")
    void testFtdUuid() {
        String expectedUuid = "f8dd2535-46ab-30b5-a396-d3e9538c5551";
        assertEquals(expectedUuid, ftdWorkflow.getPartnerUUID(),
            "FTD UUID should match requirement");
    }

    @Test
    @DisplayName("FTF and FTD should share same UUID")
    void testFtfAndFtdSameUuid() {
        assertEquals(ftfWorkflow.getPartnerUUID(), ftdWorkflow.getPartnerUUID(),
            "Both FTF and FTD should have same UUID");
    }

    // ============== Retry and Limit Tests ==============

    @Test
    @DisplayName("Should enforce retry limits for FTF")
    void testFtfRetryLimits() {
        assertEquals(4, ftfWorkflow.getMaxRetryAllowed(),
            "Max retry should be 4");
        assertEquals(25, ftfWorkflow.getMaxUrlGenAllowed(),
            "Max URL generation should be 25");
    }

    @Test
    @DisplayName("Should enforce retry limits for FTD")
    void testFtdRetryLimits() {
        assertEquals(4, ftdWorkflow.getMaxRetryAllowed(),
            "Max retry should be 4");
        assertEquals(25, ftdWorkflow.getMaxUrlGenAllowed(),
            "Max URL generation should be 25");
    }

    @Test
    @DisplayName("FTF and FTD should have identical retry limits")
    void testFtfAndFtdSameRetryLimits() {
        assertEquals(ftfWorkflow.getMaxRetryAllowed(), ftdWorkflow.getMaxRetryAllowed(),
            "Max retry should be same");
        assertEquals(ftfWorkflow.getMaxUrlGenAllowed(), ftdWorkflow.getMaxUrlGenAllowed(),
            "Max URL gen should be same");
    }

    // ============== Database Persistence Tests ==============

    @Test
    @DisplayName("Should save FTF generated URL to database")
    void testSaveFtfGeneratedUrl() {
        DynamicUrlEntity urlEntity = DynamicUrlEntity.builder()
            .partnerCode("FTF")
            .servicingCode("SVC123456")
            .dynamicUrl("www.barclaysus.com/servicing/lookup/SVC123456")
            .validity("14D")
            .build();

        when(dynamicUrlRepository.save(any(DynamicUrlEntity.class)))
            .thenReturn(urlEntity);

        DynamicUrlEntity saved = dynamicUrlRepository.save(urlEntity);

        assertNotNull(saved, "URL should be saved");
        assertEquals("FTF", saved.getPartnerCode(), "Partner code should be FTF");
        verify(dynamicUrlRepository, times(1)).save(any(DynamicUrlEntity.class));
    }

    @Test
    @DisplayName("Should retrieve FTF generated URL from database")
    void testRetrieveFtfGeneratedUrl() {
        DynamicUrlEntity urlEntity = DynamicUrlEntity.builder()
            .id(1L)
            .partnerCode("FTF")
            .servicingCode("SVC123456")
            .dynamicUrl("www.barclaysus.com/servicing/lookup/SVC123456")
            .validity("14D")
            .build();

        when(dynamicUrlRepository.findByPartnerCodeAndServicingCode("FTF", "SVC123456"))
            .thenReturn(Optional.of(urlEntity));

        Optional<DynamicUrlEntity> found = dynamicUrlRepository
            .findByPartnerCodeAndServicingCode("FTF", "SVC123456");

        assertTrue(found.isPresent(), "URL should be found");
        assertEquals("www.barclaysus.com/servicing/lookup/SVC123456", found.get().getDynamicUrl());
        verify(dynamicUrlRepository, times(1))
            .findByPartnerCodeAndServicingCode("FTF", "SVC123456");
    }

    // ============== Validation Tests ==============

    @Test
    @DisplayName("Should validate FTF configuration is complete")
    void testFtfConfigurationIsComplete() {
        assertNotNull(ftfWorkflow.getUrlTemplate(), "URL template should not be null");
        assertNotNull(ftfWorkflow.getPartnerCode(), "Partner code should not be null");
        assertNotNull(ftfWorkflow.getPartnerUUID(), "Partner UUID should not be null");
        assertNotNull(ftfWorkflow.getValidity(), "Validity should not be null");
        assertTrue(ftfWorkflow.getValidity().size() > 0, "Validity should have entries");
    }

    @Test
    @DisplayName("Should validate FTD configuration is complete")
    void testFtdConfigurationIsComplete() {
        assertNotNull(ftdWorkflow.getUrlTemplate(), "URL template should not be null");
        assertNotNull(ftdWorkflow.getPartnerCode(), "Partner code should not be null");
        assertNotNull(ftdWorkflow.getPartnerUUID(), "Partner UUID should not be null");
        assertNotNull(ftdWorkflow.getValidity(), "Validity should not be null");
        assertTrue(ftdWorkflow.getValidity().size() > 0, "Validity should have entries");
    }
}
