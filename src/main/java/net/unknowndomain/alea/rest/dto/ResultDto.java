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

import net.unknowndomain.alea.messages.MsgFilePart;
import net.unknowndomain.alea.messages.MsgPart;
import net.unknowndomain.alea.messages.MsgTextPart;
import net.unknowndomain.alea.messages.MsgUrlPart;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author m.bignami
 */
public class ResultDto
{
    private String message;
    private GenericResult results;
    
    public ResultDto()
    {
        
    }
    
    public ResultDto(ReturnMsg returnMsg)
    {
        this.message = msgToTxt(returnMsg);
        this.results = null;
    }
    
    public ResultDto(GenericResult result)
    {
        this.message = msgToTxt(result.getMessage());
        this.results = result;
    }
    
    private static String msgToTxt(ReturnMsg msg){
        StringBuilder sb = new StringBuilder();
        for (MsgPart msgPart : msg.getParts())
        {
            if (msgPart instanceof MsgTextPart)
            {
                MsgTextPart part = (MsgTextPart) msgPart;
                sb.append(part.getMsgText());
            }
            else if (msgPart instanceof MsgFilePart)
            {
                MsgFilePart part = (MsgFilePart) msgPart;
                sb.append(part.getFileName());
            }
            else if (msgPart instanceof MsgUrlPart)
            {
                MsgUrlPart part = (MsgUrlPart) msgPart;
                sb.append(part.getUrl());
            }
        }
        return sb.toString();
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public GenericResult getResults()
    {
        return results;
    }

    public void setResults(GenericResult results)
    {
        this.results = results;
    }
}
