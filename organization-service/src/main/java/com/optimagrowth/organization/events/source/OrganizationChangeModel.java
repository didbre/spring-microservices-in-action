package com.optimagrowth.organization.events.source;

import java.util.Objects;

public class OrganizationChangeModel
{
    private String type;
    private String action;
    private String organizationId;
    private String correlationId;

    public OrganizationChangeModel(String type, String action, String organizationId, String correlationId)
    {
        this.type = type;
        this.action = action;
        this.organizationId = organizationId;
        this.correlationId = correlationId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getOrganizationId()
    {
        return organizationId;
    }

    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }

    @Override
    public String toString()
    {
        return "OrganizationChangeModel{" +
                "type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationChangeModel that = (OrganizationChangeModel) o;
        return Objects.equals(type, that.type) && Objects.equals(action, that.action) && Objects.equals(organizationId, that.organizationId) && Objects.equals(correlationId, that.correlationId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, action, organizationId, correlationId);
    }
}
