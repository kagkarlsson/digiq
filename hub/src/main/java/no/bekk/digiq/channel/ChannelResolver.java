package no.bekk.digiq.channel;

public interface ChannelResolver {

    public abstract TwoWayChannel getTwoWayChannel(String key);

}