package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {
  private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);
  final LicenseService licenseService;

  public LicenseController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }

//  example de requete
//  http localhost:8080/v1/organization/d898a142-de44-466c-8c88-9ceb2c2429d3/license/f2a9c9d4-d2c0-44fa-97fe-724d77173c62/rest
  @GetMapping(value = "/{licenseId}/{clientType}")
  public ResponseEntity<License> getLicenseWithClient(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId,
      @PathVariable("clientType") String clientType) {
    License license = licenseService.getLicense(licenseId, organizationId, clientType);
    return ResponseEntity.ok(license);
  }

  @PutMapping
  public ResponseEntity<License> updateLicense(
      @PathVariable("organizationId") String organizationId,
      @RequestBody License request,
      @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
    return ResponseEntity.ok(licenseService.updateLicense(request));
  }

  @PostMapping
  public ResponseEntity<License> createLicense(
          @PathVariable("organizationId") String organizationId,
          @RequestBody License request,
          @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
    return ResponseEntity.ok(licenseService.createLicense(request));
  }

  @DeleteMapping(value = "/{licenseId}")
  public ResponseEntity<String> deleteLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId,
      @RequestHeader(value = "Accept-Language", required = false) Locale locale)
  {
    return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
  }

  @RequestMapping(value="/",method = RequestMethod.GET)
  public List<License> getLicenses(@PathVariable("organizationId") String organizationId) throws TimeoutException
  {
    logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
    return licenseService.getLicensesByOrganization(organizationId);
  }
}
