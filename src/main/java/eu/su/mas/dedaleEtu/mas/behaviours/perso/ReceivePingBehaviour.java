package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceivePingBehaviour extends TickerBehaviour{

    private String sender;

    public ReceivePingBehaviour(Agent a, long period){
        super(a, period);
    }

    @Override
    protected void onTick() {
        MessageTemplate msgTemplate = MessageTemplate.and(
            MessageTemplate.MatchProtocol("PING"), 
            MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        ACLMessage msg = this.myAgent.receive(msgTemplate);
        if (msg != null) {
            sender = msg.getSender().getLocalName();
        }
    }
}
