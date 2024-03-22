package eu.su.mas.dedaleEtu.perso.behaviours.ShareMap;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.perso.behaviours.EmptyBv;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class ShareMapBv extends TickerBehaviour{

    private static final long serialVersionUID = 1L;

    private MapRepresentation myMap;
    private Agent myAgent;
    private List<String> list_agentNames;
    private List<String> listSenders = new ArrayList<>();
    private long waitingTime;

    public ShareMapBv(Agent a, long period, long waitingTime, MapRepresentation myMap, List<String> list_agentsNames) {
        super(a, period);
        this.myMap = myMap;
        this.list_agentNames = list_agentsNames;
        this.waitingTime = waitingTime;
    }

    @Override
    protected void onTick() {
        FSMBehaviour fsm = new FSMBehaviour();

        fsm.registerFirstState(new ReceivePingBv(myAgent, this.waitingTime, "PING", listSenders), "receivePing");
        fsm.registerState(new SendPingBv(myAgent, list_agentNames, "PING"), "sendPing");
        fsm.registerState(new ReceivePingBv(myAgent, this.waitingTime, "PONG", listSenders), "receivePong");
        fsm.registerState(new SendPingBv(myAgent, listSenders, "PONG"), "sendPong");
        fsm.registerLastState(new SendMapBv(myAgent, myMap, listSenders), "sendMap");
        fsm.registerLastState(new ReceiveMapBv(myAgent, this.waitingTime, myMap), "receiveMap");
        fsm.registerLastState(new EmptyBv(myAgent), "endSate");

        fsm.registerTransition("receivePing", "sendPing", 0);
        fsm.registerTransition("receivePing", "sendPong", 1);
        fsm.registerDefaultTransition("sendPing", "receivePoing");
        fsm.registerDefaultTransition("sendPong", "receiveMap");
        fsm.registerTransition("receivePong", "sendMap", 1);
        fsm.registerTransition("receivePong", "enState", 0);
    }
    
}
