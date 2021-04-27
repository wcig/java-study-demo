package com.wcig.app.beancopy;

public class PersonMapperUtil {
    public static PersonDTO PersonDO2PersonDTO(PersonDO pd) {
        if (pd == null) {
            return null;
        }
        PersonDTO pto = new PersonDTO();
        pto.setAge(pd.getAge());
        pto.setName(pd.getName());
        pto.setBirthday(pd.getBirthday());
        return pto;
    }
}
