package core.services;
import discord4j.core.event.domain.channel.ChannelEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import parsers.netstalking.*;
import parsers.netstalking.generators.generator;
import parsers.netstalking.generators.imgurGenerator;
import parsers.netstalking.generators.screenshootsGenerator;
import parsers.netstalking.validators.imgurValidator;
import parsers.netstalking.validators.validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class Parser  {
    final static generator imgurGenerator=new imgurGenerator();
    final static generator lightshotGenerator=new screenshootsGenerator();
    final static validator imgurValidator=new imgurValidator();
    final static validator lightshotValidator=new imgurValidator();

    Mono<Void> get(MessageCreateEvent  e,String command){
        final MessageChannel channel = e.getMessage().getChannel().block();
        String [] params=command.split(" ");
        switch (params[0]){
            case "imgur" :
                try {

                    int request_number= Integer.parseInt(params[1]);
                    if (request_number>100)return channel.createMessage("Number must be less than 100").then();
                    for (int i = 0; i < request_number; i++) {
                        try {
                        channel.createMessage(imgurValidator.validate(imgurGenerator.generate()));}
                        catch (RuntimeException se){i--;}
                    }
                }
                catch (NumberFormatException ex){
                    return channel.createMessage("Number must be an integer").then();
                }
                break;

            case "lightroom" :
                try {
                    int request_number= Integer.parseInt(params[1]);
                    if (request_number>100)return channel.createMessage("Number must be less than 100").then();
                    for (int i = 0; i < request_number; i++) {
                        try {
                            channel.createMessage(lightshotValidator.validate(lightshotGenerator.generate()));}
                        catch (RuntimeException se){i--;}
                    }
                }
                catch (NumberFormatException ex){
                    return channel.createMessage("Number must be an integer").then();
                }
                break;

         }
         return channel.createMessage("❤").then();

    }

}
