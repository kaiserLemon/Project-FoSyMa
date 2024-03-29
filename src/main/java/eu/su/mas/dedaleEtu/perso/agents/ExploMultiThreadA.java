package eu.su.mas.dedaleEtu.perso.agents;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.platformManagment.startMyBehaviours;
import eu.su.mas.dedaleEtu.perso.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.perso.behaviours.EmptyBv;
import eu.su.mas.dedaleEtu.perso.behaviours.Explo.ExploCountBv;
import eu.su.mas.dedaleEtu.perso.behaviours.ShareMap.ReceiveMapBv;
import eu.su.mas.dedaleEtu.perso.behaviours.ShareMap.ReceivePingBv;
import eu.su.mas.dedaleEtu.perso.behaviours.ShareMap.SendMapBv;
import eu.su.mas.dedaleEtu.perso.behaviours.ShareMap.SendPingBv;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class ExploMultiThreadA extends AbstractDedaleAgent{

    private static final long serialVersionUID = 1L;
    
    private ThreadedBehaviourFactory tbf;
    private MapRepresentation myMap;
    private int nodeCount = 0;

    protected void setup(){
        super.setup();

        tbf = new ThreadedBehaviourFactory();

        final Object[] args = getArguments();

        List<String> list_agentNames = new ArrayList<String>();

        if(args.length == 0){
            System.out.println("Error while creating the agent, names of agent to contact expected");
            System.exit(-1);
        }else{
            int i = 2;
            while (i < args.length) {
                list_agentNames.add((String)args[i]);
                i++;
            }
        }

        //---------- FSM ----------//

        FSMBehaviour exploFSM = new FSMBehaviour(this);
        FSMBehaviour receivePingFSM = new FSMBehaviour(this);

        // STATES
        String ExploCount = "ExploCount";
        String EndExplo = "EndExplo";
        String SendPing = "SendPing";
        String ReceivePong = "ReceivePong";
        String ReceiveMap = "ReceiveMap";
        String SendMap = "SendMap";
        String ReceivePing = "ReceivePing";
        String SendPong = "SendPong";
        String EndReceivePing = "EndReceivePing";

        // Exploration
        exploFSM.registerFirstState(new ExploCountBv(this, this.myMap, this.nodeCount), ExploCount);
        exploFSM.registerLastState(new EmptyBv(null), EndExplo);
        // Send Map
        exploFSM.registerState(new SendPingBv(this, list_agentNames, "PING"), SendPing);
        exploFSM.registerState(new ReceivePingBv(this, 100, "PONG", list_agentNames), ReceivePong);
        exploFSM.registerState(new SendMapBv(this, this.myMap, list_agentNames), SendMap);
        // Receive Ping
        receivePingFSM.registerFirstState(new ReceivePingBv(this, "PING", list_agentNames), ReceivePing);
        receivePingFSM.registerState(new SendPingBv(this, list_agentNames, "PONG"), SendPong);
        receivePingFSM.registerState(new ReceiveMapBv(this, myMap), ReceiveMap);
        receivePingFSM.registerLastState(new EmptyBv(null), EndReceivePing);

        // TRANSITIONS
        exploFSM.registerTransition(ExploCount, SendPing, 1);
        exploFSM.registerTransition(ExploCount, ExploCount, 0);
        exploFSM.registerDefaultTransition(SendPing, ReceivePong);
        exploFSM.registerTransition(ReceivePong, ExploCount, 0);
        exploFSM.registerTransition(ReceivePong, SendMap, 1);
        exploFSM.registerDefaultTransition(SendMap, ExploCount);
        exploFSM.registerTransition(ExploCount, EndExplo, 2);
        
        receivePingFSM.registerTransition(ReceivePing, ReceivePing, 0);
        receivePingFSM.registerTransition(ReceivePing, SendPong, 1);
        receivePingFSM.registerDefaultTransition(SendPong, ReceiveMap);
        receivePingFSM.registerTransition(ReceiveMap, ReceivePing, 0);
        receivePingFSM.registerTransition(ReceiveMap, EndReceivePing, 1);


        this.addBehaviour(tbf.wrap(exploFSM));
        this.addBehaviour(tbf.wrap(receivePingFSM));

        List<Behaviour> lb = new ArrayList<Behaviour>();
        lb.add(exploFSM);
        lb.add(receivePingFSM);

        addBehaviour(new startMyBehaviours(this, lb));

        System.out.println("the agent" + this.getLocalName() + " is started");
    }
}
