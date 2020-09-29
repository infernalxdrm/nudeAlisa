package core.services;
import discord4j.core.event.domain.channel.ChannelEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
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

public class Parser implements Subscriber<String>,Consumer<String> {
    final static generator imgurGenerator=new imgurGenerator();
    final static generator lightshotGenerator=new screenshootsGenerator();
    final static validator imgurValidator=new imgurValidator();
    final static validator lightshotValidator=new imgurValidator();
    private MessageCreateEvent e;
    Parser(MessageCreateEvent e){
        this.e =e;
    }
    public  List<String> imgurGenerate(String number){
        int numberInt=Integer.parseInt(number);
        return   getList(numberInt,imgurGenerator,imgurValidator);

    }

    private  List<String> getList(int a,generator g,validator v) {
        List<String> strings=new LinkedList<>();
        if (a>100){
            strings.add("number is to big");
            return strings;
        }
        if (a<0){
            strings.add("number must be positive");
            return strings;
        }
        for (int i = 0; i < a; i++) {
            try {
               onNext(v.validate(g.generate()));
            }
            catch (RuntimeException e){
                i--;
            }
        }
        this.onComplete();
        return strings;
    }


    @Override
    public void onSubscribe(Subscription subscription) {
    }

    @Override
    public void onNext(String s) {
        e.getMessage().getChannel()
                .map(channel -> channel.createMessage(s)).then().subscribe();

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void accept(String s) {

    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return null;
    }
}
