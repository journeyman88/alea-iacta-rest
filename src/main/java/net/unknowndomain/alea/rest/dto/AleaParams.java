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
package net.unknowndomain.alea.rest.dto;

import java.util.Map;

/**
 *
 * @author m.bignami
 */
public class AleaParams
{
    private String callerId;
    private String langId;
    private String expression;
    private Map<String,Object> systemOptions;

    public String getCallerId()
    {
        return callerId;
    }

    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    public String getExpression()
    {
        return expression;
    }

    public void setExpression(String expression)
    {
        this.expression = expression;
    }

    public Map<String,Object> getSystemOptions()
    {
        return systemOptions;
    }

    public void setSystemOptions(Map<String,Object> systemOptions)
    {
        this.systemOptions = systemOptions;
    }

    public String getLangId()
    {
        return langId;
    }

    public void setLangId(String langId)
    {
        this.langId = langId;
    }
}
