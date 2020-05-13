package org.yaml.snakeyaml.issues.issue479;

public class Mysql {
    private String db_host;
    private String db_name;
    private Integer db_port;
    private String db_user;
    private String db_password;

    public String getDb_host() {
        return db_host;
    }

    public void setDb_host(String db_host) {
        this.db_host = db_host;
    }

    public Integer getDb_port() {
        return db_port;
    }

    public void setDb_port(Integer db_port) {
        this.db_port = db_port;
    }

    public String getDb_user() {
        return db_user;
    }

    public void setDb_user(String db_user) {
        this.db_user = db_user;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }
}
