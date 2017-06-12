package org.yaml.snakeyaml.issues.issue382;

import java.util.ArrayList;
import java.util.List;

public  class PublicFooWithoutGetter {
    public List<String> countryCodes = new ArrayList<String>();
    public String some;

    public void setCountryCodes(List<String> countryCodes) {
        for (Object countryCode : countryCodes) {
            System.out.println(countryCode.getClass().getName());
        }
        this.countryCodes = countryCodes;
    }

    public void setSome(String sime) {
        this.some = sime;
    }
}
