package com.example;

import akka.actor.typed.ActorSystem;

public class Main {

    /*public static void main(String[] args) {
        ActorSystem<String> actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem");

        actorSystem.tell("say hello");
        actorSystem.tell("who are you");
        actorSystem.tell("create a child");
        actorSystem.tell("are you there?");

    }*/

    public static void main(String[] args) {
        ActorSystem<ManagerBehavior.Command> managerActor = ActorSystem.create(ManagerBehavior.create(), "ManagerActor");
        managerActor.tell(new ManagerBehavior.InstructionCommand("start"));
    }
}
