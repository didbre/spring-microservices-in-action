package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import com.optimagrowth.license.model.Organization;
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

  @Autowired
  OrganizationRestTemplateClient organizationRestClient;

  @Autowired
  OrganizationDiscoveryClient organizationDiscoveryClient;

  public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository, ServiceConfig serviceConfig)
  {
    this.messageSource = messageSource;
    this.licenseRepository = licenseRepository;
    this.serviceConfig = serviceConfig;
  }

  public License getLicense(String licenseId, String organizationId, String clientType) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    if (null == license) {
      throw new IllegalArgumentException(String.format(messageSource.getMessage(
              "licence.search.error.message", null, null), licenseId, organizationId));
    }
    Organization organization = retrieveOrganizationInfo(organizationId, clientType);
    if (null != organization) {
      license.setOrganizationName(organization.getName());
      license.setContactName(organization.getContactName());
      license.setContactEmail(organization.getContactEmail());
      license.setContactPhone(organization.getContactPhone());
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

  private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
    Organization organization = null;

    switch (clientType) {

      case "rest":
        System.out.println("I am using the rest client");
        organization = organizationRestClient.getOrganization(organizationId);
        break;
      case "discovery":
        System.out.println("I am using the discovery client");
        organization = organizationDiscoveryClient.getOrganization(organizationId);
        break;
      default:
        organization = organizationRestClient.getOrganization(organizationId);
        break;
    }

    return organization;
  }
}
