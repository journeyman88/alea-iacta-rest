package net.unknowndomain.alea.rest;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import net.unknowndomain.alea.command.PrintableOutput;
import net.unknowndomain.alea.expr.ExpressionCommand;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.rest.dto.AleaParams;
import net.unknowndomain.alea.rest.dto.CommandDto;
import net.unknowndomain.alea.rest.dto.ResultDto;
import net.unknowndomain.alea.rest.dto.ServiceDesc;
import net.unknowndomain.alea.rest.dto.SystemDto;
import net.unknowndomain.alea.rest.dto.SystemParam;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/alea")
public class AleaService {
    
    private static final ServiceDesc SERVICE_DESC;
    private static final UUID NAMESPACE = Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate();
    private static final Logger LOGGER = LoggerFactory.getLogger(AleaService.class);
    
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
        ResultDto retVal;
        Optional<UUID> callerId = parseCallerId(params);
        ExpressionCommand expr = new ExpressionCommand();
        Optional<PrintableOutput> out = expr.execCommand("expr " + params.getExpression(), callerId);
        if (out.isPresent())
        {
            retVal = new ResultDto(out.get());
        }
        else
        {
            retVal = new ResultDto(expr.printHelp(Locale.ENGLISH));
        }
        return retVal;
    }
    
    @GET
    @Path("/{systemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SystemParam> systemParams(@PathParam("systemId") String systemId, @QueryParam("langId") String langId) {
        
        Optional<RpgSystemCommand> foundCmd = Optional.empty();
        Optional<Locale> lang = parseLang(langId);
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
            RpgSystemOptions opt = cmd.buildOptions();
            if (lang.isPresent())
            {
                return SystemParser.exportOptions(opt, lang.get());
            }
            return SystemParser.exportOptions(opt);
        }
        return null;
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
            RpgSystemOptions opt = cmd.buildOptions();
            SystemParser.parseOptions(opt, params.getSystemOptions());
            Optional<Locale> lang = parseLang(params);
            if (lang.isPresent())
            {
                result = cmd.execCommand(opt, lang.get(), parseCallerId(params));
            }
            else
            {
                result = cmd.execCommand(opt, parseCallerId(params));
            }
        }
        if (result.isPresent())
        {
            return new ResultDto(result.get());
        }
        return null;
    }
    
    private Optional<UUID> parseCallerId(AleaParams params)
    {
        UUID uuid = Generators.nameBasedGenerator(NAMESPACE).generate(params.getCallerId());
        Optional<UUID> callerId = Optional.ofNullable(uuid);
        return callerId;
    }
    
    private Optional<Locale> parseLang(AleaParams params)
    {
        return parseLang(params.getLangId());
    }
    
    private Optional<Locale> parseLang(String langId)
    {
        Optional<Locale> lang = Optional.empty();
        if ((langId != null) && (!langId.isEmpty()))
        {
            Locale l = new Locale(langId);
            lang = Optional.ofNullable(l);
        }
        return lang;
    }
}