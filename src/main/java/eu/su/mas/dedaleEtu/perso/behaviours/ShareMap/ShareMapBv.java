package eu.su.mas.dedaleEtu.perso.behaviours.ShareMap;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

public class ShareMapBv extends FSMBehaviour{

    private static final long serialVersionUID = 1L;

    private MapRepresentation myMap;
    private Agent myAgent;
    private List<String> list_agentNames;
    private List<String> listReceivers = new ArrayList<>();

    public ShareMapBv(Agent a, MapRepresentation myMap) {
        super(a);
        this.myMap = myMap;
        this.registerFirstState(new ReceivePingBv(myAgent, "PING"), "receivePing");
        this.registerState(new SendPingBv(myAgent, list_agentNames, "PING"), "sendPing");
        this.registerState(new ReceivePingBv(myAgent, "PONG"), "receivePong");
        this.registerState(new SendPingBv(myAgent, listReceivers, "PONG"), "sendPong");
        this.registerLastState(new SendMapBv(myAgent, myMap, listReceivers), "sendMap");
        this.registerLastState(new ReceiveMapBv(myAgent, myMap), "receiveMap");
    }
    
}
