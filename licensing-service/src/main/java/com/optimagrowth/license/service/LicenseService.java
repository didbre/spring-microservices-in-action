package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class LicenseService {

  final MessageSource messageSource;
  final LicenseRepository licenseRepository;
  final ServiceConfig serviceConfig;

  public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository, ServiceConfig serviceConfig)
  {
    this.messageSource = messageSource;
    this.licenseRepository = licenseRepository;
    this.serviceConfig = serviceConfig;
  }

  public License getLicense(String licenseId, String organizationId) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    if (null == license) {
      throw new IllegalArgumentException(String.format(messageSource.getMessage(
              "licence.search.error.message", null, null), licenseId, organizationId));
    }
    return license.withComment(serviceConfig.getProperty());
  }

  public License createLicense(License license) {
    license.setLicenseId(UUID.randomUUID().toString());
    licenseRepository.save(license);
    return license.withComment(serviceConfig.getProperty());
  }

  public License updateLicense(License license) {
    licenseRepository.save(license);
    return license.withComment(serviceConfig.getProperty());
  }

  public String deleteLicense(String licenseId) {
    String responseMessage = null;
    License license = new License();
    license.setLicenseId(licenseId);
    licenseRepository.delete(license);
    responseMessage = String.format(messageSource.getMessage("license.delete.message",
            null, null), licenseId);

    return responseMessage;

  }
}
