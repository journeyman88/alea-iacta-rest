/*
 * Copyright 2021 m.bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.rest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import net.unknowndomain.alea.rest.dto.SystemParam;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author m.bignami
 */
public class SystemParser
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemParser.class);
    
    public static List<SystemParam> exportOptions(RpgSystemOptions options)
    {
        return exportOptions(options, Locale.ENGLISH);
    }
    
    public static List<SystemParam> exportOptions(RpgSystemOptions options, Locale lang)
    {
        List<SystemParam> listParams = new LinkedList<>();
        List<Field> fields = new ArrayList<>();
        ResourceBundle i18n = null;
        if (options.getClass().isAnnotationPresent(RpgSystemData.class)) 
        {
            RpgSystemData data = options.getClass().getAnnotation(RpgSystemData.class);
            try 
            {
                i18n = ResourceBundle.getBundle(data.bundleName(), lang);
            }
            catch (MissingResourceException ex)
            {
                LOGGER.warn(null, ex);
            }
        }
        Class workingClass = options.getClass();
        while (true)
        {
            fields.addAll(Arrays.asList(workingClass.getDeclaredFields()));
            if (Objects.equals(RpgSystemOptions.class, workingClass))
            {
                break;
            }
            workingClass = workingClass.getSuperclass();
        }
        for (Field field : fields) 
        {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RpgSystemOption.class)) {
                RpgSystemOption[] annotations = field.getAnnotationsByType(RpgSystemOption.class);

                for(RpgSystemOption annotation : annotations){
                    SystemParam param = new SystemParam();
                    String optName = null;
                    if (!annotation.name().isEmpty())
                    {
                        optName = annotation.name();
                    }
                    if ((optName == null) && (!annotation.shortcode().isEmpty()))
                    {
                        optName = annotation.shortcode();
                    }
                    param.setName(optName);
                    param.setRequired(annotation.required());
                    if ((i18n != null) && (!annotation.description().isEmpty()))
                    {
                        param.setDescription(i18n.getString(annotation.description()));
                    }
                    Class c = field.getType();
                    if (
                            java.lang.Boolean.class.isAssignableFrom(c) || 
                            java.lang.Boolean.TYPE.isAssignableFrom(c)
                        )
                    {
                        param.setType("boolean");
                    }
                    else if (
                            java.lang.Number.class.isAssignableFrom(c) || 
                            java.lang.Short.TYPE.isAssignableFrom(c) || 
                            java.lang.Integer.TYPE.isAssignableFrom(c) || 
                            java.lang.Long.TYPE.isAssignableFrom(c) || 
                            java.lang.Float.TYPE.isAssignableFrom(c) || 
                            java.lang.Double.TYPE.isAssignableFrom(c)
                            )
                    {
                        param.setType("number");
                    }
                    else
                    {
                        param.setType("string");
                    }
                    listParams.add(param);
                }
            }
        }
        return listParams;
    }
    
    public static void parseOptions(RpgSystemOptions options, Map<String,Object> result)
    {
        List<Field> fields = new ArrayList<>();
        Class workingClass = options.getClass();
        while (true)
        {
            fields.addAll(Arrays.asList(workingClass.getDeclaredFields()));
            if (Objects.equals(RpgSystemOptions.class, workingClass))
            {
                break;
            }
            workingClass = workingClass.getSuperclass();
        }
        for (Field field : fields) 
        {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RpgSystemOption.class)) {
                RpgSystemOption[] annotations = field.getAnnotationsByType(RpgSystemOption.class);

                for(RpgSystemOption annotation : annotations){
                    
                    String optName = null;
                    if (!annotation.name().isEmpty())
                    {
                        optName = annotation.name();
                    }
                    if ((optName == null) && (!annotation.shortcode().isEmpty()))
                    {
                        optName = annotation.shortcode();
                    }
                    if ((optName != null) && (result.containsKey(optName)))
                    {
                        try
                        {
                            field.set(options, result.get(optName));
                        } catch (IllegalArgumentException | IllegalAccessException ex)
                        {
                            LOGGER.error(null, ex);
                        }
                    }
                }
            }
        }
    }
}
