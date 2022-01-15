package com.unknowncoder.bloodbank.Utils;

public class BloodRequest
{
    private final String Application_no;
    private final String application_name;
    private final String application_mobile;
    private final String application_status;
    private final String application_bloodGroup;

    public BloodRequest(String application_no, String application_name, String application_mobile, String application_status, String application_bloodGroup) {
        this.Application_no = application_no;
        this.application_name = application_name;
        this.application_mobile = application_mobile;
        this.application_status = application_status;
        this.application_bloodGroup = application_bloodGroup;
    }

    public String getApplication_no() {
        return Application_no;
    }

    public String getApplication_name() {
        return application_name;
    }

    public String getApplication_mobile() {
        return application_mobile;
    }

    public String getApplication_status() {
        return application_status;
    }

    public String getApplication_bloodGroup() {
        return application_bloodGroup;
    }
}
