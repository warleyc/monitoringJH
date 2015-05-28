package org.shm.monitoring.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.shm.monitoring.domain.util.CustomDateTimeDeserializer;
import org.shm.monitoring.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectConfiguration.
 */
@Entity
@Table(name = "PROJECTCONFIGURATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "post")
    private String post;

    @Column(name = "parametre")
    private String parametre;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "header")
    private String header;

    @Column(name = "frequence")
    private Integer frequence;

    @Column(name = "login")
    private String login;

    @Column(name = "password2")
    private String password2;

    @Column(name = "check_message")
    private String checkMessage;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "last_error")
    private DateTime lastError;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "last_succes")
    private DateTime lastSucces;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "alert_sms")
    private Boolean alertSMS;

    @Column(name = "alert_mail")
    private Boolean alertMail;

    @Column(name = "email")
    private String email;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "soap")
    private Boolean soap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getParametre() {
        return parametre;
    }

    public void setParametre(String parametre) {
        this.parametre = parametre;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getCheckMessage() {
        return checkMessage;
    }

    public void setCheckMessage(String checkMessage) {
        this.checkMessage = checkMessage;
    }

    public DateTime getLastError() {
        return lastError;
    }

    public void setLastError(DateTime lastError) {
        this.lastError = lastError;
    }

    public DateTime getLastSucces() {
        return lastSucces;
    }

    public void setLastSucces(DateTime lastSucces) {
        this.lastSucces = lastSucces;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getAlertSMS() {
        return alertSMS;
    }

    public void setAlertSMS(Boolean alertSMS) {
        this.alertSMS = alertSMS;
    }

    public Boolean getAlertMail() {
        return alertMail;
    }

    public void setAlertMail(Boolean alertMail) {
        this.alertMail = alertMail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getSoap() {
        return soap;
    }

    public void setSoap(Boolean soap) {
        this.soap = soap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectConfiguration projectConfiguration = (ProjectConfiguration) o;

        if ( ! Objects.equals(id, projectConfiguration.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectConfiguration{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", url='" + url + "'" +
                ", post='" + post + "'" +
                ", parametre='" + parametre + "'" +
                ", requestMethod='" + requestMethod + "'" +
                ", header='" + header + "'" +
                ", frequence='" + frequence + "'" +
                ", login='" + login + "'" +
                ", password2='" + password2 + "'" +
                ", checkMessage='" + checkMessage + "'" +
                ", lastError='" + lastError + "'" +
                ", lastSucces='" + lastSucces + "'" +
                ", actif='" + actif + "'" +
                ", alertSMS='" + alertSMS + "'" +
                ", alertMail='" + alertMail + "'" +
                ", email='" + email + "'" +
                ", contentType='" + contentType + "'" +
                ", soap='" + soap + "'" +
                '}';
    }
}
