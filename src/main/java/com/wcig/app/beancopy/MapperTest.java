package com.wcig.app.beancopy;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class MapperTest {
    public static void main(String[] args)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PersonDO personDO = new PersonDO();
        personDO.setId(1);
        personDO.setAge(30);
        personDO.setName("Tom");
        personDO.setBirthday(new Date());

        MapperTest mapperTest = new MapperTest();
        mapperTest.mappingByJavaCode(personDO, 100);
        mapperTest.mappingByJavaCode(personDO, 1000);
        mapperTest.mappingByJavaCode(personDO, 10000);
        mapperTest.mappingByJavaCode(personDO, 100000);
        mapperTest.mappingByJavaCode(personDO, 1000000);

        mapperTest.mappingByMapstruct(personDO, 100);
        mapperTest.mappingByMapstruct(personDO, 1000);
        mapperTest.mappingByMapstruct(personDO, 10000);
        mapperTest.mappingByMapstruct(personDO, 100000);
        mapperTest.mappingByMapstruct(personDO, 1000000);

        mapperTest.mappingBySpringBeanUtils(personDO, 100);
        mapperTest.mappingBySpringBeanUtils(personDO, 1000);
        mapperTest.mappingBySpringBeanUtils(personDO, 10000);
        mapperTest.mappingBySpringBeanUtils(personDO, 100000);
        mapperTest.mappingBySpringBeanUtils(personDO, 1000000);

        mapperTest.mappingByCglibBeanCopier(personDO, 100);
        mapperTest.mappingByCglibBeanCopier(personDO, 1000);
        mapperTest.mappingByCglibBeanCopier(personDO, 10000);
        mapperTest.mappingByCglibBeanCopier(personDO, 100000);
        mapperTest.mappingByCglibBeanCopier(personDO, 1000000);

        mapperTest.mappingByApachePropertyUtils(personDO, 100);
        mapperTest.mappingByApachePropertyUtils(personDO, 1000);
        mapperTest.mappingByApachePropertyUtils(personDO, 10000);
        mapperTest.mappingByApachePropertyUtils(personDO, 100000);
        mapperTest.mappingByApachePropertyUtils(personDO, 1000000);

        mapperTest.mappingByApacheBeanUtils(personDO, 100);
        mapperTest.mappingByApacheBeanUtils(personDO, 1000);
        mapperTest.mappingByApacheBeanUtils(personDO, 10000);
        mapperTest.mappingByApacheBeanUtils(personDO, 100000);
        mapperTest.mappingByApacheBeanUtils(personDO, 1000000);
    }

    private void mappingByJavaCode(PersonDO personDO, int times) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = PersonMapperUtil.PersonDO2PersonDTO(personDO);
        }
        stopwatch.stop();
        System.out.println("mappingByJavaCode cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }

    private void mappingByMapstruct(PersonDO personDO, int times) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = PersonConverter.INSTANCE.do2dto(personDO);
        }
        stopwatch.stop();
        System.out.println("mappingByMapstruct cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }

    private void mappingBySpringBeanUtils(PersonDO personDO, int times) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            org.springframework.beans.BeanUtils.copyProperties(personDO, personDTO);
        }
        stopwatch.stop();
        System.out.println("mappingBySpringBeanUtils cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }

    private void mappingByCglibBeanCopier(PersonDO personDO, int times) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            BeanCopier copier = BeanCopier.create(PersonDO.class, PersonDTO.class, false);
            copier.copy(personDO, personDTO, null);
        }
        stopwatch.stop();
        System.out.println("mappingByCglibBeanCopier cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }

    private void mappingByApacheBeanUtils(PersonDO personDO, int times)
            throws InvocationTargetException, IllegalAccessException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            org.apache.commons.beanutils.BeanUtils.copyProperties(personDTO, personDO);
        }
        stopwatch.stop();
        System.out.println("mappingByApacheBeanUtils cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }

    private void mappingByApachePropertyUtils(PersonDO personDO, int times)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            PropertyUtils.copyProperties(personDTO, personDO);
        }
        stopwatch.stop();
        System.out.println("mappingByApachePropertyUtils cost: " + times + " : " + stopwatch.getTotalTimeMillis());
    }
}
