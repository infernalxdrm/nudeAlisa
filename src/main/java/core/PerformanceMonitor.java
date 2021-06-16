package core;
import com.sun.management.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;

public class PerformanceMonitor {
    static private final int  availableProcessors =  Runtime.getRuntime().availableProcessors();
   static private long lastSystemTime      = 0;
   static private long lastProcessCpuTime  = 0;
   static private int called=0;

   static public synchronized double getCpuUsage()
    {
        if ( lastSystemTime == 0 )
        {
            baselineCounters();
            if (called<10){
                called++;
                return getMemUsage();
            }
            return Double.NaN;
        }

        long systemTime     = System.nanoTime();
        long processCpuTime = 0;

        if ( getOperatingSystemMXBean() instanceof OperatingSystemMXBean )
        {
            processCpuTime = ( (OperatingSystemMXBean) getOperatingSystemMXBean() ).getProcessCpuTime();
        }

        double cpuUsage = ((double)( processCpuTime - lastProcessCpuTime )) / ((double)( systemTime - lastSystemTime ));

        lastSystemTime     = systemTime;
        lastProcessCpuTime = processCpuTime;

        return (double) cpuUsage /(double) availableProcessors;
    }

   static private void baselineCounters()
    {
        lastSystemTime = System.nanoTime();

        if ( getOperatingSystemMXBean() instanceof OperatingSystemMXBean )
        {
            lastProcessCpuTime = ( (OperatingSystemMXBean) getOperatingSystemMXBean() ).getProcessCpuTime();
        }
    }
    //return bytes

    static private long  getMemUsage(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }
    static  private long getTotalMem(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
    }
    static  private long getOffHeapMemUsage(){
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }
    static private long getOffHeapMem(){
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted();
    }
    public static String print(){
        StringBuilder b = new StringBuilder();
        b.append("CPU CORES AVAILABLE ").append(availableProcessors).append("\n")
                .append("CPU USAGE ").append(getCpuUsage()*100).append("%\n")
                .append("Heap memory usage ").append(((double) getMemUsage()/(double)getTotalMem())*100f).append("%\n")
                .append("NonHeap memory usage ").append(((double) getOffHeapMemUsage()/(double)getOffHeapMem())*100f).append("%\n");
        return b.toString();
    }
    @SneakyThrows
    public static String neofetch()  {
        if (!System.getProperty("os.name").toLowerCase().contains("nix"))return "Non unix system";
    String s= new String(Runtime.getRuntime().exec("neofetch").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return s;
    }
    public static Mono<Void>__(MessageCreateEvent e){
       final MessageChannel channel = e.getMessage().getChannel().block();
       StringBuilder b = new StringBuilder();
       b.append("```css\n").append(neofetch()).append("\n```\n");
       channel.createMessage(print()).block();
       channel.createMessage(b.toString()).block();
       return e.getMessage().getChannel().then();
    }
}
