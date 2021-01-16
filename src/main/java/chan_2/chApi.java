package chan_2;

import chan_2.JsonComponents.board;
import chan_2.JsonComponents.file;
import chan_2.JsonComponents.thread;
import com.google.gson.Gson;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;
import utils.StringCutter;
import utils.htmlToDiscord;
import utils.httpUtil;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class chApi {
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
        int x = 0;
        for (thread t : board.threads) {
            if (x >= 20) break;
            String comment = htmlToDiscord.normalize(t.getComment());
            AtomicInteger filesAdded = new AtomicInteger();
            int index = 1500;
            List<String> strings = StringCutter.cut(comment, index);
            for (int i = 0; i < strings.size(); i++) {

                if (i == strings.size() - 1) {
                    int finalI = i;
                    channel.createEmbed(spec ->
                            spec.setColor(Color.RED)
                                    .setAuthor(t.getName(), null, null)
                                    .setImage(1 >= t.getFiles().length ? "https://i.imgur.com/dzn8huv.png" : "https://2ch.hk" + t.getFiles()[filesAdded.getAndIncrement()].path) //change.
                                    .setTitle(t.getSubject())
                                    .setUrl(link)
                                    .setDescription(strings.get(finalI))
                                    .addField("Постов в треде", String.valueOf(t.getPosts_count()), true)
                                    .addField("Просмотры", String.valueOf(t.getViews()), false)
                                    .addField("Популярность", String.valueOf(t.getScore()), false)
                                    .addField("Дата", t.getDate(), false)
                    ).block();
                    if (filesAdded.get() >= t.getFiles().length) {
                        filesAdded.set(0);
                    }
                } else {
                    int finalI1 = i;
                    channel.createEmbed(spec ->
                            spec.setColor(Color.RED)
                                    .setImage(1 >= t.getFiles().length ? "https://i.imgur.com/dzn8huv.png" : "https://2ch.hk" + t.getFiles()[filesAdded.getAndIncrement()].path) //change.
                                    .setDescription(strings.get(finalI1)))
                            .block();
                    if (filesAdded.get() >= t.getFiles().length) {
                        filesAdded.set(0);
                    }
                }
            }
            for (int a = filesAdded.get(); a < t.getFiles().length; a++) {
                int finalA = a;
                channel.createEmbed(spec ->
                        spec.setColor(Color.RED)
                                .setAuthor(t.getName(), null, null)
                                .setImage("https://2ch.hk" + t.getFiles()[finalA].path)
                                .setTitle(t.getSubject())
                ).block();
            }

//            channel.createEmbed(spec ->
//                    spec.setColor(Color.RED)
//                            .setAuthor(t.getName(), null,null)
//                            .setImage(t.getFiles()==null? "https://i.imgur.com/dzn8huv.png" : "https://2ch.hk"+t.getFiles()[0].path )
//                            .setTitle(t.getSubject())
//                            .setUrl(link)
//                            .setDescription(htmlToDiscord.normalize(t.getComment()))
//                            .addField("Постов в треде", String.valueOf(t.getPosts_count()), true)
//                            .addField("Просмотры", String.valueOf(t.getViews()), false)
//                            .addField("Популярность",String.valueOf(t.getScore()),false)
//                            .addField("Дата",t.getDate(),false)
//                            //.addField()
//                            //.setThumbnail(photo)
//                            //.setFooter("Made by Kworker#0101", photo)
//                            //.setTimestamp(Instant.now())
//            ).block();
            x++;
        }
        return e.getMessage().getChannel().then();
    }

}
