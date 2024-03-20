package eu.su.mas.dedaleEtu.perso.behaviours.ShareMap;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMapBv extends OneShotBehaviour{

    private static final long serialVersionUID = 1L;

  private MapRepresentation myMap;
  private String sender;

  public ReceiveMapBv(Agent a, MapRepresentation myMap) {
    super(a);
    this.myMap = myMap;
  }

  @Override
    public void action() {
    MessageTemplate msgTemplate = MessageTemplate.and(
        MessageTemplate.MatchProtocol("SHARE-MAP"), 
        MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    ACLMessage msg = this.myAgent.receive(msgTemplate);
    if (msg != null) {
        SerializableSimpleGraph<String,MapAttribute> sgreceived = null;
        try{
            sgreceived = (SerializableSimpleGraph<String,MapAttribute>) msg.getContentObject();
        }catch(UnreadableException e){
            e.printStackTrace();
        }
        this.myMap.mergeMap(sgreceived);
    }
  }

  public MapRepresentation getMap(){
    return myMap;
  }

  public String getSender(){
    return sender;
  }
    
}
