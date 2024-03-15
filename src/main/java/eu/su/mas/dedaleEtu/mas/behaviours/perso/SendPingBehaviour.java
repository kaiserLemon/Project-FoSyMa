package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SendPingBehaviour extends TickerBehaviour{

    private List<String> receivers;

    public SendPingBehaviour(Agent a, long period, List<String> receivers) {
        super(a, period);
        this.receivers=receivers;
    }
    
    @Override
    protected void onTick() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setProtocol("PING");
        msg.setSender(this.myAgent.getAID());
        for (String agent : receivers){
            msg.addReceiver(new AID(agent, AID.ISLOCALNAME));
        }
        ((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
    }
    
}
