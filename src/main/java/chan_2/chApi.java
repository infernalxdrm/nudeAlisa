package chan_2;

import chan_2.JsonComponents.*;
import com.google.gson.Gson;
import core.services.MessageManager;
import core.services.Properties;
import core.services.reactions.ReactionListener;
import core.services.reactions.ReactionListenerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;
import utils.StringCutter;
import utils.htmlToDiscord;
import utils.httpUtil;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class chApi {
    Properties p;

    public chApi(Properties p) {
        this.p = p;
    }
    public String getBoards() {
        return "\n" +
                "Разное\n" +
                "Купить пасскод\n" +
                "/d/ - дискуссии о два.ч\n" +
                "/b/ - бред\n" +
                "/o/ - оэкаки\n" +
                "/soc/ - общение\n" +
                "/media/ - анимация\n" +
                "/r/ - просьбы\n" +
                "/api/ - API\n" +
                "/rf/ - убежище\n" +
                "Тивач\n" +
                "Политика\n" +
                "/int/ - international\n" +
                "/po/ - политика\n" +
                "/news/ - новости\n" +
                "/hry/ - х р ю\n" +
                "Тематика\n" +
                "/au/ - автомобили и транспорт\n" +
                "/bi/ - велосипеды\n" +
                "/biz/ - бизнес\n" +
                "/bo/ - книги\n" +
                "/c/ - комиксы и мультфильмы\n" +
                "/em/ - другие страны и туризм\n" +
                "/fa/ - мода и стиль\n" +
                "/fiz/ - физкультура\n" +
                "/fl/ - иностранные языки\n" +
                "/ftb/ - футбол\n" +
                "/hh/ - hip-hop\n" +
                "/hi/ - история\n" +
                "/me/ - медицина\n" +
                "/mg/ - магия\n" +
                "/mlp/ - my little pony\n" +
                "/mo/ - мотоциклы\n" +
                "/mov/ - Фильмы\n" +
                "/mu/ - музыка\n" +
                "/ne/ - животные и природа\n" +
                "/psy/ - психология\n" +
                "/re/ - религия\n" +
                "/sci/ - наука\n" +
                "/sf/ - научная фантастика\n" +
                "/sn/ - паранормальные явления\n" +
                "/sp/ - спорт\n" +
                "/spc/ - космос и астрономия\n" +
                "/tv/ - тв и кино\n" +
                "/un/ - образование\n" +
                "/w/ - оружие\n" +
                "/wh/ - warhammer\n" +
                "/wm/ - военная техника и оружие\n" +
                "/wp/ - обои и высокое разрешение\n" +
                "/zog/ - теории заговора\n" +
                "Творчество\n" +
                "/de/ - дизайн\n" +
                "/di/ - столовая\n" +
                "/diy/ - хобби\n" +
                "/mus/ - музыканты\n" +
                "/pa/ - живопись\n" +
                "/p/ - фото\n" +
                "/wrk/ - РАБота и карьера\n" +
                "/trv/ - путешествия\n" +
                "Техника и софт\n" +
                "/gd/ - gamedev\n" +
                "/hw/ - компьютерное железо\n" +
                "/mobi/ - мобильные устройства и приложения\n" +
                "/pr/ - программирование\n" +
                "/ra/ - радиотехника\n" +
                "/s/ - программы\n" +
                "/t/ - техника\n" +
                "/web/ - веб-мастера\n" +
                "Игры\n" +
                "/bg/ - настольные игры\n" +
                "/cg/ - консоли\n" +
                "/gsg/ - grand strategy games\n" +
                "/ruvn/ - российские визуальные новеллы\n" +
                "/tes/ - the elder scrolls\n" +
                "/v/ - video games\n" +
                "/vg/ - video games general\n" +
                "/wr/ - текстовые авторские рпг\n" +
                "Японская культура\n" +
                "/a/ - аниме\n" +
                "/fd/ - фэндом\n" +
                "/ja/ - японская культура\n" +
                "/ma/ - манга\n" +
                "/vn/ - визуальные новеллы\n" +
                "Взрослым\n" +
                "/fg/ - трапы\n" +
                "/fur/ - фурри\n" +
                "/gg/ - хорошие девушки\n" +
                "/ga/ - геи\n" +
                "/vape/ - электронные сигареты\n" +
                "/h/ - хентай\n" +
                "/ho/ - прочий хентай\n" +
                "/hc/ - hardcore\n" +
                "/e/ - extreme pron\n" +
                "/fet/ - фетиш\n" +
                "/sex/ - секс и отношения\n" +
                "/fag/ - фагготрия";
    }

    public static Mono<Void> help(MessageCreateEvent event) {
        final MessageChannel channel = event.getMessage().getChannel().block();
        assert channel != null;


        channel.createEmbed(spec ->
                spec.setColor(Color.RED)
                        //.setAuthor("Alica bot", "https://github.com/Kw0rker/nudeAlisa", "https://2ch.hk/ing/thumb/1186/14616819779650s.jpg")
                        .setImage("https://2ch.hk/ing/thumb/1186/14616819779650s.jpg")
                        .setTitle("Alisa 2ch guide")
                        .setUrl("https://2ch.hk/")
                        .setDescription(
                                "2ch_boards - list all 2ch boards\n" +
                                        "2ch_board - as parameter take board's name")

                        //  .setThumbnail(photo)
                        //.setFooter("Made by Kworker#0101", photo)
                        .setTimestamp(Instant.now())
        ).block();
        return event.getMessage().getChannel().then();
    }

    public board getBoradFromLink(String link) {
        Gson gson = new Gson();

        try {
            return gson.fromJson(httpUtil.getRequestResponse(link), board.class);
        } catch (Exception e) {
            board error = new board();
            error.Board = " Errror";
            error.threads = new thread[1];
            thread t = new thread();
            t.comment = e.getMessage();
            t.lasthit = 0;
            t.num = "0";
            t.posts_count = 0;
            t.subject = "Error";
            t.timestamp = 0;
            t.views = 1337;
            t.date = "Я ебал кривой апи двача";
            t.files = new file[0];
            t.score = 0;
            t.name = "xuy";
            error.threads[0] = t;
            return error;
        }
    }

    public threadBoard getThreadBoardFormLink(String link) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(httpUtil.getRequestResponse(link), threadBoard.class);
        } catch (Exception e) {
            threadBoard error = new threadBoard();
            error.Board = " Errror";
            error.threads = new posts[1];
            post p = new post();
            p.comment = e.getMessage();
            p.date = "Я ебал кривой апи двача";
            p.files = new file[0];
            p.subject = "Error";
            posts posts = new posts();
            posts.posts = new post[1];
            posts.posts[0] = p;
            error.threads[0] = posts;
            return error;

        }
    }

    public Mono<Void> proceed(MessageCreateEvent e) {

        final MessageChannel channel = e.getMessage().getChannel().block();
        assert channel != null;
        String boardname;
        try {
            boardname = e.getMessage().getContent().split(" ")[1];
        } catch (ArrayIndexOutOfBoundsException es) {
            return e.getMessage().getChannel().then();
        }
        String link = "https://2ch.hk/" + boardname + "/catalog_num.json";
        board board = getBoradFromLink(link);
        AtomicInteger f = new AtomicInteger(0);
        work(e, channel, boardname, link, board, f.get());

        return e.getMessage().getChannel().then();
    }

    private Mono<Void> work(MessageCreateEvent e, MessageChannel channel, String boardname, String link, board board, int f) {
        if (board == null || board.threads == null) return e.getMessage().getChannel().then();
        ReactionListener listener = ReactionListenerManager.of(e.getGuildId().get());

        for (int z = 4 * f; z < 4 * (f + 1) && z < board.threads.length; z++) {
            thread t = board.threads[z];
            String comment = htmlToDiscord.normalize(t.getComment());
            AtomicInteger filesAdded = new AtomicInteger();
            int index = 1500;
            List<String> strings = StringCutter.cut(comment, index);
            for (int i = 0; i < strings.size(); i++) {

                if (i == strings.size() - 1) {
                    int finalI = i;


                    if (filesAdded.get() >= t.getFiles().length) {
                        channel.createEmbed(spec ->
                                spec.setColor(Color.RED)
                                        .setAuthor(t.getName(), null, null)
                                        .setTitle(t.getSubject())
                                        .setUrl(link)
                                        .setDescription(strings.get(finalI))
                                        .addField("Постов в треде", String.valueOf(t.getPosts_count()), true)
                                        .addField("Просмотры", String.valueOf(t.getViews()), false)
                                        .addField("Популярность", String.valueOf(t.getScore()), false)
                                        .addField("Дата", t.getDate(), false)
                        ).block();
                    } else {
                        channel.createEmbed(spec ->
                                spec.setColor(Color.RED)
                                        .setAuthor(t.getName(), null, null)
                                        .setImage("https://2ch.hk" + t.getFiles()[filesAdded.getAndIncrement()].path) //change.
                                        .setTitle(t.getSubject())
                                        .setUrl(link)
                                        .setDescription(strings.get(finalI))
                                        .addField("Постов в треде", String.valueOf(t.getPosts_count()), true)
                                        .addField("Просмотры", String.valueOf(t.getViews()), false)
                                        .addField("Популярность", String.valueOf(t.getScore()), false)
                                        .addField("Дата", t.getDate(), false)
                        ).block();
                    }
//                    if (filesAdded.get() >= t.getFiles().length) {
//                        filesAdded.set(0);
//                    }
                } else {
                    int finalI1 = i;

                    if (filesAdded.get() >= t.getFiles().length) {
                        channel.createEmbed(spec ->
                                spec.setColor(Color.RED)
                                        .setDescription(strings.get(finalI1)))
                                .block();
                    } else {
                        channel.createEmbed(spec ->
                                spec.setColor(Color.RED)
                                        .setImage("https://2ch.hk" + t.getFiles()[filesAdded.getAndIncrement()].path)
                                        .setDescription(strings.get(finalI1)))
                                .block();
                    }
                    if (filesAdded.get() >= t.getFiles().length) {
                        filesAdded.set(0);
                    }
                }

            }
            for (int a = filesAdded.get(); a < t.getFiles().length; a++) {
                int finalA = a;
                channel.createEmbed(spec ->
                                spec.setColor(Color.RED)
                                        .setFooter(t.getSubject(), null)
                                        //  .setAuthor(t.getName(), null, null)
                                        .setImage("https://2ch.hk" + t.getFiles()[finalA].path)
                        //.setTitle(t.getSubject())
                ).block();
            }
            Message m1 = channel.createMessage("Continue reading this thread ? ").block();
            m1.addReaction(ReactionEmoji.unicode("\u27A1")).block();
//            if (p.listener == null) {
//                p.listener = new ReactionListener(e.getGuildId().get());
//            }
            listener.instructions.put(
                    m1.getId(),
                    event -> proceedThreadBoard("https://2ch.hk/" + boardname + "/res/" + t.getNum() + ".json", e)
            );
//            channel.getClient().getEventDispatcher().on(ReactionAddEvent.class)
//                    .filter(reactionAddEvent -> {
//                        return
//                                reactionAddEvent.getMessage().block().equals(m1)
//                                        && reactionAddEvent.getEmoji().equals(ReactionEmoji.unicode("\u27A1"))
//                                        && !(reactionAddEvent.getMember().get().isBot());
//                    })
//                    .map(s -> proceedThreadBoard("https://2ch.hk/" + boardname + "/res/" + t.getNum() + ".json", e))
//                    .subscribe();


//            channel.getClient().getEventDispatcher().on(ReactionAddEvent.class)
//                    .map(ReactionAddEvent::getEmoji)
//                    .filter(reactionEmoji -> ReactionEmoji.unicode("\u27A1").equals(reactionEmoji))
//                    .map(reactionEmoji -> )
//                    .subscribe();
        }
        Message m1 = channel.createMessage("Continue reading this board ? ").block();
        AtomicInteger z = new AtomicInteger(f);
        MessageManager.createReactionListener(Objects.requireNonNull(m1),ReactionEmoji.unicode("\u27A1"), event -> work(e, channel, boardname, link, board, z.incrementAndGet()));
        //p.listener=new ReactionListener(e.getGuildId().get());
        return e.getMessage().getChannel().then();
    }

    public Mono<Void> proceedThreadBoard(String link, MessageCreateEvent e) {
        final MessageChannel channel = e.getMessage().getChannel().block();
        assert channel != null;
        threadBoard t = getThreadBoardFormLink(link);
        if (t == null || t.Board == null || t.Board.equals("")) return e.getMessage().getChannel().then();
        post[] posts = t.getThreads()[0].posts;
        Stack<post> postsStack = new Stack<>();
        List<post> list = Arrays.asList(posts);
        Collections.reverse(list);
        postsStack.addAll(list);
        int x = 0;
        workFromThread(channel, postsStack, x);
//
        return e.getMessage().getChannel().then();
    }

    private Mono<Void> workFromThread(MessageChannel channel, Stack<post> postsStack, int x) {
        for (int i = 4 * x; i < 4 * (x + 1) && postsStack.size() > 0; i++) {
            AtomicInteger filesAdded = new AtomicInteger(0);
            post p = postsStack.pop();
            List<String> comments = StringCutter.cut(htmlToDiscord.normalize(p.comment), 1500);
            for (int j = 0; j < comments.size() - 1; j++) {

                if (filesAdded.get() >= p.files.length) {
                    String com = comments.get(j);
                    channel.createEmbed(message ->
                            message.setColor(Color.DARK_GOLDENROD)
                                    .setDescription(com)
                    )
                            .block();
                } else {
                    String com = comments.get(j);
                    channel.createEmbed(message ->
                            message.setColor(Color.DARK_GOLDENROD)
                                    .setDescription(com)
                                    .setImage("https://2ch.hk/" + p.files[filesAdded.getAndIncrement()].path)
                    )
                            .block();
                }
                //if (filesAdded.get() >= p.files.length) filesAdded.set(0);
            }
            if (p.files.length > 0)
                if (filesAdded.get() >= p.files.length) {
                    channel.createEmbed(message ->
                            message.setTitle(p.subject)
                                    .setAuthor(p.name, null, null)
                                    .addField("Дата", p.date, false)
                                    .setDescription(comments.get(comments.size() - 1))
                                    .setColor(Color.DARK_GOLDENROD)
                    )

                            .block();
                } else {
                    channel.createEmbed(message ->
                            message.setTitle(p.subject)
                                    .setAuthor(p.name, null, null)
                                    .addField("Дата", p.date, false)
                                    .setDescription(comments.get(comments.size() - 1))
                                    .setColor(Color.DARK_GOLDENROD)
                                    .setImage("https://2ch.hk/" + p.files[filesAdded.getAndIncrement()].path)
                    )

                            .block();
                }
            else channel.createEmbed(message ->
                    message.setTitle(p.subject)
                            .setAuthor(p.name, null, null)
                            .addField("Дата", p.date, false)
                            .setDescription(comments.get(comments.size() - 1))
                            .setColor(Color.DARK_GOLDENROD)).block();


            for (int a = filesAdded.get(); a < p.files.length; a++) {
                channel.createEmbed(message ->
                        message.setColor(Color.DARK_GOLDENROD)
                                .setImage("https://2ch.hk/" + p.files[filesAdded.getAndIncrement()].path))
                        .block();
            }
        }
        Message m1 = channel.createMessage("Continue reading this thread ? ").block();
        AtomicInteger z = new AtomicInteger(x);
        MessageManager.createReactionListener(m1,ReactionEmoji.unicode("\u27A1"),event -> workFromThread(channel, postsStack, z.incrementAndGet()));
        //  p.listener=new ReactionListener(channel.getId());
        return channel.getLastMessage().then();
    }

}
