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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author m.bignami
 */
public class ServiceDesc
{

    public List<CommandDto> getAvailableCommands()
    {
        if (availableCommands == null)
        {
            availableCommands = new LinkedList<>();
        }
        return availableCommands;
    }

    public void setAvailableCommands(List<CommandDto> availableCommands)
    {
        this.availableCommands = availableCommands;
    }

    public List<SystemDto> getAvaliableSystems()
    {
        if (avaliableSystems == null)
        {
            avaliableSystems = new LinkedList<>();
        }
        return avaliableSystems;
    }

    public void setAvaliableSystems(List<SystemDto> avaliableSystems)
    {
        this.avaliableSystems = avaliableSystems;
    }

    public List<String> getAvailableLangs()
    {
        if (availableLangs == null)
        {
            availableLangs = new LinkedList<>();
        }
        return availableLangs;
    }

    public void setAvailableLangs(List<String> availableLangs)
    {
        this.availableLangs = availableLangs;
    }
    private List<CommandDto> availableCommands;
    private List<SystemDto> avaliableSystems;
    private List<String> availableLangs;
}
