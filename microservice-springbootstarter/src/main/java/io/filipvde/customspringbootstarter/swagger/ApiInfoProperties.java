package io.filipvde.customspringbootstarter.swagger;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConfigurationProperties("microservice.swagger.api-info")
public class ApiInfoProperties {
    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String licenseName;
    private String licenseUrl;
    private ContactProperties contact;
    private Optional<BuildProperties> buildPropertiesOpt;

    /**
     * On a pristine checkout the build.properties is not yet present (made by Maven build)
     * so lets not depend on it as it is only ment for the swagger definition
     **/
    @Autowired
    public ApiInfoProperties(@Nonnull final Optional<BuildProperties> buildPropertiesOpt) {
        this.buildPropertiesOpt = buildPropertiesOpt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public void setContact(ContactProperties contact) {
        this.contact = contact;
    }

    public ContactProperties getContact() {
        return contact;
    }

    @Nonnull
    public Info buildApiInfo() {
        String version = buildPropertiesOpt.map(BuildProperties::getVersion).orElse("(unk)");
        Info info = new Info();
        info.setTitle(title);
        info.setDescription(description);
        info.setVersion(version);
        info.setTermsOfService(termsOfServiceUrl);
        License license = new License();
        license.setName(licenseName);
        license.setUrl(licenseUrl);
        info.setLicense(license);
        info.setContact(contact.buildContact());

        return info;
    }

    public static class ContactProperties {
        private String name;
        private String url;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Nonnull
        public Contact buildContact() {
            Contact contact = new Contact();
            contact.setName(name);
            contact.setUrl(url);
            contact.setEmail(email);

            return contact;
        }
    }
}