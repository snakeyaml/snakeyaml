package org.yaml.snakeyaml.issues.issue479;

public class CredentialAppConfig {
    private Mysql mysql;
    private String test1;

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public Mysql getMysql() {
        return mysql;
    }

    public void setMysql(Mysql mysql) {
        this.mysql = mysql;
    }
}
