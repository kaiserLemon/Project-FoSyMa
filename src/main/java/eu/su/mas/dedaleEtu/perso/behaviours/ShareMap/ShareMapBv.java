package eu.su.mas.dedaleEtu.perso.behaviours.ShareMap;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.perso.behaviours.EmptyBv;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class ShareMapBv extends FSMBehaviour{

    private static final long serialVersionUID = 1L;

    private MapRepresentation myMap;
    private Agent myAgent;
    private List<String> list_agentNames;
    private List<String> listSenders = new ArrayList<>();
    private long waitingTime;

    public ShareMapBv(Agent a, long waitingTime, MapRepresentation myMap, List<String> list_agentsNames) {
        super(a);
        this.myMap = myMap;
        this.list_agentNames = list_agentsNames;
        this.waitingTime = waitingTime;

        this.registerFirstState(new ReceivePingBv(myAgent, this.waitingTime, "PING", listSenders), "receivePing");
        this.registerState(new SendPingBv(myAgent, list_agentNames, "PING"), "sendPing");
        this.registerState(new ReceivePingBv(myAgent, this.waitingTime, "PONG", listSenders), "receivePong");
        this.registerState(new SendPingBv(myAgent, listSenders, "PONG"), "sendPong");
        this.registerLastState(new SendMapBv(myAgent, myMap, listSenders), "sendMap");
        this.registerLastState(new ReceiveMapBv(myAgent, this.waitingTime, myMap), "receiveMap");
        this.registerLastState(new EmptyBv(myAgent), "endSate");

        this.registerTransition("receivePing", "sendPing", 0);
        this.registerTransition("receivePing", "sendPong", 1);
        this.registerDefaultTransition("sendPing", "receivePoing");
        this.registerDefaultTransition("sendPong", "receiveMap");
        this.registerTransition("receivePong", "sendMap", 1);
        this.registerTransition("receivePong", "enState", 0);

    }
    
}
