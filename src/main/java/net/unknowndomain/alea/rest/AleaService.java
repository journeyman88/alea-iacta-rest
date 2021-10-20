package net.unknowndomain.alea.rest;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.unknowndomain.alea.expr.ExpressionCommand;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.rest.dto.AleaParams;
import net.unknowndomain.alea.rest.dto.CommandDto;
import net.unknowndomain.alea.rest.dto.ResultDto;
import net.unknowndomain.alea.rest.dto.ServiceDesc;
import net.unknowndomain.alea.rest.dto.SystemDto;

@Path("/alea")
public class AleaService {
    
    private static final ServiceDesc SERVICE_DESC;
    
    static {
        ServiceDesc temp = new ServiceDesc();
        
        CommandDto command = new CommandDto();
        command.setId("expr");
        command.setPath("/alea/expr");
        temp.getAvailableCommands().add(command);
        
        for (RpgSystemCommand system : RpgSystemCommand.LOADER)
        {
            SystemDto sys = new SystemDto();
            sys.setId(system.getCommandDesc().getCommand());
            sys.setPath("/alea/" + system.getCommandDesc().getCommand());
            sys.setName(system.getCommandDesc().getSystem());
            temp.getAvaliableSystems().add(sys);
        }
        temp.getAvailableLangs().add("en");
        temp.getAvailableLangs().add("it");
        SERVICE_DESC = temp;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceDesc aleaDesc() {
        return SERVICE_DESC;
    }
    
    @POST
    @Path("/expr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultDto expressionSolve(AleaParams params) {
        Optional<UUID> callerId = parseCallerId(params);
        ExpressionCommand expr = new ExpressionCommand();
        ReturnMsg msg = expr.execCommand("expr " + params.getExpression(), callerId);
        return new ResultDto(msg);
    }
    
    @POST
    @Path("/{systemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultDto systemRoll(@PathParam("systemId") String systemId, AleaParams params) {
        
        Optional<RpgSystemCommand> foundCmd = Optional.empty();
        Optional<GenericResult> result = Optional.empty();
        for (RpgSystemCommand cmd : RpgSystemCommand.LOADER)
        {
            if (cmd.getCommandDesc().getCommand().equals(systemId))
            {
                foundCmd = Optional.of(cmd);
            }
        }
        if (foundCmd.isPresent())
        {
            RpgSystemCommand cmd = foundCmd.get();
            result = cmd.execCommand(params.getSystemOptions(), parseCallerId(params));
        }
        if (result.isPresent())
        {
            return new ResultDto(result.get());
        }
        return null;
    }
    
    private Optional<UUID> parseCallerId(AleaParams params)
    {
        UUID uuid = UUID.fromString(params.getCallerId());
        Optional<UUID> callerId = Optional.ofNullable(uuid);
        return callerId;
    }
}