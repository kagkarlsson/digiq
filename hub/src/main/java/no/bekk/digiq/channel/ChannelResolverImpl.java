package no.bekk.digiq.channel;

import javax.annotation.Resource;

import no.bekk.digiq.channel.smtp.SmtpChannel;

import org.springframework.stereotype.Component;

@Component
public class ChannelResolverImpl implements ChannelResolver {

    @Resource
    private SmtpChannel smtpAdapter;
    
    @Override
    public TwoWayChannel getTwoWayChannel(String key) {
        // TODO: Detta måste göras automatiskt via spring/reflection magic.
        // hårdkodad for now.
        return smtpAdapter;
    }

}
